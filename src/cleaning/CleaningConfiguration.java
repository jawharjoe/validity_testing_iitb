/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package cleaning;

import _stanfordParser.StanfordParserConfiguration;

public class CleaningConfiguration
{
	public static String ProjectPath = "/home/vidhant/Documents/PMT/WorkspacePM/PatternMinerAndValidator/";
	
	static String dictionariesPath = ProjectPath + "data/cleaning/input/dictionaries/";
	static String inputFilePath = StanfordParserConfiguration.OutputFileName + " - "
			+ StanfordParserConfiguration.PhraseType;
	static String outputFilePath = ProjectPath + "data/cleaning/output/cleaned rules.txt";
}