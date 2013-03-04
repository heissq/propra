package validation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CreateSummary {
	// id
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
		this.id = id;
		this.filename = filename;
	}

	public void createFile(boolean append) throws IOException {
		try {
				// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,append));
			
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
	
	public void createDetailedFileTxt(ArrayList<Data> daten,String filename,boolean append) throws IOException {
		try {
				// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,append));
			DecimalFormat df = new DecimalFormat("##.###");
			for (Data data : daten) {
				//erste zeile identifier
				out.write(">"+data.pdbid+" "+df.format(data.getResult().q3)+" "+df.format(data.getResult().sov)+" "+df.format(data.getResult().qh)+" "+df.format(data.getResult().qe)+" "+df.format(data.getResult().qc)+" "+df.format(data.getResult().sov_h)+" "+df.format(data.getResult().sov_e)+" "+df.format(data.getResult().sov_c)+"\n");
				
				//amino acid chain
				out.write("AS "+data.getAs()+"\n");
				
				//predicted secondary structure chain
				out.write("PS "+data.getPs()+"\n");
				
				//secondary structure chain
				out.write("RS "+data.getRs()+"\n");
			}
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
