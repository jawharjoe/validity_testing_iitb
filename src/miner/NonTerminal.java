/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package miner;

public class NonTerminal
{
	int id;
	String text;
	String translationText;
	Triple occurrenceTriple;

	public NonTerminal(int id, String text)
	{
		this.id = id;
		this.text = text;
		translationText = "";
		occurrenceTriple = new Triple(0, 0, 0);
	}

	public String toString()
	{
		return String.valueOf(id) + " --> " + text + " --> " + occurrenceTriple.toString();
	}
}