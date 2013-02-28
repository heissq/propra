package validation;
public class Q3 {
	
	private String str1 = "";
	private String str2 = "";
	
	private double result;
	
	private int counter;
	
	public Q3 (String aStr1, String aStr2) {
		str1 = aStr1;
		str2 = aStr2;
		counter = 0;
		result = -1;
		calcQ3();
	}	
	
	public boolean testSameLength () {
		if (str2.length()!=str1.length())
			return false;
		else 
			return true;
	}

	public void calcQ3 () {
		for (int i = 0; i<str1.length();i++) {
			if (str1.charAt(i) == str2.charAt(i)) {
				counter++;
			}
		}
		result = (double)counter/str1.length();
	}
	
	public double getResult() {
		return result;
	}
}