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
				// FIXME NaN abfangen

				double q3 = data.getResult().q3;
				double qh = data.getResult().qh;
				if (Double.isNaN(qh) || Double.isInfinite(qh) || qh == -1)
					qh = 0;
				double qe = data.getResult().qe;
				if (Double.isNaN(qe) || Double.isInfinite(qe) || qe == -1)
					qe = 0;
				double qc = data.getResult().qc;
				if (Double.isNaN(qc) || Double.isInfinite(qc) || qc == -1)
					qc = 0;
				double sov = data.getResult().sov;

				double sov_h = data.getResult().sov_h;
				if (Double.isNaN(sov_h) || Double.isInfinite(sov_h))
					sov_h = 0;
				double sov_e = data.getResult().sov_e;
				if (Double.isNaN(sov_e) || Double.isInfinite(sov_e))
					sov_e = 0;
				double sov_c = data.getResult().sov_c;
				if (Double.isNaN(sov_c) || Double.isInfinite(sov_c))
					sov_c = 0;

				out.write(">" + data.pdbid + " " + df.format(q3) + " "
						+ df.format(sov) + " " + df.format(qh) + " "
						+ df.format(qe) + " " + df.format(qc) + " "
						+ df.format(sov_h) + " " + df.format(sov_e) + " "
						+ df.format(sov_c) + "\n");

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
			out.write("Mean: Q3 = " + df.format(q3_mean) + " | SOV = "
					+ df.format(sov_mean) + "\n");

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
			out.write("Mean: Q3 = " + df.format(q3_mean) + " | SOV = "
					+ df.format(sov_mean) + "<br />");

			out.write("</p></body></html>");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void createSummaryFileHtml(ArrayList<DataSet> ds_set,
			String filename, int iterations) throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			DecimalFormat df = new DecimalFormat("##.###");
			out.write("<html><head><title></title></head><body style=\"font-family:courier new,courier,monospace;\">");
			out.write("<h2>" + iterations + "-fold crossvalidation</h3>");
			for (DataSet ds : ds_set) {

				double q3_mean = ds.getMean_q3();
				if (Double.isNaN(q3_mean))
					q3_mean = 0;

				double sov_mean = ds.getMean_sov();
				if (Double.isNaN(sov_mean))
					sov_mean = 0;

				double qh_mean = ds.getMean_qh();
				if (Double.isNaN(qh_mean))
					qh_mean = 0;

				double qe_mean = ds.getMean_qe();
				if (Double.isNaN(qe_mean))
					qe_mean = 0;

				double qc_mean = ds.getMean_qc();
				if (Double.isNaN(qc_mean))
					qc_mean = 0;

				double sovh_mean = ds.getMean_sovh();
				if (Double.isNaN(sovh_mean))
					sovh_mean = 0;

				double sove_mean = ds.getMean_sove();
				if (Double.isNaN(sove_mean))
					sove_mean = 0;

				double sovc_mean = ds.getMean_sovc();
				if (Double.isNaN(sovc_mean))
					sovc_mean = 0;

				// stdv
				double qh_stdv = ds.getStdv_qh();
				if (Double.isNaN(qh_stdv))
					qh_stdv = 0;

				double qe_stdv = ds.getStdv_qe();
				if (Double.isNaN(qe_stdv))
					qe_stdv = 0;

				double qc_stdv = ds.getStdv_qc();
				if (Double.isNaN(qc_stdv))
					qc_stdv = 0;

				double sovh_stdv = ds.getStdv_sovh();
				if (Double.isNaN(sovh_stdv))
					sovh_stdv = 0;

				double sove_stdv = ds.getStdv_sove();
				if (Double.isNaN(sove_stdv))
					sove_stdv = 0;

				double sovc_stdv = ds.getStdv_sovc();
				if (Double.isNaN(sovc_stdv))
					sovc_stdv = 0;

				// Max
				double qh_max = ds.getMax_qh();
				if (Double.isNaN(qh_max))
					qh_max = 0;

				double qe_max = ds.getMax_qe();
				if (Double.isNaN(qe_max))
					qe_max = 0;

				double qc_max = ds.getMax_qc();
				if (Double.isNaN(qc_max))
					qc_max = 0;

				double sovh_max = ds.getMax_sovh();
				if (Double.isNaN(sovh_max))
					sovh_max = 0;

				double sove_max = ds.getMax_sove();
				if (Double.isNaN(sove_max))
					sove_max = 0;

				double sovc_max = ds.getMax_sovc();
				if (Double.isNaN(sovc_max))
					sovc_max = 0;

				// min
				double qh_min = ds.getMin_qh();
				if (Double.isNaN(qh_min))
					qh_min = 0;

				double qe_min = ds.getMin_qe();
				if (Double.isNaN(qe_min))
					qe_min = 0;

				double qc_min = ds.getMin_qc();
				if (Double.isNaN(qc_min))
					qc_min = 0;

				double sovh_min = ds.getMin_sovh();
				if (Double.isNaN(sovh_min))
					sovh_min = 0;

				double sove_min = ds.getMin_sove();
				if (Double.isNaN(sove_min))
					sove_min = 0;

				double sovc_min = ds.getMin_sovc();
				if (Double.isNaN(sovc_min))
					sovc_min = 0;

				// out.write("<div style=\"font-family:courier new,courier,monospace;overflow:auto;\">");

				out.write("<table cellpadding=\"0\" cellspacing=\"0\" style='border: 1px solid black;' width=\"800\">");
				out.write("<caption>" + ds.identity + "</caption>");

				out.write("<thead><tr><th scope=\"row\">&nbsp;</th>");
				out.write("<th scope=\"col\">Q3</th>");
				out.write("<th scope=\"col\">SOV</th>");
				out.write("<th scope=\"col\">QH</th>");
				out.write("<th scope=\"col\">QE</th>");
				out.write("<th scope=\"col\">QC</th>");
				out.write("<th scope=\"col\">SOVH</th>");
				out.write("<th scope=\"col\">SOVE</th>");
				out.write("<th scope=\"col\">SOVC</th>");
				out.write("</tr>");
				out.write("</thead>");

				out.write("<tbody>");
				out.write("<tr>");
				out.write("<th scope=\"row\">Mean</th>");
				out.write("<td>" + df.format(q3_mean) + "</td>");
				out.write("<td>" + df.format(sov_mean) + "</td>");
				out.write("<td>" + df.format(qh_mean) + "</td>");
				out.write("<td>" + df.format(qe_mean) + "</td>");
				out.write("<td>" + df.format(qc_mean) + "</td>");
				out.write("<td>" + df.format(sovh_mean) + "</td>");
				out.write("<td>" + df.format(sove_mean) + "</td>");
				out.write("<td>" + df.format(sovc_mean) + "</td>");
				out.write("</tr>");

				out.write("<tr>");
				out.write("<th scope=\"row\">Stdv</th>");
				out.write("<td>" + df.format(ds.getStdv_q3()) + "</td>");
				out.write("<td>" + df.format(ds.getStdv_sov()) + "</td>");
				out.write("<td>" + df.format(qh_stdv) + "</td>");
				out.write("<td>" + df.format(qe_stdv) + "</td>");
				out.write("<td>" + df.format(qc_stdv) + "</td>");
				out.write("<td>" + df.format(sovh_stdv) + "</td>");
				out.write("<td>" + df.format(sove_stdv) + "</td>");
				out.write("<td>" + df.format(sovc_stdv) + "</td>");
				out.write("</tr>");

				out.write("<tr>");
				out.write("<th scope=\"row\">Max</th>");
				out.write("<td>" + df.format(ds.getMax_q3()) + "</td>");
				out.write("<td>" + df.format(ds.getMax_sov()) + "</td>");
				out.write("<td>" + df.format(qh_max) + "</td>");
				out.write("<td>" + df.format(qe_max) + "</td>");
				out.write("<td>" + df.format(qc_max) + "</td>");
				out.write("<td>" + df.format(sovh_max) + "</td>");
				out.write("<td>" + df.format(sove_max) + "</td>");
				out.write("<td>" + df.format(sovc_max) + "</td>");
				out.write("</tr>");

				out.write("<tr>");
				out.write("<th scope=\"row\">Min</th>");
				out.write("<td>" + df.format(ds.getMin_q3()) + "</td>");
				out.write("<td>" + df.format(ds.getMin_sov()) + "</td>");
				out.write("<td>" + df.format(qh_min) + "</td>");
				out.write("<td>" + df.format(qe_min) + "</td>");
				out.write("<td>" + df.format(qc_min) + "</td>");
				out.write("<td>" + df.format(sovh_min) + "</td>");
				out.write("<td>" + df.format(sove_min) + "</td>");
				out.write("<td>" + df.format(sovc_min) + "</td>");
				out.write("</tr>");

				out.write("<tr>");
				out.write("<th scope=\"row\">n</th>");
				out.write("<td>" + df.format(ds.getListSize()) + "</td>");
				out.write("</tr>");
				out.write("<hr />");

				// TODO 5 %
				// TODO 95 %

				out.write("</tbody></table>");
				// out.write("Anzahl an Sequenzen analysiert = " +
				// ds.getListSize()
				// + "<br />");
				// out.write("Max: Q3 = " + df.format(ds.getMax_q3()) +
				// " | SOV = "
				// + df.format(ds.getMax_sov()) + "<br />");
				// out.write("Min: Q3 = " + df.format(ds.getMin_q3()) +
				// " | SOV = "
				// + df.format(ds.getMin_sov()) + "<br />");
				// out.write("Mean: Q3 = " + df.format(q3_mean)+ " | SOV = "
				// + df.format(sov_mean)+ "<br />");
				// out.write("Standardabweichung: Q3 = " +
				// df.format(ds.getStdv_q3())
				// + " | SOV = " + df.format(ds.getStdv_sov()) + "<br />");

				out.write("</table>");
				out.write("</div>");
			}

			out.write("</body></html>");

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
			out.write("<html><head><title></title></head><body style=\"font-family:courier new,courier,monospace;\">");
			DecimalFormat df = new DecimalFormat("##.###");
			for (Data data : daten) {
				double q3 = data.getResult().q3;
				double qh = data.getResult().qh;
				if (Double.isNaN(qh) || Double.isInfinite(qh) || qh == -1)
					qh = 0;
				double qe = data.getResult().qe;
				if (Double.isNaN(qe) || Double.isInfinite(qe) || qe == -1)
					qe = 0;
				double qc = data.getResult().qc;
				if (Double.isNaN(qc) || Double.isInfinite(qc) || qc == -1)
					qc = 0;
				double sov = data.getResult().sov;

				double sov_h = data.getResult().sov_h;
				if (Double.isNaN(sov_h) || Double.isInfinite(sov_h))
					sov_h = 0;
				double sov_e = data.getResult().sov_e;
				if (Double.isNaN(sov_e) || Double.isInfinite(sov_e))
					sov_e = 0;
				double sov_c = data.getResult().sov_c;
				if (Double.isNaN(sov_c) || Double.isInfinite(sov_c))
					sov_c = 0;

				out.write(">" + data.pdbid + " "
						+ df.format(data.getResult().q3) + "&nbsp"
						+ df.format(data.getResult().sov) + "&nbsp"
						+ df.format(qh) + "&nbsp" + df.format(qe) + "&nbsp"
						+ df.format(qc) + "&nbsp" + df.format(sov_h) + "&nbsp"
						+ df.format(sov_e) + "&nbsp" + df.format(sov_c)
						+ "<br />");

				out.write("<div style=\"font-family:courier new,courier,monospace;overflow:auto;\">");
				// erste zeile identifier

				// amino acid chain
				out.write("AS&nbsp" + data.getAs() + "<br />");

				// predicted secondary structure chain
				out.write("PS&nbsp" + data.getPs() + "<br />");

				// secondary structure chain
				out.write("RS&nbsp" + data.getRs() + "<br />");
				out.write("</div>");
				out.write("<p></p>");
			}
			out.write("</body></html>");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Gibt daten von CV für gnuplot aus
	 * 
	 * @param ds_set
	 *            set von dataset containing daten
	 * @param filename
	 *            name wo zu speichern
	 * @throws IOException
	 */
	public void createCVTableData(ArrayList<DataSet> ds_set, String filename)
			throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			DecimalFormat df = new DecimalFormat("##.###");
			out.write("k\tq3\tsov\tqh\tqe\tqc\tsovh\tsove\tsovc\tq3_stdv\tsov_stdv\n");

			int count = 1;

			for (DataSet ds : ds_set) {

				double q3_mean = ds.getMean_q3();
				if (Double.isNaN(q3_mean))
					q3_mean = 0;

				double sov_mean = ds.getMean_sov();
				if (Double.isNaN(sov_mean))
					sov_mean = 0;

				double qh_mean = ds.getMean_qh();
				if (Double.isNaN(qh_mean))
					qh_mean = 0;

				double qe_mean = ds.getMean_qe();
				if (Double.isNaN(qe_mean))
					qe_mean = 0;

				double qc_mean = ds.getMean_qc();
				if (Double.isNaN(qc_mean))
					qc_mean = 0;

				double sovh_mean = ds.getMean_sovh();
				if (Double.isNaN(sovh_mean))
					sovh_mean = 0;

				double sove_mean = ds.getMean_sove();
				if (Double.isNaN(sove_mean))
					sove_mean = 0;

				double sovc_mean = ds.getMean_sovc();
				if (Double.isNaN(sovc_mean))
					sovc_mean = 0;

				// stdv
				double qh_stdv = ds.getStdv_qh();
				if (Double.isNaN(qh_stdv))
					qh_stdv = 0;

				double qe_stdv = ds.getStdv_qe();
				if (Double.isNaN(qe_stdv))
					qe_stdv = 0;

				double qc_stdv = ds.getStdv_qc();
				if (Double.isNaN(qc_stdv))
					qc_stdv = 0;

				double sovh_stdv = ds.getStdv_sovh();
				if (Double.isNaN(sovh_stdv))
					sovh_stdv = 0;

				double sove_stdv = ds.getStdv_sove();
				if (Double.isNaN(sove_stdv))
					sove_stdv = 0;

				double sovc_stdv = ds.getStdv_sovc();
				if (Double.isNaN(sovc_stdv))
					sovc_stdv = 0;

				// Max
				double qh_max = ds.getMax_qh();
				if (Double.isNaN(qh_max))
					qh_max = 0;

				double qe_max = ds.getMax_qe();
				if (Double.isNaN(qe_max))
					qe_max = 0;

				double qc_max = ds.getMax_qc();
				if (Double.isNaN(qc_max))
					qc_max = 0;

				double sovh_max = ds.getMax_sovh();
				if (Double.isNaN(sovh_max))
					sovh_max = 0;

				double sove_max = ds.getMax_sove();
				if (Double.isNaN(sove_max))
					sove_max = 0;

				double sovc_max = ds.getMax_sovc();
				if (Double.isNaN(sovc_max))
					sovc_max = 0;

				// min
				double qh_min = ds.getMin_qh();
				if (Double.isNaN(qh_min))
					qh_min = 0;

				double qe_min = ds.getMin_qe();
				if (Double.isNaN(qe_min))
					qe_min = 0;

				double qc_min = ds.getMin_qc();
				if (Double.isNaN(qc_min))
					qc_min = 0;

				double sovh_min = ds.getMin_sovh();
				if (Double.isNaN(sovh_min))
					sovh_min = 0;

				double sove_min = ds.getMin_sove();
				if (Double.isNaN(sove_min))
					sove_min = 0;

				double sovc_min = ds.getMin_sovc();
				if (Double.isNaN(sovc_min))
					sovc_min = 0;

				out.write(count + "\t" + df.format(q3_mean) + "\t"
						+ df.format(sov_mean) + "\t" + df.format(qh_mean)
						+ "\t" + df.format(qe_mean) + "\t" + df.format(qc_mean)
						+ "\t" + df.format(sovh_mean) + "\t"
						+ df.format(sove_mean) + "\t" + df.format(sovc_mean)
						+ "\t" + df.format(ds.getStdv_q3()) + "\t"
						+ df.format(ds.getStdv_sov()) + "\n");
				count++;
			}

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Gibt daten von CV für gnuplot aus
	 * 
	 * @param ds_set
	 *            set von dataset containing daten
	 * @param filename
	 *            name wo zu speichern
	 * @throws IOException
	 */
	public void createTableData(DataSet ds, String filename,boolean append)
			throws IOException {
		try {
			// Create file
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,append));
			int count = 1;
			DecimalFormat df = new DecimalFormat("##.###");
			ArrayList<Data> daten = ds.getDataPackage();
			out.write("n\tvalues\ttype\n");

			for (Data data : daten) {
				
				double q3 = data.getResult().q3;
				double qh = data.getResult().qh;
				double qe = data.getResult().qe;
				double qc = data.getResult().qc;
				double sov = data.getResult().sov;
				double sovh = data.getResult().sov_h;
				double sove = data.getResult().sov_e;
				double sovc = data.getResult().sov_c;
				
				out.write(count+"\t"+(!Double.isNaN(q3) ? df.format(q3) : "NaN")+"\ta\tq\n");
				out.write(count+"\t"+(!Double.isNaN(qh) ? df.format(qh) : "NaN")+"\th\tq\n");
				out.write(count+"\t"+(!Double.isNaN(qe) ? df.format(qe) : "NaN")+"\te\tq\n");
				out.write(count+"\t"+(!Double.isNaN(qc) ? df.format(qc) : "NaN")+"\tc\tq\n");
				out.write(count+"\t"+(!Double.isNaN(sov) ? df.format(sov) : "NaN")+"\ta\tsov\n");
				out.write(count+"\t"+(!Double.isNaN(sovh) ? df.format(sovh) : "NaN")+"\th\tsov\n");
				out.write(count+"\t"+(!Double.isNaN(sove) ? df.format(sove) : "NaN")+"\te\tsov\n");
				out.write(count+"\t"+(!Double.isNaN(sovc) ? df.format(sovc) : "NaN")+"\tc\tsov\n");
				count++;
			}
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: ");
			e.printStackTrace();
		}
	}

}
