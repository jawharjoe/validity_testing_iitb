/*
 * @author  Vidhant (vidhant@gmail.com)
 */

package testValidity;

import miner.Triple;

public class ValidPatternInstance
{
	public ValidPattern vp;
	public String instanceText;
	Triple triple;

	public ValidPatternInstance(ValidPattern vp, String text, Triple t)
	{
		this.vp = vp;
		instanceText = text;
		triple = t;
	}

	public String toString()
	{
		return "Pattern : {" + vp.text + "} || " + "Instance : {" + instanceText + "} || " + "Index : {" + triple.start
				+ " to " + triple.end + "}";
	}
}