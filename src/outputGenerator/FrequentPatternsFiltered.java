/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import miner.MinerConfiguration;
import miner.Triple;
import testValidity.ValidPattern;
import testValidity.ValidatePatterns;

public class FrequentPatternsFiltered
{
	public static void write(ValidatePatterns vpObject) throws FileNotFoundException
	{
		System.out.println("Writing Frequent Patterns Filtered");
		PrintWriter gift = new PrintWriter(MinerConfiguration.FrequentPatternsFilteredFileName
				+ vpObject.fileNameSuffix);

		for (String pattern : vpObject.validPatterns.keySet())
		{
			ValidPattern validPatternObj = vpObject.validPatterns.get(pattern);
			String words[] = pattern.trim().split("\\s+");
			if (validPatternObj.occurrences.size() >= vpObject.Thresholds.get(words.length))
			{
				vpObject.totalValidPatterns++;
				gift.write(pattern + " : ");

				String triplesString = "";
				for (Triple t : validPatternObj.occurrences)
				{
					triplesString += "( " + t.sentenceId + "," + t.start + "," + t.end + " ) ";
				}
				triplesString.trim();
				gift.write(triplesString + "\n");
			}
		}
		gift.close();
	}
}