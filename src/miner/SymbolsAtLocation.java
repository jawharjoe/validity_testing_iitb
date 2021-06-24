package miner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolsAtLocation
{
	public HashMap<Pattern, ArrayList<Integer>> allSymbols = new HashMap<Pattern, ArrayList<Integer>>();
	
	/* Adds a Pattern as key to the allSymbols HashMap if it is not already present.
	 * Then it adds newSymbol.len to the List for the current Pattern. 
	 * If the symbol already existed with the current length, then returns false, else true.*/
	public boolean addSymbol(Symbol newSymbol)
	{
		Integer len = newSymbol.len;
		if (!allSymbols.containsKey(newSymbol.ls))
			allSymbols.put(newSymbol.ls, new ArrayList<Integer>());

		for (int existingLength : allSymbols.get(newSymbol.ls))
			if (existingLength == len)
				return false;

		allSymbols.get(newSymbol.ls).add(len);
		return true;
	}

	public String toString()
	{
		String ret = new String();
		for (Map.Entry<Pattern, ArrayList<Integer>> entry : allSymbols.entrySet())
		{
			ret += (entry.getKey() + " :: ");
			for (Integer i : entry.getValue())
			{
				ret += (i + " ");
			}
			ret += ("\n");
		}
		return ret;
	}

	public static void println(Object obj)
	{
		System.out.println(obj);
	}

	public static void print(Object obj)
	{
		System.out.print(obj);
	}

}
