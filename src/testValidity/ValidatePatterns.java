/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package testValidity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miner.NonTerminal;
import miner.Pattern;
import miner.Triple;
import outputGenerator.Coverage;
import outputGenerator.FrequentPatternTriples;
import outputGenerator.FrequentPatternsFiltered;
import outputGenerator.GoodPatterns;
import outputGenerator.PatternsPerSentence;
import outputGenerator.Statistics;
import _stanfordParser.Document;
import edu.stanford.nlp.trees.Tree;

/* @TODO Ideally this should be a static class */
public class ValidatePatterns
{
	public Document document = new Document(_stanfordParser.StanfordParserConfiguration.InputFileName,
			_stanfordParser.StanfordParserConfiguration.SerializationFileName);
	public HashMap<Integer, Integer> Thresholds;
	public HashMap<String, ValidPattern> validPatterns;
	static HashMap<String, ArrayList<ArrayList<NonTerminal>>> specificNonTerminals;
	public HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>> frequentGlobalPatterns;
	public ArrayList<ValidPattern> frequentValidPatterns;
	public double coverage;
	public int ThForLengthOnePatterns;
	public long totalPatterns;
	public long totalInstances;
	public long totalValidPatterns;
	public long totalValidInstances;
	public String fileNameSuffix;
	public int totalSentences;
	public int maxLengthOfSentence = 300;

	public ValidatePatterns(HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>> frequentGlobalPatterns,
			HashMap<String, ArrayList<ArrayList<NonTerminal>>> nonTerminalInstances,
			HashMap<Integer, Integer> ThresholdMap, int totalSentences, String fileNameSuffix)
	{
		this.fileNameSuffix = fileNameSuffix;
		Thresholds = new HashMap<Integer, Integer>(ThresholdMap);
		ValidatePatterns.specificNonTerminals = nonTerminalInstances;
		this.frequentGlobalPatterns = frequentGlobalPatterns;
		this.totalSentences = totalSentences;
		totalPatterns = 0;
		totalInstances = 0;
		totalValidPatterns = 0;
		totalValidInstances = 0;
		coverage = 0.0;
		validPatterns = new HashMap<String, ValidPattern>();
		frequentValidPatterns = new ArrayList<ValidPattern>();
	}

	public void validate() throws IOException
	{
		long start = System.currentTimeMillis();

		checkValidity();
		findFrequentValidPatterns();
		writeOutputs();

		long end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) / 1000.0 + " seconds");
	}

	public void checkValidity() throws IOException
	{
		document.synthesizeDocument(true);
		System.out.println("Checking the validity of the patterns.");
		int totalPatternsOfLengthOne = 0;
		for (int length : frequentGlobalPatterns.keySet())
		{
			HashMap<miner.Pattern, ArrayList<Triple>> globalPatternsForCurentL = frequentGlobalPatterns.get(length);
			for (Map.Entry<Pattern, ArrayList<Triple>> entry : globalPatternsForCurentL.entrySet())
			{
				for (Triple triple : entry.getValue())
				{
					totalInstances++;
					String pattern = entry.getKey().toString();
					triple.end = triple.end - 1; // end is stored as +1 in Triple class
					boolean includeLengthOne = (pattern.indexOf(32) == -1 && pattern.startsWith("CAT"));
					if (includeLengthOne)
					{
						System.out.println("including : " + pattern);
						totalPatternsOfLengthOne++;
					}

					if ((pattern.indexOf(32) != -1 && isValid(triple)) || includeLengthOne)
					{
						totalValidInstances++;
						ValidPattern validPatternObj = null;
						if (validPatterns.containsKey(pattern))
						{
							validPatternObj = validPatterns.get(pattern);
						} else
						{
							validPatternObj = new ValidPattern(pattern);
							validPatternObj.nonTerminals = specificNonTerminals.get(pattern.trim());
							validPatterns.put(pattern, validPatternObj);
						}
						validPatternObj.occurrences.add(triple);
					}
				}
			}
		}
		System.out.println("Total patterns of length one included : " + totalPatternsOfLengthOne);
	}

	public boolean isValid(Triple triple)
	{
		Tree tree = (document.sentences.get(triple.sentenceId)).parseTree;
		List<Tree> leaves = tree.getLeaves();
		List<Tree> aptLeaves = tree.joinNode(leaves.get(triple.start), leaves.get(triple.end)).getLeaves();
		if (aptLeaves.size() == (triple.end - triple.start + 1))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void findFrequentValidPatterns()
	{
		for (String pattern : validPatterns.keySet())
		{
			ValidPattern validPatternObj = validPatterns.get(pattern);
			String words[] = pattern.trim().split("\\s+");
			if (validPatternObj.occurrences.size() >= Thresholds.get(words.length))
			{
				validPatternObj.buildUniqueOccurrences(document);
				frequentValidPatterns.add(validPatternObj);
			}
		}
	}

	public void writeOutputs() throws IOException
	{
		FrequentPatternTriples.write(frequentGlobalPatterns, fileNameSuffix);
		FrequentPatternsFiltered.write(this);
		submodular.GoodPatterns.submodular(fileNameSuffix);
		GoodPatterns.write(this.frequentValidPatterns, fileNameSuffix);
		Coverage.write(this);
		Statistics.write(this);
		for (ValidPattern vp : frequentValidPatterns)
		{
			vp.generatePatternsPerSentence(fileNameSuffix);
		}
		// PatternsPerSentence.write(document, true, fileNameSuffix);
		PatternsPerSentence.write(document, false, fileNameSuffix);
	}
}