package miner;

import java.util.ArrayList;
import java.util.Arrays;

public class Pattern
{
	int[] symbols;

	public Pattern(int length)
	{
		symbols = new int[length];
	}

	public boolean equals(Object obj)
	{
		return Arrays.equals(((Pattern) obj).symbols, symbols);
	}

	public int hashCode()
	{
		return Arrays.hashCode(symbols);
	}

	public Pattern addLS(Pattern ls)
	{
		Pattern newLS = new Pattern(symbols.length + ls.symbols.length);
		for (int i = 0; i < symbols.length; ++i)
			newLS.symbols[i] = symbols[i];
		for (int i = 0; i < ls.symbols.length; ++i)
			newLS.symbols[i + symbols.length] = ls.symbols[i];
		return newLS;
	}

	public int get(int index)
	{
		return symbols[index];
	}

	public String toString()
	{
		String ret = new String("");
		for (int s : symbols)
			ret += Parser.tokenName.get(s) + " ";

		return ret.equals("") ? ret : ret.substring(0, ret.length() - 1);
	}

	public void makeGeneric()
	{
		for (int s = 0; s < symbols.length; s++)
		{
			if (Parser.tokenName.get(symbols[s]).contains(MinerConfiguration.NonTerminalToken))
			{
				symbols[s] = 0;
			}
		}
	}

	public String getGenericVersion()
	{
		String ret = new String("");
		for (int s = 0; s < symbols.length; s++)
		{
			if (Parser.tokenName.get(symbols[s]).contains(MinerConfiguration.NonTerminalToken))
			{
				/* Strip the non-terminal number from the non-terminal. Example : $1 -> $ , $248 -> $ and so on */
				ret += MinerConfiguration.NonTerminalToken + " ";
			} else
			{
				ret += Parser.tokenName.get(symbols[s]) + " ";
			}
		}
		return ret.trim();
	}

	public ArrayList<NonTerminal> getNonTerminals(int sentenceId, int index)
	{
		ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();
		int terminalsCount = index;
		for (int s = 0; s < symbols.length; s++)
		{
			String symbol = Parser.tokenName.get(symbols[s]);
			if (symbol.contains(MinerConfiguration.NonTerminalToken))
			{
				int indexOfNonTerminal = symbol.indexOf(MinerConfiguration.NonTerminalToken);
				int nonTerminalLength = MinerConfiguration.NonTerminalToken.length();
				String nonTerminal = symbol.substring(indexOfNonTerminal + nonTerminalLength);
				int nonTerminalNumber = Integer.parseInt(nonTerminal);
				NonTerminal currentNonTerminalDummy = Parser.nonTerminals.get(nonTerminalNumber);
				NonTerminal currentNonTerminal = new NonTerminal(currentNonTerminalDummy.id,
						currentNonTerminalDummy.text);
				int wordsInNonTerminal = currentNonTerminal.text.split("\\s+").length;
				currentNonTerminal.occurrenceTriple = new Triple(sentenceId, terminalsCount, terminalsCount
						+ wordsInNonTerminal);
				nonTerminals.add(currentNonTerminal);
				terminalsCount += wordsInNonTerminal;
			} else
			{
				terminalsCount++;
			}
		}
		return nonTerminals;
	}

	/* Generates a pattern for the given mask */
	public Pattern generate(int mask)
	{
		Pattern ret = new Pattern(symbols.length);
		for (int i = 0; i < symbols.length; i++)
			ret.symbols[i] = (mask & (1 << i)) == 0 ? symbols[i] : 0;
		return ret;
	}
}