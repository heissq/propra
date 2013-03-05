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

	public CreateSummary(String file, String id, String as, String ss,
			String ps, double q3, double sov, double qh, double qe, double qc,
			double sov_h, double sov_e, double sov_c, String filename) {
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

	public CreateSummary() {
	}

	public void createFile(boolean append) throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,
					append));

			// erste zeile identifier
			out.write(">" + id + " " + q3 + " " + sov + " " + qh + " " + qe
					+ " " + qc + " " + sov_h + " " + sov_e + " " + sov_c + "\n");

			// amino acid chain
			out.write("AS " + as + "\n");

			// predicted secondary structure chain
			out.write("PS " + ps + "\n");

			// secondary structure chain
			out.write("SS " + ss + "\n");

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void createDetailedFileTxt(ArrayList<Data> daten, String filename,
			boolean append) throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,
					append));
			DecimalFormat df = new DecimalFormat("##.###");
			for (Data data : daten) {
				// erste zeile identifier
				//FIXME NaN abfangen
				out.write(">" + data.pdbid + " "
						+ df.format(data.getResult().q3) + " "
						+ df.format(data.getResult().sov) + " "
						+ df.format(data.getResult().qh) + " "
						+ df.format(data.getResult().qe) + " "
						+ df.format(data.getResult().qc) + " "
						+ df.format(data.getResult().sov_h) + " "
						+ df.format(data.getResult().sov_e) + " "
						+ df.format(data.getResult().sov_c) + "\n");

				// amino acid chain
				out.write("AS " + data.getAs() + "\n");

				// predicted secondary structure chain
				out.write("PS " + data.getPs() + "\n");

				// secondary structure chain
				out.write("RS " + data.getRs() + "\n");
			}

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void createSummaryFileTxt(DataSet ds, String filename)
			throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			DecimalFormat df = new DecimalFormat("##.###");
			
			double q3_mean = ds.getMean_q3();
			if (Double.isNaN(q3_mean))
				q3_mean = 0;
			
			double sov_mean = ds.getMean_sov();
			if (Double.isNaN(sov_mean))
				sov_mean = 0;

			
			
			
			out.write("Anzahl an Sequenzen analysiert = " + ds.getListSize()
					+ "\n");
			out.write("Max: Q3 = " + df.format(ds.getMax_q3()) + " | SOV = "
					+ df.format(ds.getMax_sov()) + "\n");
			out.write("Min: Q3 = " + df.format(ds.getMin_q3()) + " | SOV = "
					+ df.format(ds.getMin_sov()) + "\n");
			out.write("Standardabweichung: Q3 = " + df.format(ds.getStdv_q3())
					+ " | SOV = " + df.format(ds.getStdv_sov()) + "\n");
			out.write("Mean: Q3 = " + df.format(q3_mean)+ " | SOV = "
					+ df.format(sov_mean)+ "\n");

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public void createSummaryFileHtml(DataSet ds, String filename)
			throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			DecimalFormat df = new DecimalFormat("##.###");
			
			double q3_mean = ds.getMean_q3();
			if (Double.isNaN(q3_mean))
				q3_mean = 0;
			
			double sov_mean = ds.getMean_sov();
			if (Double.isNaN(sov_mean))
				sov_mean = 0;

			out.write("<html><head><title></title></head><body>");
			out.write("<p style=\"font-family:courier new,courier,monospace;\">");
			out.write("Anzahl an Sequenzen analysiert = " + ds.getListSize()
					+ "<br />");
			out.write("Max: Q3 = " + df.format(ds.getMax_q3()) + " | SOV = "
					+ df.format(ds.getMax_sov()) + "<br />");
			out.write("Min: Q3 = " + df.format(ds.getMin_q3()) + " | SOV = "
					+ df.format(ds.getMin_sov()) + "<br />");
			out.write("Standardabweichung: Q3 = " + df.format(ds.getStdv_q3())
					+ " | SOV = " + df.format(ds.getStdv_sov()) + "<br />");
			out.write("Mean: Q3 = " + df.format(q3_mean)+ " | SOV = "
					+ df.format(sov_mean)+ "<br />");

			out.write("</p></body></html>");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	
	public void createDetailedFileHtml(ArrayList<Data> daten, String filename,
			boolean append) throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,
					append));
			out.write("<html><head><title></title></head><body>");
			DecimalFormat df = new DecimalFormat("##.###");
			for (Data data : daten) {
				out.write("<div style=\"font-family:courier new,courier,monospace;overflow:auto;\">");
				out.write("<p style=\"font-family:courier new,courier,monospace;\">");
				// erste zeile identifier
				
				double q3 = data.getResult().q3;
				double qh = data.getResult().qh;
				if(Double.isNaN(qh) || Double.isInfinite(qh) || qh == -1)
					qh = 0;
				double qe = data.getResult().qe;
				if(Double.isNaN(qe) || Double.isInfinite(qe) || qe == -1)
					qe = 0;
				double qc = data.getResult().qc;
				if(Double.isNaN(qc) || Double.isInfinite(qc) || qc == -1)
					qc = 0;
				double sov = data.getResult().sov;
				
				double sov_h = data.getResult().sov_h;
				if(Double.isNaN(sov_h) || Double.isInfinite(sov_h))
					sov_h = 0;
				double sov_e = data.getResult().sov_e;
				if(Double.isNaN(sov_e) || Double.isInfinite(sov_e))
					sov_e = 0;
				double sov_c = data.getResult().sov_c;
				if(Double.isNaN(sov_c) || Double.isInfinite(sov_c))
					sov_c = 0;
				
				out.write(">" + data.pdbid + " "
						+ df.format(data.getResult().q3) + "&nbsp"
						+ df.format(data.getResult().sov) + "&nbsp"
						+ df.format(qh) + "&nbsp"
						+ df.format(qe) + "&nbsp"
						+ df.format(qc) + "&nbsp"
						+ df.format(sov_h) + "&nbsp"
						+ df.format(sov_e) + "&nbsp"
						+ df.format(sov_c) + "<br />");

				// amino acid chain
				out.write("AS&nbsp" + data.getAs() + "<br />");

				// predicted secondary structure chain
				out.write("PS&nbsp" + data.getPs() + "<br />");

				// secondary structure chain
				out.write("RS&nbsp" + data.getRs() + "<br />");
				out.write("</p>");
				out.write("</div>");
			}
			out.write("</body></html>");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
