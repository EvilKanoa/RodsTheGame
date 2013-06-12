package ca.kanoa.rodsthegame.store;

public class BuyableFormatException extends Exception {

	private static final long serialVersionUID = -4656798629638829250L;

	public BuyableFormatException(String str) {
		super("Error at lines: " + str);
	}
	
}
