package miner;

public class Rule
{
	int lhs;
	int[] rhs;

	/* Create the Rule object, having lhs = tokenID of the lhs token of the
	 * rule and rhs[] = tokenIDs of the tokens on the rhs of the rule */
	public Rule(String l_, String r_)
	{
		lhs = Parser.getTokenID(l_.trim());
		String[] rhstok = r_.trim().split("\\s+");
		rhs = new int[rhstok.length];
		for (int i = 0; i < rhs.length; ++i)
			rhs[i] = Parser.getTokenID(rhstok[i].trim());
	}
	
	/* Represents the rule object as a String. Example :
	 * If this.lhs = 12 and this.rhs = [13], where 13 is tokenID for the token "mn",
	 * Then String will be :
	 * 
	 * Lhs = 12
	 * Rhs mn */
	public String toString()
	{
		String ret = new String();
		ret += ("Lhs = " + lhs + "\nRhs ");
		for (int term : rhs)
		{
			ret += (Parser.tokenName.get(term) + " ");
		}
		return ret;
	}
}