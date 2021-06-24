/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import miner.MinerConfiguration;
import miner.Triple;
import testValidity.ValidPattern;
import testValidity.ValidatePatterns;

public class Coverage
{
	public static void write(ValidatePatterns vpObject) throws FileNotFoundException
	{
		System.out.println("Writing coverage statistics.");
		boolean matrix[][] = new boolean[vpObject.totalSentences + 1][vpObject.maxLengthOfSentence];
		long rowWiseCoverage[][] = new long[vpObject.totalSentences + 1][2];
		HashMap<String, Double> coverageMap = new HashMap<String, Double>();

		for (int i = 0; i < vpObject.totalSentences; i++)
		{
			for (int j = 0; j < vpObject.maxLengthOfSentence; j++)
			{
				matrix[i][j] = false;
			}
		}

		for (String pattern : vpObject.validPatterns.keySet())
		{
			ValidPattern validPatternObj = vpObject.validPatterns.get(pattern);
			String words[] = pattern.trim().split("\\s+");
			if (validPatternObj.occurrences.size() >= vpObject.Thresholds.get(words.length))
			{
				for (Triple triple : validPatternObj.occurrences)
				{
					int row = triple.sentenceId;
					// System.out.println("The pattern was : " +
					// validPatternObj.pattern);
					// System.out.println("Sentence was : " +
					// document.sentences.get(row).text);
					for (int column = triple.start; column <= triple.end; column++)
					{
						matrix[row][column] = true;
						// String sentence = document.sentences.get(row).text;
						// String arr[] = sentence.trim().split("\\s+");
						// System.out.println("Word is : " + arr[column]);
					}
					// Scanner in = new Scanner(System.in);
					// int x = in.nextInt();
				}
			}
		}

		long totalWords = 0;
		long totalWordsCovered = 0;
		String notCoveredSequences = "";
		int prev = -1;

		PrintWriter pw = new PrintWriter(MinerConfiguration.CoverageStatsFileName + vpObject.fileNameSuffix);
		PrintWriter pwNotCovered = new PrintWriter(MinerConfiguration.NotCoveredSentencesFileName
				+ vpObject.fileNameSuffix);
		for (int i = 1; i <= vpObject.totalSentences; i++)
		{
			String sentence = vpObject.document.sentences.get(i).text;
			String arr[] = sentence.trim().split("\\s+");
			rowWiseCoverage[i][0] = arr.length;
			rowWiseCoverage[i][1] = 0;
			notCoveredSequences = "";
			prev = -1;
			int count = 0;

			pwNotCovered.write("For Sentence Id: " + i + "\n");
			pwNotCovered.write("Sentence: " + sentence + "\n");
			pwNotCovered.write("Uncovered words/sequences : \n");

			for (int j = 0; j < rowWiseCoverage[i][0]; j++)
			{
				totalWords++;
				if (matrix[i][j] == true || arr[j].equals(".") || arr[j].equals(",") || arr[j].equals("(")
						|| arr[j].equals(")") || arr[j].equals("?") || arr[j].equals(";") || arr[j].equals(":")
						|| arr[j].equals("-"))
				{
					rowWiseCoverage[i][1]++;
					totalWordsCovered++;
				} else
				{
					if (prev == -1 || prev == j - 1)
					{
						notCoveredSequences += arr[j] + " ";
						prev = j;
					} else
					{
						if (notCoveredSequences.length() > 0)
						{
							// System.out.println("sentence : " + i + " , '" + notCoveredSequences +
							// "' is not covered.");
							pwNotCovered.write(++count + ". '" + notCoveredSequences.trim() + "' (Index : "
									+ (prev - (notCoveredSequences.trim().split("\\s+").length) + 1) + " to " + prev
									+ ")\n");
						}
						prev = j;
						notCoveredSequences = arr[j] + " ";
					}
					// System.out.println("sentence : " + i + " , '" + arr[j] + j + "' is not covered.");
				}
			}

			if (notCoveredSequences.length() > 0)
			{
				// System.out.println("sentence : " + i + " , '" + notCoveredSequences.trim() + "' is not covered.");
				pwNotCovered.write(++count + ". '" + notCoveredSequences.trim() + "' (Index : "
						+ (prev - (notCoveredSequences.trim().split("\\s+").length) + 1) + " to " + prev + ")\n");
			}

			double percent = (rowWiseCoverage[i][1] * 100.0) / rowWiseCoverage[i][0];

			pwNotCovered.write("Total Words : " + rowWiseCoverage[i][0]);
			pwNotCovered.write("\tCovered Words: " + rowWiseCoverage[i][1]);
			pwNotCovered.write("\tPercentage : " + percent + " %");
			pwNotCovered.write("\n");
			pwNotCovered.write("----------------------------------\n");

			pw.write("Sentence : " + i + "\n");
			pw.write("Total : " + rowWiseCoverage[i][0]);
			pw.write("\tCovered : " + rowWiseCoverage[i][1]);
			pw.write("\tPercentage : " + percent + " %");
			pw.write("\n");

			coverageMap.put(sentence, percent);

			// System.out.println("Sentence : " + i + " total , covered : " +
			// rowWiseCoverage[i][0] + " , "
			// + rowWiseCoverage[i][1] + " : " + percent + " %");
		}

		int buckets = 20;
		int[] frequency = new int[buckets + 1];

		Arrays.fill(frequency, 0);

		for (String s : coverageMap.keySet())
		{
			double percent = coverageMap.get(s);
			int bucket = (int) (percent / (100 / buckets));
			frequency[bucket]++;
		}

		vpObject.coverage = (totalWordsCovered * 100.0) / totalWords;
		pw.write("---------------------------------------------------\n");
		pw.write((totalWordsCovered * 100.0) / totalWords + "%\n");
		pw.write("Bucket Information : \n");

		for (int i = 0; i <= buckets; i++)
		{
			int bucketSize = 100 / buckets;

			if (i < buckets)
			{
				pw.write(bucketSize * i + " to " + bucketSize * (i + 1) + " : " + frequency[i] + "\n");
			} else
			{
				pw.write(bucketSize * i + " : " + frequency[i] + "\n");
			}
		}

		pw.close();
		pwNotCovered.close();
	}
}