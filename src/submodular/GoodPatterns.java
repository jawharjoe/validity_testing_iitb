package submodular;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GoodPatterns
{
	public native String[] getGoodPatterns(String minerInputFile, String corpusFile, String filteredPatternsFile,
			double fraction, int debugverbosity, int devverbosity);

	public static void submodular(String fileNameSuffix) throws FileNotFoundException
	{
		System.out.println("Running submodular.");
		System.load(SubmodularConfiguration.SfoFilePath);
		GoodPatterns gp = new GoodPatterns();
		String[] results = gp.getGoodPatterns(SubmodularConfiguration.ContestInputFileName,
				SubmodularConfiguration.WordCountsFileName, SubmodularConfiguration.FrequentPattersFilteredFileName
						+ fileNameSuffix, SubmodularConfiguration.Fraction, SubmodularConfiguration.DebugVerbosity,
				SubmodularConfiguration.DevelopmentVerbosity);

		PrintWriter pw = new PrintWriter(SubmodularConfiguration.SubmodularPatternsFileName + fileNameSuffix);
		System.out.println("Total patterns from submodular : " + results.length);
		for (int i = 0; i < results.length; i++)
		{
			pw.println(results[i]);
		}

		pw.close();
	}
}