/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package cleaning;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import miner.MinerConfiguration;
import _stanfordParser.Document;

public class GenerateInput
{
	public static void generateInputForMiner(Document document) throws FileNotFoundException
	{
		PrintWriter pw = new PrintWriter(MinerConfiguration.ContestInputFileName);

		pw.println(document.sentences.size() - 1);

		for (int sentenceNumber = 1; sentenceNumber < document.sentences.size(); sentenceNumber++)
		{
			pw.println(sentenceNumber + "- " + document.sentences.get(sentenceNumber).text);
		}

		pw.println(document.rules.size());

		for (int ruleNumber = 0; ruleNumber < document.rules.size(); ruleNumber++)
		{
			pw.println(document.rules.get(ruleNumber));
		}

		pw.println("0");

		pw.close();
	}
}