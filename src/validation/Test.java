package validation;
import gorpack.*;

public class Test {

	/**
	 * @param args
	 * test klasse zum debuggen etc.
	 */
	public static void main(String[] args) {
		Segment[][] foo = SOV.makesegments("CCCCCCCHHHHH", "CCCHHHHHHHHC");
		System.out.println(foo[0][0].c);
		System.out.println("blaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		String string1 = "AAAAACCCCCCAAAA";
		String string2 = "AAAAACCAACCAAAA";
		String stringarray[][] = new String[10][2];
		
		
		
		Q3 testq3 = new Q3(string1, string2);
		System.out.println(testq3.getResult());
		// TODO read in file for Q3 validation
	}
}
