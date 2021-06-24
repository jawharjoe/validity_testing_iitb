/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import miner.MinerConfiguration;
import miner.NonTerminal;
import miner.Triple;
import testValidity.ValidPattern;
import _stanfordParser.Document;

public class GoodPatterns
{
	public static void write(ArrayList<ValidPattern> frequentValidPatterns, String fileNameSuffix) throws IOException
	{
		System.out.println("Writing the good patterns.");
		PrintWriter pwPatterns = new PrintWriter(MinerConfiguration.GoodInstancesFileName + fileNameSuffix);
		PrintWriter pwNonTerminals = new PrintWriter(MinerConfiguration.NonTerminalsPerPatternFileName + fileNameSuffix);
		for (ValidPattern vp : frequentValidPatterns)
		{
			printUniqueOccurrences(vp, pwPatterns, pwNonTerminals);
		}

		pwPatterns.close();
		pwNonTerminals.close();
	}

	public static void printUniqueOccurrences(ValidPattern vpObject, PrintWriter pw, PrintWriter pw1)
			throws IOException
	{

		pw.write("Pattern : " + vpObject.text + " ; Total occurrences = " + vpObject.occurrences.size()
				+ " ; The various instances are :" + "\n");
		for (String instance : vpObject.uniqueInstances.keySet())
		{
			List<Triple> instanceList = vpObject.uniqueInstances.get(instance);
			pw.write("Instance : " + instance + "( Total Occurrences = " + instanceList.size() + " ) : " + "\n");
			pw.write("Sentence IDs : ");
			for (Triple triple : instanceList)
			{
				pw.write(triple.sentenceId + ", ");
			}
			pw.write("\n----------------------------------------------------------\n");
		}
		pw.write("*************************************************\n");

		if (vpObject.nonTerminals != null)
		{
			pw1.write("Pattern : {" + vpObject.text + "}" + ". The various instances are :" + "\n");
			for (ArrayList<NonTerminal> nts : vpObject.nonTerminals)
			{
				if (nts != null && nts.size() != 0)
				{
					for (NonTerminal nt : nts)
					{
						pw1.write("{" + nt.toString() + "}, ");
					}
					pw1.write("\n----------------------------------------------------------\n");
				} else
				{
					pw1.write("No non terminals in this sentence\n");
				}
			}
			pw1.write("*************************************************\n");
		}
	}

	public void printOccurrences(ValidPattern vpObject, Document document, PrintWriter pw)
	{
		pw.write(vpObject.text + " : " + vpObject.occurrences.size() + " : " + "\n");
		for (Triple triple : vpObject.occurrences)
		{
			String words[] = (document.sentences.get(triple.sentenceId).text).trim().split("\\s+");
			pw.write(triple.sentenceId + " --> ");
			for (int word = triple.start; word <= triple.end; word++)
				pw.write(words[word] + " ");

			pw.write("\n");
		}
		pw.write("*************************************************\n");
	}
}