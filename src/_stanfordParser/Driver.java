/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.util.ArrayList;
import java.util.List;

import cleaning.GenerateInput;
import cleaning.RemoveFW;
import edu.stanford.nlp.trees.Tree;

public class Driver
{
	public static void main(String args[]) throws Exception
	{
		long start = System.currentTimeMillis();
		Document document = new Document(StanfordParserConfiguration.InputFileName,
				StanfordParserConfiguration.SerializationFileName);
		/* false : serialize, true : deserialize */
		document.synthesizeDocument(StanfordParserConfiguration.DeserializeFromFile);

		/* For writing the rules to the file */
		Parser p = new Parser();
		List<List<Tree>> rhsLists = new ArrayList<List<Tree>>();

		for (Sentence currentSentence : document.sentences)
		{
			List<Tree> rhs = p.getPhrases(StanfordParserConfiguration.PhraseType, currentSentence.parseTree);
			rhsLists.add(rhs);
		}

		PrintingUtilities.PrintRules(StanfordParserConfiguration.PhraseType, rhsLists);

		long end = System.currentTimeMillis();
		System.out.println("Generation of Production Rules done in " + (end - start) / 1000.0 + " seconds.");

		document.rules = RemoveFW.getCleanedNonTerminalRules(); // Remove the Functional Words from the beginning
																// and start of the rules.
		GenerateInput.generateInputForMiner(document);
		String arr[] = new String[1];
		miner.Driver.main(arr);
	}
}