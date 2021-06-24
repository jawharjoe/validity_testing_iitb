/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package outputGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import miner.MinerConfiguration;
import testValidity.ValidatePatterns;

public class Statistics
{
	public static void write(ValidatePatterns vpObject) throws IOException
	{
		System.out.println("Writing overall statistics.");
		PrintWriter out = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		fw = new FileWriter(MinerConfiguration.StatsFileName, true);
		bw = new BufferedWriter(fw);
		out = new PrintWriter(bw);

		out.print("Setting : " + vpObject.fileNameSuffix + "\n");
		out.print("Total Sentences : " + vpObject.totalSentences + "\n");
		out.print("Frequent Patterns from Pattern Miner : " + "------" + "\n");
		out.print("Valid Frequent Patterns (post validity checking) : " + vpObject.totalValidPatterns + "\n");
		out.print("Frequent Instances from Pattern Miner : " + vpObject.totalInstances + "\n");
		out.print("Valid Frequent Instances (post validity checking) : " + vpObject.totalValidInstances);
		out.format("(%.2f%%)\n", (vpObject.totalValidInstances * 100.0) / vpObject.totalInstances);
		out.format("Coverage : %.2f%%\n", vpObject.coverage);
		out.println("***********************************************************");

		out.close();
		bw.close();
		fw.close();
	}
}