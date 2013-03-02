package validation;
public class Q3 {
	
	private static String rs = "";
	private static String ps = "";
	
	public static double q3result;
	public static double qcresult;
	public static double qeresult;
	public static double qhresult;
	
	public static int counter;
	public static int counterc;
	public static int countere;
	public static int counterh;
	public static int countercc;
	public static int counteree;
	public static int counterhh;
	
	//
	public Q3(String observed, String predicted) {
		rs = observed;
		ps = predicted;
		counter = 0;
		counterc = 0;
		countercc = 0;
		countere = 0;
		counteree = 0;
		counterh = 0;
		counterhh = 0;
		q3result = -1;
		calcQ3();
	}	
	
	public static boolean testSameLength() {
		if (ps.length() != rs.length())
			return false;
		else 
			return true;
	}

	public static void calcQ3() {
		for (int i = 0; i < rs.length(); i++) {
			if (rs.charAt(i) == ps.charAt(i)) {
				counter++;
				switch (rs.charAt(i)) {
				case 'C':
					countercc++;
				case 'E':
					counteree++;
				case 'H':
					counterhh++;
				}
			}
			// Qe
			switch (rs.charAt(i)) {
			case 'C':
				counterc++;
			case 'E':
				countere++;
			case 'H':
				counterh++;
			}
		}
		q3result = (double) counter / rs.length();
		qcresult = (double) countercc / counterc;
		qhresult = (double) counterhh / counterh;
		qeresult = (double) counteree / countere;
	}

	public static void print() {
		System.out.println("q3=" + q3result);
		System.out.println("qh=" + qhresult);
		System.out.println("qe=" + qeresult);
		System.out.println("qc=" + qcresult);
	}
}