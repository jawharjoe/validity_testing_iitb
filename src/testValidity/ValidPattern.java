/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package testValidity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import miner.MinerConfiguration;
import miner.NonTerminal;
import miner.Triple;
import submodular.SubmodularConfiguration;
import _stanfordParser.Document;

public class ValidPattern
{
	public String text;
	public String translation;
	public List<Triple> occurrences;
	public int totalNonTerminals;
	public HashMap<String, List<Triple>> uniqueInstances;
	public static TreeMap<Integer, List<ValidPatternInstance>> allPPS = new TreeMap<Integer, List<ValidPatternInstance>>();
	public static TreeMap<Integer, List<ValidPatternInstance>> submodularPPS = new TreeMap<Integer, List<ValidPatternInstance>>();
	public ArrayList<ArrayList<NonTerminal>> nonTerminals;

	public ValidPattern(String text)
	{
		this.text = text;
		translation = "";
		totalNonTerminals = countNonTerminals();
		occurrences = new ArrayList<Triple>();
		uniqueInstances = new HashMap<String, List<Triple>>();
		allPPS = new TreeMap<Integer, List<ValidPatternInstance>>();
		submodularPPS = new TreeMap<Integer, List<ValidPatternInstance>>();
		nonTerminals = new ArrayList<ArrayList<NonTerminal>>();
	}

	public void buildUniqueOccurrences(Document document)
	{
		for (Triple triple : occurrences)
		{
			String instance = "";
			String words[] = (document.sentences.get(triple.sentenceId).text).trim().split("\\s+");
			for (int word = triple.start; word <= triple.end; word++)
				instance += (words[word] + " ");

			if (uniqueInstances.containsKey(instance))
			{
				uniqueInstances.get(instance).add(triple);
			} else
			{
				List<Triple> instanceList = new ArrayList<Triple>();
				instanceList.add(triple);
				uniqueInstances.put(instance, instanceList);
			}
		}
	}

	public void generatePatternsPerSentence(String fileNameSuffix) throws IOException
	{
		/* Inefficient right now, improve */
		FileReader fr = new FileReader(SubmodularConfiguration.SubmodularPatternsFileName + fileNameSuffix);
		BufferedReader br = new BufferedReader(fr);

		boolean foundInSubmodular = false;

		// Check if the pattern is present in the sub modular output
		String line = "";
		while ((line = br.readLine()) != null)
		{
			if (line.trim().equalsIgnoreCase(text.trim()))
			{
				foundInSubmodular = true;
				break;
			}
		}

		for (String instance : uniqueInstances.keySet())
		{
			List<Triple> instanceList = uniqueInstances.get(instance);
			for (Triple triple : instanceList)
			{
				if (allPPS.containsKey(triple.sentenceId))
				{
					List<ValidPatternInstance> patterns = allPPS.get(triple.sentenceId);
					ValidPatternInstance vpi = new ValidPatternInstance(this, instance.trim(), triple);
					patterns.add(vpi);
					allPPS.put(triple.sentenceId, patterns);
				} else
				{
					List<ValidPatternInstance> patterns = new ArrayList<ValidPatternInstance>();
					ValidPatternInstance vpi = new ValidPatternInstance(this, instance.trim(), triple);
					patterns.add(vpi);
					allPPS.put(triple.sentenceId, patterns);
				}
				if (foundInSubmodular)
				{
					if (submodularPPS.containsKey(triple.sentenceId))
					{
						List<ValidPatternInstance> patterns = submodularPPS.get(triple.sentenceId);
						ValidPatternInstance vpi = new ValidPatternInstance(this, instance.trim(), triple);
						patterns.add(vpi);
						submodularPPS.put(triple.sentenceId, patterns);
					} else
					{
						List<ValidPatternInstance> patterns = new ArrayList<ValidPatternInstance>();
						ValidPatternInstance vpi = new ValidPatternInstance(this, instance.trim(), triple);
						patterns.add(vpi);
						submodularPPS.put(triple.sentenceId, patterns);
					}
				}
			}
		}
		br.close();
		fr.close();
	}

	int countNonTerminals()
	{
		String copy = text;
		int numberOfNonTerminals = 0;
		int index = copy.indexOf(MinerConfiguration.NonTerminalToken);

		while (index != -1)
		{
			numberOfNonTerminals++;
			copy = copy.substring(index + 1);
			index = copy.indexOf(MinerConfiguration.NonTerminalToken);
		}

		return numberOfNonTerminals;
	}
}