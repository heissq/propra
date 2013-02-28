package validation;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Segment[][] foo = SOV.makesegments("CCCCCCCHHHHH", "CCCHHHHHHHHCC");
		System.out.println(foo[0][0].c);
	}
}
