package miner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Sentences
{
	int id;
	int[] tokens;
	SymbolsAtLocation[] symbolsAtLocations;
	ListPattern[][] patterns;

	public Sentences(int id_, String sentence)
	{
		id = id_;
		String[] tokensArr = sentence.trim().split("\\s+");
		tokens = new int[tokensArr.length - 1];
		symbolsAtLocations = new SymbolsAtLocation[tokensArr.length - 1];
		patterns = new ListPattern[tokensArr.length - 1][];

		for (int i = 0; i < tokens.length; ++i)
		{
			tokens[i] = Parser.getTokenID(tokensArr[i + 1]);
			symbolsAtLocations[i] = new SymbolsAtLocation();
			patterns[i] = new ListPattern[Parser.ToLength + 1];
		}
	}

	public void parseSymbols(Parser p)
	{
		for (int index = tokens.length - 1; index >= 0; --index)
			parseSymbols(p, index);
	}

	public ArrayList<Integer> parseSymbols(Parser p, int index, Pattern ls)
	{
		// println(" HERHEHRE RHER EHRE HRE "+index + ls);
		if (ls.symbols.length == 0)
		{
			ArrayList<Integer> ret = new ArrayList<Integer>();
			ret.add(0);
			return ret;
		}

		if (index >= tokens.length)
			return null;

		/*
		 * if ls is contained in symbolsAtLocations, return a list of all possible lengths of ls
		 */
		if (symbolsAtLocations[index].allSymbols.containsKey(ls))
		{
			// println("Function call with "+ls+" returns "+symbolsAtLocations.get(index).allSymbols.get(ls));
			return symbolsAtLocations[index].allSymbols.get(ls);
		}
		/* Separate out the first word from the current rhs */
		Pattern firstSym = new Pattern(1);
		firstSym.symbols[0] = ls.symbols[0];

		Pattern remSym = new Pattern(ls.symbols.length - 1);
		for (int i = 0; i < remSym.symbols.length; i++)
			remSym.symbols[i] = ls.symbols[i + 1];

		if (!symbolsAtLocations[index].allSymbols.containsKey(firstSym))
		{
			// println(" NOT FOUND LOL !") ;
			return new ArrayList<Integer>();
		}
		// println(symbolsAtLocations.get(index).allSymbols.containsKey(firstSym));
		ArrayList<Integer> lengthsOfFirstSym = symbolsAtLocations[index].allSymbols.get(firstSym);
		for (Integer len : lengthsOfFirstSym)
		{
			// println("Calling Now " + index +" "+ len + " " + remSym);
			ArrayList<Integer> recAnswer = parseSymbols(p, index + len, remSym);
			if (recAnswer == null)
				continue;
			// println("Calling parseSymbols with1 "+remSym);
			// why this? just to maintain the length?
			for (int ansLen : recAnswer)
				symbolsAtLocations[index].addSymbol(new Symbol(ls, len + ansLen));

		}
		// println("Function call with "+ls+" returns "+symbolsAtLocations.get(index).allSymbols.get(ls));
		return symbolsAtLocations[index].allSymbols.get(ls);
	}

	public void parseSymbols(Parser p, int index)
	{
		// println("Doing the work at symbol " + index);
		ArrayList<Symbol> unexpandedSymbols = new ArrayList<Symbol>();
		Symbol seed = new Symbol(new Pattern(1), 1);
		seed.ls.symbols[0] = tokens[index];

		unexpandedSymbols.add(seed);
		symbolsAtLocations[index].addSymbol(seed);
		while (!unexpandedSymbols.isEmpty())
		{
			int lastIndex = unexpandedSymbols.size() - 1;
			Symbol toExpand = unexpandedSymbols.get(lastIndex);
			// println("Expanding "+toExpand);
			unexpandedSymbols.remove(lastIndex);
			if (Parser.inverseMap.get(toExpand.ls.symbols[0]).isEmpty())
				continue;

			ArrayList<Integer> listRules = Parser.inverseMap.get(toExpand.ls.get(0));
			int len = toExpand.len;
			for (int ruleId : listRules)
			{

				Rule newRule = Parser.rules.get(ruleId);
				// println("Working with Inverse rule "+newRule);
				Pattern newLs = new Pattern(newRule.rhs.length - 1);
				for (int i = 0; i < newLs.symbols.length; ++i)
					newLs.symbols[i] = newRule.rhs[i + 1];

				ArrayList<Integer> listInt = parseSymbols(p, index + len, newLs);
				/*
				 * Length of the parse will be returned to find the length of this NT
				 */
				if (listInt == null)
					continue;
				// if (listInt.size() > 1)
				// {
				// System.out.println("lolwa 3?");
				// Scanner in = new Scanner(System.in);
				// int x = in.nextInt();
				// }
				for (int length : listInt)
				{
					Symbol sym = new Symbol(new Pattern(1), length + len);
					sym.ls.symbols[0] = newRule.lhs;
					boolean isAdded = symbolsAtLocations[index].addSymbol(sym);
					if (isAdded)
						unexpandedSymbols.add(sym);
				}
			}
		}

		for (int i = 1; i <= Parser.ToLength; i++)
			patterns[index][i] = new ListPattern();

		for (int currLen = 1; currLen <= Parser.ToLength; currLen++)
		{
			ListPattern lp = new ListPattern();
			for (Map.Entry<Pattern, ArrayList<Integer>> entry : symbolsAtLocations[index].allSymbols.entrySet())
			{
				Pattern currSymbol = entry.getKey();
				for (Integer lenOfCurrSymbol : entry.getValue())
				{
					ListPattern retLP = getLP(index + lenOfCurrSymbol, currLen - 1);
					lp.mergePatternLPandAdd(currSymbol, lenOfCurrSymbol, retLP);
				}
			}
			patterns[index][currLen] = lp;
		}

		for (int length = Parser.FromLength; length <= Parser.ToLength; length++)
		{
			for (Map.Entry<Pattern, HashSet<Integer>> entry : patterns[index][length].patterns.entrySet())
			{
				Pattern newPattern = entry.getKey();
				// if(entry.getValue().size()>1)
				// {
				// System.out.println("lolwa 2?");
				// Scanner in = new Scanner(System.in);
				// int x = in.nextInt();
				// }
				for (Integer len : entry.getValue())
				{
					ArrayList<ArrayList<NonTerminal>> nonTerminalsList = null;
					String genericVersionOfThePattern = newPattern.getGenericVersion();
					if (Parser.nonTerminalInstances.containsKey(genericVersionOfThePattern))
					{
						nonTerminalsList = Parser.nonTerminalInstances.get(genericVersionOfThePattern);
					} else
					{
						nonTerminalsList = new ArrayList<ArrayList<NonTerminal>>();
					}
					ArrayList<NonTerminal> nonTerminals = newPattern.getNonTerminals(id, index);
					if (!nonTerminalsList.contains(nonTerminals))
					{
						nonTerminalsList.add(nonTerminals);
					}
					Parser.nonTerminalInstances.put(genericVersionOfThePattern, nonTerminalsList);

					/* Copying the pattern to add as a generic pattern for Pattern Mining */
					Pattern copyPattern = new Pattern(newPattern.symbols.length);
					for (int i = 0; i < newPattern.symbols.length; i++)
					{
						copyPattern.symbols[i] = newPattern.symbols[i];
					}

					copyPattern.makeGeneric();
					p.addPattern(length, copyPattern, new Triple(id, index, index + len), 1);
				}
			}
		}

		// for (ListPattern[] patternRow : patterns)
		// {
		// System.out.println(Arrays.toString(patternRow));
		// }
	}

	public ListPattern getLP(int index, int len)
	{
		if (len == 0)
		{
			ListPattern lp = new ListPattern();
			HashSet<Integer> newHS = new HashSet<Integer>();
			newHS.add(0);
			lp.patterns.put(new Pattern(0), newHS);
			return lp;
		}
		if (index == tokens.length)
			return new ListPattern();

		return patterns[index][len];
	}

	public static void println(Object obj)
	{
		System.out.println(obj);
	}

	public static void print(Object obj)
	{
		System.out.print(obj);
	}

	public void clear()
	{
		patterns = null;
		symbolsAtLocations = null;
		tokens = null;
	}
}