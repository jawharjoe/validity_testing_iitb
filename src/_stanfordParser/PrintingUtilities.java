/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;

public class PrintingUtilities
{

	public static long PrintRules(String lhs, List<List<Tree>> rules) throws FileNotFoundException
	{
		List<String> printedRules = new ArrayList<String>();
		File outputFile = new File(StanfordParserConfiguration.OutputFileName + " - " + StanfordParserConfiguration.PhraseType);
		PrintWriter pw = null;

		// if (outputFile.exists())
		// {
		// pw = new PrintWriter(new FileOutputStream(new
		// File(Configuration.OutputFileName + " - "
		// + Configuration.PhraseType), true));
		// } else
		// {
		pw = new PrintWriter(StanfordParserConfiguration.OutputFileName + " - " + StanfordParserConfiguration.PhraseType);
		// }
		long numberOfRules = 0;
		for (List<Tree> rhs : rules)
		{
			for (Tree subtree : rhs)
			{
				String rhsString = "";
				for (Tree leaf : subtree.getLeaves())
				{
					rhsString += leaf + " ";
				}
				if (!printedRules.contains(rhsString))
				{
					pw.write(lhs + " -> ");
					pw.write(rhsString + "\n");
					numberOfRules++;
					printedRules.add(rhsString);
				}
			}
		}
		pw.close();
		System.out.println("Wrote to the file : " + outputFile.getAbsolutePath());
		return numberOfRules;
	}
}