package miner;

public class Symbol {
	public Pattern ls;
	public int len;

	public Symbol(Pattern s_, int len_) {
		ls = s_;
		len = len_;
	}

	public String toString() {
		String ret = new String("Symbol : ");
		ret += ls;
		ret += (" ::: Len : " + len);
		return ret;
	}
}
