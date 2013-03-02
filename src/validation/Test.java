package validation;

public class Test {

	/**
	 * @param args
	 * test klasse zum debuggen etc.
	 */
	public static void main(String[] args) {
		Segment[][] foo = SOV.makesegments("CCCCCCCHHHHH", "CCCHHHHHHHHC");
		System.out.println(foo[0][0].c);
		String string1 = "AAAAACCCCCCAAAA";
		String string2 = "AAAAACCAACCAAAA";
		String stringarray[][] = new String[10][2];
		
		// TODO read in file for Q3 validation
	}
}
