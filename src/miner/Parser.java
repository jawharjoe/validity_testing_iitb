package miner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import testValidity.ValidatePatterns;

public class Parser
{
	static int L, Th, G;
	static int FromLength, ToLength;
	static HashMap<Integer, Integer> Thresholds;

	static HashMap<String, Integer> tokenID;
	static ArrayList<String> tokenName;

	static ArrayList<Rule> rules;
	static ArrayList<ArrayList<Integer>> inverseMap;

	ArrayList<Sentences> sentences;
	HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>> globalPatterns;
	HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>> frequentGlobalPatterns;

	// Stores all the patterns, frequent or not
	static HashMap<String, ArrayList<ArrayList<NonTerminal>>> nonTerminalInstances;
	static ArrayList<NonTerminal> nonTerminals;

	public Parser()
	{
		tokenID = new HashMap<String, Integer>();
		tokenName = new ArrayList<String>();

		rules = new ArrayList<Rule>();
		inverseMap = new ArrayList<ArrayList<Integer>>();

		sentences = new ArrayList<Sentences>();
		globalPatterns = new HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>>();
		frequentGlobalPatterns = new HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>>();

		nonTerminalInstances = new HashMap<String, ArrayList<ArrayList<NonTerminal>>>();
		nonTerminals = new ArrayList<NonTerminal>();

		// Add a dummy non Terminal at the top to keep index of nonTerminal consistent with their number from SP
		// => start with 1 . Thus, $1 is at index 1, $2 is at index 2 and so on.
		nonTerminals.add(new NonTerminal(0, "dummy"));
	}

	/* Assigns a unique token id to each token */
	static int getTokenID(String str)
	{
		if (!tokenID.containsKey(str))
		{
			tokenID.put(str, tokenID.size());
			tokenName.add(str);
			inverseMap.add(new ArrayList<Integer>());
		}
		return tokenID.get(str);
	}

	void printHMSAI(ArrayList<ArrayList<Integer>> hmsai)
	{
		println("Printing the hashmap ================ ");
		for (int i = 0; i < hmsai.size(); ++i)
		{
			print(tokenName.get(i) + " :: ");
			for (Integer j : hmsai.get(i))
			{
				print(j + " ");
			}
			print("\n");
		}
		println("==================");
	}

	/*
	 * Transforms the rules to objects of class Rule. Adds these objects to the list "rules" In the process, also
	 * generates the tokenIDs for the tokens in these rules.
	 */
	public void addRule(String dictStr)
	{
		String dictArr[] = dictStr.split("\\s*[-]+>\\s*");
		for (String rule : dictArr[1].split("\\s*[|]\\s*"))
		{
			Rule newRule = new Rule(dictArr[0], rule);
			inverseMap.get(newRule.rhs[0]).add(rules.size());
			rules.add(newRule);
		}

		int id = Integer.parseInt(dictArr[0].substring(MinerConfiguration.NonTerminalToken.length()).trim());
		NonTerminal nt = new NonTerminal(id, dictArr[1]);
		nonTerminals.add(nt);
	}

	public void readInput(InputStream inStr) throws Exception
	{
		Scanner in = new Scanner(inStr);
		int num_sentences = in.nextInt();
		in.nextLine();

		for (int i = 0; i < num_sentences; i++)
			sentences.add(new Sentences((i + 1), in.nextLine()));

		int num_dict = in.nextInt();
		in.nextLine();
		for (int i = 0; i < num_dict; i++)
			addRule(in.nextLine());

		int num_gr = in.nextInt();
		in.nextLine();
		for (int i = 0; i < num_gr; i++)
			addRule(in.nextLine());

		// printHMSAI(inverseMap);
		in.close();
	}

	private void generateAllPatterns()
	{
		for (int i = 0; i < sentences.size(); i++)
		{
			System.out.println("Parsing Sentence : " + i);
			sentences.get(i).parseSymbols(this);
			sentences.get(i).clear();
		}
	}

	private void findFrequentPatterns()
	{
		for (int length : Thresholds.keySet())
		{
			HashMap<Pattern, ArrayList<Triple>> globalPatternsForCurentL = globalPatterns.get(length);
			for (Map.Entry<Pattern, ArrayList<Triple>> entry : globalPatternsForCurentL.entrySet())
			{
				if (entry.getValue().size() < Thresholds.get(length))
				{
					continue;
				}
				// fp_pw.write(entry.getKey() + " : ");
				// print(entry.getKey() + " : ");
				for (Triple t : entry.getValue())
				{
					addPattern(length, entry.getKey(), t, 2);
					// fp_pw.write(t + " ");
					// fpt_pw.write(entry.getKey() + " @");
					// fpt_pw.write(t + "\n");
					// print(t + " ");
				}
				// fp_pw.write("\n");
				// print("\n");
			}

			HashMap<Pattern, HashSet<Triple>> patternsWithX = new HashMap<Pattern, HashSet<Triple>>();
			for (int mask = 1; mask < (1 << length); mask++)
			{
				patternsWithX.clear();
				if (!validMask(mask, Parser.G))
					continue;
				// println("Valid "+mask);
				for (Map.Entry<Pattern, ArrayList<Triple>> entry : globalPatternsForCurentL.entrySet())
				{
					Pattern newLS = entry.getKey().generate(mask);
					// println(entry.getKey()+" =("+""+")=> "+newLS);
					if (!patternsWithX.containsKey(newLS))
						patternsWithX.put(newLS, new HashSet<Triple>());
					HashSet<Triple> hashSet = patternsWithX.get(newLS);
					for (Triple triple : entry.getValue())
					{
						hashSet.add(triple);
					}
				}
				for (Map.Entry<Pattern, HashSet<Triple>> entry : patternsWithX.entrySet())
				{
					if (entry.getValue().size() < Thresholds.get(length))
						continue;
					// fp_pw.write(entry.getKey() + " : ");
					print(entry.getKey() + " : ");
					for (Triple t : entry.getValue())
					{
						addPattern(length, entry.getKey(), t, 2);
						// fp_pw.write(t + " ");
						// fpt_pw.write(entry.getKey() + " @");
						// fpt_pw.write(t + "\n");
						// print(t + " ");
					}
					// fp_pw.write("\n");
					print("\n");
				}
			}
		}
	}

	private boolean validMask(int mask, int g)
	{
		int numCont = 0;
		while (mask > 0)
		{
			int bit = mask % 2;
			mask /= 2;
			if (bit == 1)
				numCont++;
			else
				numCont = 0;
			if (numCont > g)
				return false;
		}
		return true;
	}

	public void addPattern(int length, Pattern newPattern, Triple triple, int addTo)
	{
		/* addTo = 1 => add to globalPatterns ; addTo = 2 => add to frequentGlobalPatterns */
		if (addTo == 1)
		{
			if (!globalPatterns.containsKey(length))
			{
				HashMap<Pattern, ArrayList<Triple>> globalPatternsForCurentL = new HashMap<Pattern, ArrayList<Triple>>();
				globalPatterns.put(length, globalPatternsForCurentL);
			}
			{
				HashMap<Pattern, ArrayList<Triple>> globalPatternsForCurentL = globalPatterns.get(length);
				if (!globalPatternsForCurentL.containsKey(newPattern))
				{
					globalPatternsForCurentL.put(newPattern, new ArrayList<Triple>());
				}
				globalPatternsForCurentL.get(newPattern).add(triple);
			}
		} else
		{
			if (!frequentGlobalPatterns.containsKey(length))
			{
				HashMap<Pattern, ArrayList<Triple>> frequentGlobalPatternsForCurentL = new HashMap<Pattern, ArrayList<Triple>>();
				frequentGlobalPatterns.put(length, frequentGlobalPatternsForCurentL);
			}
			{
				HashMap<Pattern, ArrayList<Triple>> frequentGlobalPatternsForCurentL = frequentGlobalPatterns
						.get(length);
				if (!frequentGlobalPatternsForCurentL.containsKey(newPattern))
				{
					frequentGlobalPatternsForCurentL.put(newPattern, new ArrayList<Triple>());
				}
				frequentGlobalPatternsForCurentL.get(newPattern).add(triple);
			}
		}
	}

	public static void println(Object obj)
	{
		// System.out.println(obj);
	}

	public static void print(Object obj)
	{
		// System.out.print(obj);
	}

	public static void main(int fromLength, int toLength, HashMap<Integer, Integer> ThresholdMap, int Gap,
			String fileNameSuffix) throws Exception
	{
		long start = System.currentTimeMillis();
		FromLength = fromLength;
		ToLength = toLength;
		Thresholds = new HashMap<Integer, Integer>(ThresholdMap);
		G = Gap;

		Parser p = new Parser();
		getTokenID(MinerConfiguration.NonTerminalToken);

		p.readInput(new FileInputStream(MinerConfiguration.ContestInputFileName));
		p.generateAllPatterns();
		p.findFrequentPatterns();

		long end = System.currentTimeMillis();
		System.out.println("Time for pattern generation : " + (end - start) / 1000.0 + " seconds. ");

		ValidatePatterns validateObj = new ValidatePatterns(p.frequentGlobalPatterns, nonTerminalInstances, Thresholds,
				p.sentences.size(), fileNameSuffix);
		validateObj.validate();
	}

	/*
	 * public static void main(String args[]) throws Exception { long start = System.currentTimeMillis();
	 * getTokenID("_X_"); Parser p = new Parser();
	 * 
	 * Scanner in = new Scanner(System.in); L = in.nextInt(); Th = in.nextInt(); G = in.nextInt(); int ThForLengthOne =
	 * in.nextInt(); File fp = new File(Configuration.frequentPatternsFileName + L + "-" + Th); File fpt = new
	 * File(Configuration.patternTriplesFileName + L + "-" + Th); ValidatePatterns validateObj = new ValidatePatterns(L,
	 * Th, ThForLengthOne);
	 * 
	 * if (fp.exists() && fpt.exists()) { validateObj.validate(); } else { PrintWriter fp_pw = new PrintWriter(fp);
	 * PrintWriter fpt_pw = new PrintWriter(fpt); if (args.length != 0) p.readInput(new FileInputStream(args[0])); else
	 * p.readInput(new FileInputStream(Configuration.contestInputFileName)); p.generatePatterns();
	 * p.printGlobalPatterns(fp_pw, fpt_pw); long end = System.currentTimeMillis(); fp_pw.close(); fpt_pw.close();
	 * //System.out.println("Time for pattern generation : " + (end - start) / 1000.0 + " seconds. ");
	 * validateObj.validate(); } }
	 */
}