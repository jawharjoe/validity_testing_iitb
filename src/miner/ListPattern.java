package miner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ListPattern
{
	HashMap<Pattern, HashSet<Integer>> patterns = new HashMap<Pattern, HashSet<Integer>>();

	public int getSize()
	{
		return patterns.size();
	}

	public void mergePatternLPandAdd(Pattern currSymbol, int lenOfCurrSymbol, ListPattern retLP)
	{
		for (Map.Entry<Pattern, HashSet<Integer>> entry : retLP.patterns.entrySet())
		{
			/* Add the new Pattern to the current symbol */
			Pattern newPattern = currSymbol.addLS(entry.getKey());
			HashSet<Integer> newLengths = new HashSet<Integer>();
			/* Add the lengths to the HashSet for the new pattern */
			// if(entry.getValue().size()>1)
			// {
			// System.out.println("lolwa?");
			// Scanner in = new Scanner(System.in);
			// int x = in.nextInt();
			// }
			for (Integer retLens : entry.getValue())
				newLengths.add(retLens + lenOfCurrSymbol);

			addPattern(newPattern, newLengths);
		}
	}

	private void addPattern(Pattern newPattern, HashSet<Integer> newLengths)
	{
		if (!patterns.containsKey(newPattern))
		{
			patterns.put(newPattern, new HashSet<Integer>());
		}
		HashSet<Integer> relevantHS = patterns.get(newPattern);
		for (Integer len : newLengths)
		{
			relevantHS.add(len);
		}
	}

	public String toString()
	{
		String ret = new String();
		for (Map.Entry<Pattern, HashSet<Integer>> entry : patterns.entrySet())
		{
			ret += (entry.getKey() + " :::: " + toString(entry.getValue()) + " ");
		}
		return ret;
	}

	private String toString(HashSet<Integer> value)
	{
		String ret = new String();
		for (Integer val : value)
		{
			ret += (val + " ");
		}
		return ret;
	}
}
