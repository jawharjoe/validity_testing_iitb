/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import miner.MinerConfiguration;
import miner.NonTerminal;
import testValidity.ValidPattern;
import testValidity.ValidPatternInstance;
import _stanfordParser.Document;

public class PatternsPerSentence
{
	public static void write(Document document, boolean All, String fileNameSuffix) throws FileNotFoundException
	{
		TreeMap<Integer, List<ValidPatternInstance>> ppsToPrint;
		String ppsFileName = "";
		if (All)
		{
			System.out.println("Writing all PPS");
			ppsToPrint = ValidPattern.allPPS;
			ppsFileName = MinerConfiguration.AllPPSFileName + fileNameSuffix;
		} else
		{
			System.out.println("Writing submodular PPS");
			ppsToPrint = ValidPattern.submodularPPS;
			ppsFileName = MinerConfiguration.SubmodularPPSFileName + fileNameSuffix;
		}

		boolean showOnConsole = false;
		PrintWriter pw = new PrintWriter(ppsFileName);
		for (Integer sentenceId : ppsToPrint.keySet())
		{
			pw.write("****************************************************************\n\n");
			pw.write("Sentence id : " + sentenceId + "\n");
			pw.write("Sentence : " + (document.sentences.get(sentenceId).text).trim() + "\n");
			pw.write("Total patterns (Unique/Non-unique) : " + ppsToPrint.get(sentenceId).size() + "\n");
			pw.write("\n\n--------------------------------------------------------------\n");
			if (showOnConsole)
			{
				System.out.print("****************************************************************\n\n");
				System.out.print("Sentence id : " + sentenceId + "\n");
				System.out.print("Sentence : " + (document.sentences.get(sentenceId).text).trim() + "\n");
				System.out.print("Total patterns (Unique/Non-unique) : " + ppsToPrint.get(sentenceId).size() + "\n");
				System.out.print("\n\n--------------------------------------------------------------\n");
			}
			int patternNumber = 0;
			for (ValidPatternInstance pattern : ppsToPrint.get(sentenceId))
			{
				++patternNumber;
				if (showOnConsole)
				{
					System.out.print((patternNumber) + " -> " + pattern + "\n");
				}
				pw.write((patternNumber) + " -> " + pattern + "\n");
				if (!pattern.vp.nonTerminals.get(0).isEmpty())
				{
					pw.write("The other values of the non terminals for this pattern : \n");
					for (ArrayList<NonTerminal> nt : pattern.vp.nonTerminals)
					{
						pw.println(nt.toString());
					}
					if (showOnConsole)
					{
						System.out.print("The other values of the non terminals for this pattern : \n");
						int count = 0;
						for (ArrayList<NonTerminal> nt : pattern.vp.nonTerminals)
						{
							System.out.println(nt.toString());
							count++;
							if (count == 5)
								break;
						}
					}
				}
				pw.write("--------------------------------------------------------------\n");
				if (showOnConsole)
				{
					System.out.print("--------------------------------------------------------------\n");
				}
			}
			pw.write("\n\n****************************************************************\n");
			if (showOnConsole)
			{
				System.out.print("\n\n****************************************************************\n");
				System.out.println("0. Skip remaining sentences. \n 1. Continue to next sentence.\n");
				Scanner in = new Scanner(System.in);
				int temp = in.nextInt();
				if (temp == 0)
				{
					showOnConsole = false;
				}
				in.close();
			}
		}
		pw.close();
	}
}