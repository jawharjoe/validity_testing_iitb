/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package _stanfordParser;

public class StanfordParserConfiguration
{
	public static String Domain = "Legal";
	public static String NumberOfSentences = "1582";
	public static String PhraseType = "NP";
	public static boolean DeserializeFromFile = true;
	public static String NonTerminalToken = "$"; /* Use a unique token which is not present in Corpus */
	/* false : serialize, true : deserialize */

	public static String ProjectPath = "/home/vidhant/Documents/PMT/WorkspacePM/PatternMinerAndValidator/";
	public static String InputFileName = ProjectPath + "data/stanford parser/input/Input" + NumberOfSentences + Domain;
	public static String OutputFileName = ProjectPath + "data/stanford parser/output/rules/rules";
	public static String SerializationFileName = ProjectPath + "data/stanford parser/output/serialized parses/Parser"
			+ NumberOfSentences + Domain + ".ser";
}