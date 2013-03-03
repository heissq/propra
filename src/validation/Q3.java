package validation;
public class Q3 {

	private String rs = "";
	private String ps = "";

	public double q3result;
	public double qcresult;
	public double qeresult;
	public double qhresult;

	private int counter;
	private int counterc;
	private int countere;
	private int counterh;
	private int countercc;
	private int counteree;
	private int counterhh;

	//
	public Q3(String observed, String predicted) {
		rs = observed;
		ps = predicted;
		// counter = 0;
		// counterc = 0;
		// countercc = 0;
		// countere = 0;
		// counteree = 0;
		// counterh = 0;
		// counterhh = 0;
		q3result = -1;
		calcQ3();
	}	

	public boolean testSameLength() {
		if (ps.length() != rs.length())
			return false;
		else 
			return true;
	}

	public void calcQ3() {
		counter = 0;
		counterc = 0;
		countercc = 0;
		countere = 0;
		counteree = 0;
		counterh = 0;
		counterhh = 0;
		int counterhypen = 0;
		for (int i = 0; i < rs.length(); i++) {
			if (ps.charAt(i) == '-')
				counterhypen++;
			if (rs.charAt(i) == ps.charAt(i)) {
				counter++;
				switch (rs.charAt(i)) {
				case 'C':
					countercc++;
					break;
				case 'E':
					counteree++;
					break;
				case 'H':
					counterhh++;
					break;
				default:
					break;
				}
			}
			// Qe
			if (ps.charAt(i) != '-') {
				switch (rs.charAt(i)) {
				case 'C':
					counterc++;
					break;
				case 'E':
					countere++;
					break;
				case 'H':
					counterh++;
					break;
				default:
					break;
				}
			}

		}
		q3result = 100*((double) counter / (rs.length() - counterhypen));
		qcresult = 100 * ((double) countercc / counterc);
		qhresult = 100*((double) counterhh / counterh);
		qeresult = 100 * ((double) counteree / countere);
	}

	// TODO einspeichern der werte!!!

	public void print() {
		System.out.println("q3=" + q3result);
		System.out.println("qh=" + qhresult);
		System.out.println("qe=" + qeresult);
		System.out.println("qc=" + qcresult);
	}
}