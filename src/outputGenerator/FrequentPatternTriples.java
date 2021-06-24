/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import miner.MinerConfiguration;
import miner.Pattern;
import miner.Triple;

public class FrequentPatternTriples
{
	public static void write(HashMap<Integer, HashMap<Pattern, ArrayList<Triple>>> frequentGlobalPatterns,
			String fileNameSuffix) throws FileNotFoundException
	{
		System.out.println("Writing FrequentPatterns and PatternTriples prior to validity checking.");
		File fp = new File(MinerConfiguration.FrequentPatternsFileName + fileNameSuffix);
		File fpt = new File(MinerConfiguration.PatternTriplesFileName + fileNameSuffix);
		PrintWriter fp_pw = new PrintWriter(fp);
		PrintWriter fpt_pw = new PrintWriter(fpt);
		for (int length : frequentGlobalPatterns.keySet())
		{
			HashMap<miner.Pattern, ArrayList<Triple>> globalPatternsForCurentL = frequentGlobalPatterns.get(length);
			for (Map.Entry<Pattern, ArrayList<Triple>> entry : globalPatternsForCurentL.entrySet())
			{
				fp_pw.write(entry.getKey() + " : ");
				for (Triple t : entry.getValue())
				{
					fp_pw.write(t + " ");
					fpt_pw.write(entry.getKey() + " @");
					fpt_pw.write(t + "\n");
				}
				fp_pw.write("\n");
			}
		}
		fp_pw.close();
		fpt_pw.close();
	}
}