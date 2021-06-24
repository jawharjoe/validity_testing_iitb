/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable
{
	private static final long serialVersionUID = -8362626053122851900L;
	static int documentCount = 0;
	int id;
	public List<String> rules;
	public List<Sentence> sentences;
	String documentPath;
	String serializedDocumentPath;

	public Document(String documentPath, String serializedDocumentPath)
	{
		id = ++documentCount;
		this.documentPath = documentPath;
		rules = new ArrayList<String>();
		this.serializedDocumentPath = serializedDocumentPath;
		sentences = new ArrayList<Sentence>();
	}

	public void synthesizeDocument(boolean deserialize) throws IOException
	{
		if (!deserialize)
		{
			readDocument();
			generateParsesForSentences();
			boolean success = SerializationAndDeserializationUtilities.Serialize(this, serializedDocumentPath);
			System.out.println("Serialization was : " + (success ? "successful." : "unsuccessful"));
		} else
		{
			SerializationAndDeserializationUtilities.DeSerialize(this, serializedDocumentPath);
			System.out.println("Successfully deserialized " + this.sentences.size() + " sentences.");
		}
	}

	private void readDocument() throws IOException
	{
		FileReader fr = new FileReader(documentPath);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);

		// Add dummy sentence at top to start indexing of "sentences" from 1.
		Sentence dummy = new Sentence();
		sentences.add(dummy);

		String line = "";
		while ((line = br.readLine()) != null)
		{
			line = line.substring(line.indexOf(" ") + 1);
			Sentence newSentence = new Sentence(line);
			sentences.add(newSentence);
		}
	}

	private void generateParsesForSentences() throws FileNotFoundException
	{
		Parser parser = new Parser();
		for (Sentence currentSentence : sentences)
		{
			System.out.println("Parsing sentence : " + currentSentence.id);
			currentSentence.parseTree = parser.getParseTree(currentSentence.text);
		}
	}
}