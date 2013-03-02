package validation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import gorpack.*;

public class CreateSummary {
	// id
	private String id = "";
	
	// parameter to validate:
	private double q3 = -1;
	private double sov = -1;
	private double qh = -1; // TODO die drei q dinger berechnen
	private double qe = -1;
	private double qc = -1;
	private double sov_h = -1;
	private double sov_e = -1;
	private double sov_c = -1;
	
	// filename
	private String filename = "";

	// strings a und b für aa-seq
	private String as = "";
	private String ss = "";
	private String ps = "";

	public CreateSummary(String file, String id, String as, String ss, String ps, double q3,
			double sov, double qh, double qe, double qc, double sov_h,
			double sov_e, double sov_c, String filename) {
		this.id = id;
		this.q3 = q3;
		this.sov = sov;
		this.qh = qh;
		this.qe = qe;
		this.qc = qc;
		this.sov_e = sov_e;
		this.sov_c = sov_c;
		this.as = as;
		this.ss = ss;
		this.ps = ps;
		this.filename = filename;
	}

	public CreateSummary(String id, String filename) {
		super();
		this.id = id;
		this.filename = filename;
	}

	public void createFile() throws IOException {
		try { // TODO format richtig ansetzen --> frage wie viele sequenzen?
				// Create file
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			
			//erste zeile identifier
			out.write(">"+id+" "+q3+" "+sov+" "+qh+" "+qe+" "+qc+" "+sov_h+" "+sov_e+" "+sov_c+"\n");
			
			//amino acid chain
			out.write("AS "+as+"\n");
			
			//predicted secondary structure chain
			out.write("PS "+ps+"\n");
			
			//secondary structure chain
			out.write("SS "+ss+"\n");
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
