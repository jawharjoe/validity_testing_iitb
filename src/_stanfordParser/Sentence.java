/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.io.Serializable;

import edu.stanford.nlp.trees.Tree;

public class Sentence implements Serializable
{
	private static final long serialVersionUID = 8958376098478516421L;
	static int sentenceCount = 0;
	public int id;
	public String text;
	public String tokenizedText;
	public Tree parseTree;

	public Sentence()
	{
		id = 0;
		text = "";
		parseTree = null;
		tokenizedText = "";
	}

	public Sentence(String text)
	{
		this.id = ++sentenceCount;
		this.text = text;
		this.tokenizedText = "";
		parseTree = null;
	}
}