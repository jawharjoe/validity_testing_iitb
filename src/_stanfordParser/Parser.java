/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

class Parser
{
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"; //$NON-NLS-1$
	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
			new CoreLabelTokenFactory(), "invertible=true"); //$NON-NLS-1$
	private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);

	public Tree getParseTree(String str)
	{
		List<CoreLabel> tokens = tokenize(str);
		Tree tree = parser.apply(tokens);
		return tree;
	}

	// public boolean getParseTree1(String str, PrintWriter pw, int count)
	// {
	// List<CoreLabel> tokens = tokenize(str);
	// int length = tokens.size();
	//
	// String[] arr = str.split(" ");
	//
	// int strLength = arr.length;
	//
	// if (length != strLength)
	// {
	// if (length > strLength)
	// {
	// System.out.println("Increased from : " + strLength + " to : " + length);
	// System.out.println(str);
	// System.out.println(tokens.toString());
	// }
	// if (length < strLength)
	// {
	// System.out.println("Decreased from : " + strLength + " to : " + length);
	// System.out.println(str);
	// System.out.println(tokens.toString());
	// }
	// return false;
	// }
	// else
	// {
	// pw.write(count + "- ");
	// pw.write(str);
	// pw.write("\n");
	// return true;
	// }
	// // Tree tree = null;//parser.apply(tokens);
	// // return tree;
	// }

	public List<CoreLabel> tokenize(String str)
	{
		Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));
		return tokenizer.tokenize();
	}

	public List<Tree> getPhrases(String phraseType, Tree tree)
	{
		List<Tree> phraseList = new ArrayList<Tree>();
		for (Tree subtree : tree)
		{
			if (subtree.label().value().equalsIgnoreCase(phraseType))
			{
				phraseList.add(subtree);
			}
		}
		return phraseList;
	}
}