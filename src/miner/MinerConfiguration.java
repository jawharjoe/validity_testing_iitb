/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package miner;

import _stanfordParser.StanfordParserConfiguration;

public class MinerConfiguration
{
	static String Domain = StanfordParserConfiguration.Domain;
	static String TotalSentences = StanfordParserConfiguration.NumberOfSentences;
	public static String OtherSuffix = "";

	/*
	 * Input file name must be of the format 'InputTotalSentencesDomainOtherSuffix'. Examples : Input1582Legal,
	 * Input1144BiologyWithNP, Input1582LegalWithVP, etc.
	 */

	public static String NonTerminalToken = StanfordParserConfiguration.NonTerminalToken; /*
																						 * Use a unique token which is
																						 * not present in Corpus
																						 */
	static String ProjectPath = "/home/vidhant/Documents/PMT/WorkspacePM/PatternMinerAndValidator/";
	public static String ContestInputFileName = ProjectPath + "data/validator_and_miner/input/Input" + TotalSentences
			+ Domain + OtherSuffix;
	public static String StatsFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/statistics/stats" + OtherSuffix;
	public static String CoverageStatsFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/coverage statistics/coverageStats" + OtherSuffix + "-";
	public static String PatternTriplesFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/frequent pattern triples/FPT" + OtherSuffix + "-";
	public static String FrequentPatternsFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/frequent patterns/FP" + OtherSuffix + "-";
	public static String FrequentPatternsFilteredFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/frequent_patterns_filtered/FP_Filtered" + OtherSuffix + "-";
	public static String GoodInstancesFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/good instances/GoodInstances" + OtherSuffix + "-";
	public static String NotCoveredSentencesFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/coverage statistics/SentenceWiseCoverage" + OtherSuffix + "-";
	public static String AllPPSFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/all patterns per sentence/all_pps" + OtherSuffix + "-";
	public static String SubmodularPPSFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/submodular patterns per sentence/submod_pps" + OtherSuffix + "-";
	public static String NonTerminalsPerPatternFileName = ProjectPath + "data/validator_and_miner/output/" + Domain
			+ "/non terminals per pattern/ntpp" + OtherSuffix + "-";
}