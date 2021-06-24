/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package cleaning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import _stanfordParser.StanfordParserConfiguration;

public class RemoveFW
{
	static HashMap<String, Integer> toPrint = new HashMap<String, Integer>();

	@SuppressWarnings("unused")
	public static List<String> getCleanedNonTerminalRules() throws IOException
	{
		List<String> rules = new ArrayList<String>();
		PrintWriter pw = new PrintWriter(CleaningConfiguration.outputFilePath);
		ArrayList<String> dt = loadDictionaryFromFile(CleaningConfiguration.dictionariesPath + "articles.txt");
		ArrayList<String> cc = loadDictionaryFromFile(CleaningConfiguration.dictionariesPath + "conjunctions.txt");
		ArrayList<String> prep = loadDictionaryFromFile(CleaningConfiguration.dictionariesPath + "prepositions.txt");
		long countRemovedDT = removeFunctionalWords(dt, true, pw);
		long countRemovedCC = removeFunctionalWords(cc, false, pw);
		long countRemovedPrepositions = removeFunctionalWords(prep, false, pw);
		// System.out.println(dt.size() + " " + cc.size() + " " + prep.size());
		// System.out.println("Removed dt : " + countRemovedDT);
		// System.out.println("Removed cc : " + countRemovedCC);
		// System.out.println("Removed prepositions : " + countRemovedPrepositions);
		int nonTerminalNumber = 0;
		for (String s : toPrint.keySet())
		{
			if (toPrint.get(s) == 3)
			{
				String rule = StanfordParserConfiguration.NonTerminalToken + (++nonTerminalNumber) + " -> " + s;
				pw.write(rule + "\n");
				rules.add(rule);
			}
		}
		pw.close();

		return rules;
	}

	public static ArrayList<String> loadDictionaryFromFile(String fileName) throws IOException
	{
		ArrayList<String> wordsList = new ArrayList<String>();
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null)
		{
			wordsList.add(line.toLowerCase());
		}
		br.close();
		fr.close();
		return wordsList;
	}

	public static long removeFunctionalWords(ArrayList<String> wordsList, boolean removeOnlyIfLengthOne, PrintWriter pw)
			throws IOException
	{
		String fileName = CleaningConfiguration.inputFilePath;
		long removed = 0;
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null)
		{
			line = line.substring((StanfordParserConfiguration.PhraseType + " -> ").length());
			String lineCopy = line;
			line = line.toLowerCase();
			boolean print = true;
			String words[] = line.split(" ");

			if (wordsList.contains(words[0]))
			{
				if (removeOnlyIfLengthOne)
				{
					if (words.length == 1)
					{
						print = false;
					}
				} else
				{
					print = false;
				}
			}
			if (wordsList.contains(words[words.length - 1]))
			{
				if (!removeOnlyIfLengthOne)
					print = false;
			}
			if (!print)
			{
				// System.out.println("Removed : " + line);
				removed++;
			} else
			{
				lineCopy = lineCopy.replace("-LRB-", "(");
				lineCopy = lineCopy.replace("-RRB-", ")");
				String key = lineCopy;
				if (toPrint.containsKey(key))
				{
					int val = toPrint.get(key);
					toPrint.put(key, val + 1);
				} else
				{
					toPrint.put(key, 1);
				}
			}

		}
		br.close();
		fr.close();
		return removed;
	}
}