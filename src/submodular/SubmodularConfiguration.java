/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package submodular;

import miner.MinerConfiguration;
import _stanfordParser.StanfordParserConfiguration;

public class SubmodularConfiguration
{
	/*
	 * DevelopmentVerbosity is an Integer which will be used as a bit[8] where each bit from lsb to msb : corpusQuality
	 * InputPatternQuality OutputPatternQuality DisplayPatternText DisplayPatternPerSentence
	 */
	public static float Fraction = (float) 0.4;
	public static int DebugVerbosity = 0;
	public static int DevelopmentVerbosity = 16;
	static String OtherSuffix = MinerConfiguration.OtherSuffix;
	public static String ProjectPath = StanfordParserConfiguration.ProjectPath;
	public static String SfoFilePath = ProjectPath + "dependencies/sfoGoodPatterns.so";
	public static String ContestInputFileName = MinerConfiguration.ContestInputFileName;
	public static String WordCountsFileName = "data/submodular/input/corpusFile.txt";
	public static String FrequentPattersFilteredFileName = MinerConfiguration.FrequentPatternsFilteredFileName;
	public static String SubmodularPatternsFileName = ProjectPath + "data/submodular/output/submodular_patterns"
			+ OtherSuffix + "-";
}
