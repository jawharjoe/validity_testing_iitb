package miner;

import java.util.ArrayList;


public class NT {
	static public enum Type {
		Dict, GR
	};

	public NT(Type type_) {
		type = type_;
	}

	Type type;
	ArrayList<String> rules = new ArrayList<String>();

	public void addRule(String s) {
		rules.add(s);
	}

	public void buildMatrix() {

	}
}
