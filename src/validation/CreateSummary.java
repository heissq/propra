package validation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import gorpack.*;

public class CreateSummary {
	private String id = "";
	// parameter to validate:
	private double q3 = -1;
	private double sov = -1;
	private double qh = -1;
	private double qe = -1;
	private double qc = -1;
	private double sov_h = -1;
	private double sov_e = -1;
	private double sov_c = -1;
	
	// strings a und b für aa-seq
	private String a = "";
	private String b = "";
	private String ps = "";

	public CreateSummary(String id, String a, String b, String ps, double q3,
			double sov, double qh, double qe, double qc, double sov_h,
			double sov_e, double sov_c) {
		this.id = id;
		this.q3 = q3;
		this.sov = sov;
		this.qh = qh;
		this.qe = qe;
		this.qc = qc;
		this.sov_h = sov_h;
		this.sov_e = sov_e;
		this.sov_c = sov_c;
		this.a = a;
		this.b = b;
		this.ps = ps;
	}

	public void createFile() throws IOException {
		try {
			// Create file
			FileWriter fstream = new FileWriter("out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Hello Java");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
