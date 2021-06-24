/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package miner;

import java.util.HashMap;
import java.util.Scanner;

public class Driver
{
	static int G;
	static int From;
	static int To;
	static String fileNameSuffix;
	static HashMap<Integer, Integer> Thresholds;

	public static void main(String args[]) throws Exception
	{
		System.out.println("Enter in format : \"FromLength ToLength Gap\"");
		Scanner in = new Scanner(System.in);
		From = in.nextInt();
		To = in.nextInt();
		G = in.nextInt();

		System.out.println("Enter the Thresholds for the various lengths, starting from : " + From);

		Thresholds = new HashMap<Integer, Integer>();

		for (int i = From; i <= To; i++)
		{
			Thresholds.put(i, in.nextInt());
		}

		if (From == To)
		{
			fileNameSuffix = "L =  " + From + " ,Th = " + Thresholds.toString();
		} else
		{
			fileNameSuffix = " From : L = " + From + " To L =  " + To + " ,Th = " + Thresholds.toString();
		}

		Parser.main(From, To, Thresholds, G, fileNameSuffix);
		in.close();
	}
}