package validation;

import java.util.ArrayList;

public class DataSet {
	// empty?
	private boolean isempty = true;

	// id
	public String identity = "";

	// sov
	private double stdv_sov = -1; // standarddeviation
	private double stdv_sovh = -1;
	private double stdv_sove = -1;
	private double stdv_sovc = -1;
	private double mean_sov = -1; // mean ....
	private double max_sov = -1;
	private double max_sovh = -1;
	private double max_sove = -1;
	private double max_sovc = -1;
	private double min_sov = 101;
	private double min_sovh = 101;
	private double min_sove = 101;
	private double min_sovc = 101;
	private double mean_sovh = -1;
	private double mean_sove = -1;
	private double mean_sovc = -1;

	// q3
	private double stdv_q3 = -1; // standarddeviation
	private double stdv_qh = -1;
	private double stdv_qe = -1;
	private double stdv_qc = -1;
	private double mean_q3 = -1; // mean ....
	private double max_q3 = -1;
	private double max_qh = -1;
	private double max_qe = -1;
	private double max_qc = -1;
	private double min_q3 = 101;
	private double min_qh = 101;
	private double min_qe = 101;
	private double min_qc = 101;
	private double mean_qh = -1;
	private double mean_qe = -1;
	private double mean_qc = -1;

	// daten als datenpaket in dataset gespeichert
	// bestehend aus daten
	private ArrayList<Data> daten = new ArrayList<Data>();

	public DataSet() {

	}

	public DataSet(String identity) {
		this.identity = identity;
	}

	public void addInputChunk(String id, String rs, String ps, String as) {
		if (!isempty) {
			for (Data d : daten) {
				if (d.pdbid.equals(id)) {
					// TODO prüfen ob daten richtig noch nicht wirklich
					// implementiert... also ob sequenz aa_seq mit der schon
					// gespeicherten auch übereinstimmt
					if (d.getAs().isEmpty()) {
						d.setAs(as);
					}
					if (d.getRs().isEmpty()) {
						d.setRs(rs);
					}
					if (d.getPs().isEmpty()) {
						d.setPs(ps);
					}
					return;
				}
			}
		}

		// wenn nicht gefunden neues element anlegen

		daten.add(new Data(id, rs, ps, as));
		isempty = false;
	}

	@Override
	public String toString() {
		return "NumberOfData = " + daten.size()
				+ "\nValidierungsDatenenthalten?:"
				+ (daten.size() > 0 ? daten.get(0).containsSomeResult() : "");
	}

	public void printeverything() {
		for (Data data : daten) {
			System.out.println(data);
			System.out.println();
			System.out
					.println("-------------------------------------------------------------");
			System.out.println();
		}
	}

	public void printids() {
		for (Data data : daten) {
			System.out.println(data.pdbid);
		}
	}

	public Data getDataFromPDBId(String pdbid) {
		for (Data data : daten) {
			if (data.pdbid.equals(pdbid))
				return data;
		}
		return null;
	}

	public int getListSize() {
		return daten.size();
	}

	public void calcQ3values() {
		for (Data data : daten) {
			data.calcQ3values();
		}
	}

	/**
	 * parameter model mit einbezogen zur kalkulation
	 * 
	 * @param windowsize
	 *            ist gleich der hälfte von windowsize-1 z.B 17 windowsize (1xAA
	 *            + 16xAA) = 17-1 = 16/2 = 8
	 */
	public void calcSOVvalues(int windowsize) {
		for (Data data : daten) {
			data.calcSOVvalues(windowsize);
		}
	}

	public void printSegments() {
		for (Data data : daten) {
			data.printSegments();
		}
	}

	public void printSegmentByPDBId(String pdbid) {
		for (Data data : daten) {
			if (data.pdbid.equals(pdbid))
				data.printSegments();
		}
	}

	// whole data return
	public ArrayList<Data> getDataPackage() {
		ArrayList<Data> tmp = new ArrayList<Data>();
		for (Data data : daten) {
			tmp.add(data);
		}
		return tmp;
	}

	public void printDataByPDBId(String pdbid) {
		for (Data data : daten) {
			if (data.pdbid.equals(pdbid))
				data.printeverything();
		}
	}

	public boolean completeResults() {
		for (Data d : daten) {
			if (!d.containsCompleteResults())
				return false;
		}
		return true;
	}

	public void calcSummaryStatistics() {
		// arrays um werte für varianz auszurechnen
		double[] db_array_sov = new double[daten.size()];
		double[] db_array_sovh = new double[daten.size()];
		double[] db_array_sove = new double[daten.size()];
		double[] db_array_sovc = new double[daten.size()];
		
		double[] db_array_q3 = new double[daten.size()];
		double[] db_array_q3h = new double[daten.size()];
		double[] db_array_q3e = new double[daten.size()];
		double[] db_array_q3c = new double[daten.size()];

		// qhec und sov hec aber nur mean value/arithmetisches mittel
		double sum_array_qh = 0;
		double sum_array_qe = 0;
		double sum_array_qc = 0;

		double sum_array_sovh = 0;
		double sum_array_sove = 0;
		double sum_array_sovc = 0;

		double sum_sov = 0;
		double sum_q3 = 0;

		int countqh = 0;
		int countqe = 0;
		int countqc = 0;

		int countsovh = 0;
		int countsove = 0;
		int countsovc = 0;

		for (int i = 0; i < daten.size(); i++) {
			db_array_sov[i] = daten.get(i).getResult().sov;
			db_array_q3[i] = daten.get(i).getResult().q3;

			// arithmetisches mittel
			sum_sov += db_array_sov[i];
			sum_q3 += db_array_q3[i];
			if (!Double.isNaN(daten.get(i).getResult().qh)) {
				sum_array_qh += daten.get(i).getResult().qh;
				db_array_q3h[i-countqh] = daten.get(i).getResult().qh;
				max_qh = Math.max(daten.get(i).getResult().qh, max_qh);
				min_qh = Math.min(daten.get(i).getResult().qh, min_qh);
			} else
				countqh++;

			if (!Double.isNaN(daten.get(i).getResult().qe)) {
				sum_array_qe += daten.get(i).getResult().qe;
				db_array_q3e[i-countqe] = daten.get(i).getResult().qe;
				max_qe = Math.max(daten.get(i).getResult().qe, max_qe);
				min_qe = Math.min(daten.get(i).getResult().qe, min_qe);
			} else
				countqe++;
			if (!Double.isNaN(daten.get(i).getResult().qc)) {
				sum_array_qc += daten.get(i).getResult().qc;
				db_array_q3c[i-countqc] = daten.get(i).getResult().qc;
				max_qc = Math.max(daten.get(i).getResult().qc, max_qc);
				min_qc = Math.min(daten.get(i).getResult().qc, min_qc);
			} else
				countqc++;

			if (!Double.isNaN(daten.get(i).getResult().sov_h)) {
				sum_array_sovh += daten.get(i).getResult().sov_h;
				db_array_sovh[i-countsovh] = daten.get(i).getResult().sov_h;
				max_sovh = Math.max(daten.get(i).getResult().sov_h, max_sovh);
				min_sovh = Math.min(daten.get(i).getResult().sov_h, min_sovh);
			} else
				countsovh++;
			if (!Double.isNaN(daten.get(i).getResult().sov_e)) {
				sum_array_sove += daten.get(i).getResult().sov_e;
				db_array_sove[i-countsove] = daten.get(i).getResult().sov_e;
				max_sove = Math.max(daten.get(i).getResult().sov_e, max_sove);
				min_sove = Math.min(daten.get(i).getResult().sov_e, min_sove);
			} else
				countsove++;
			if (!Double.isNaN(daten.get(i).getResult().sov_c)) {
				sum_array_sovc += daten.get(i).getResult().sov_c;
				db_array_sovc[i-countsovc] = daten.get(i).getResult().sov_c;
				max_sovc = Math.max(daten.get(i).getResult().sov_c, max_sovc);
				min_sovc = Math.min(daten.get(i).getResult().sov_c, min_sovc);
			} else
				countsovc++;

			// max und min für q3 plus sov
			max_q3 = Math.max(daten.get(i).getResult().q3, max_q3);
			min_q3 = Math.min(daten.get(i).getResult().q3, min_q3);
			max_sov = Math.max(daten.get(i).getResult().sov, max_sov);
			min_sov = Math.min(daten.get(i).getResult().sov, min_sov);
		}

		// arithmetisches mittel
		double armit_sov = sum_sov / db_array_sov.length;
		double armit_q3 = sum_q3 / db_array_q3.length;
		// wenn median gleich arithmetisches mittel anscheinend ist dann:
		mean_q3 = armit_q3;
		mean_sov = armit_sov;
		mean_qh = sum_array_qh / (daten.size() - countqh);
		mean_qe = sum_array_qe / (daten.size() - countqe);
		mean_qc = sum_array_qc / (daten.size() - countqc);
		mean_sovh = sum_array_sovh / (daten.size() - countsovh);
		mean_sove = sum_array_sove / (daten.size() - countsove);
		mean_sovc = sum_array_sovc / (daten.size() - countsovc);

		// oben werte... kein plan...wie man das nennen soll
		double sum_oben_sov = 0;
		double sum_oben_q3 = 0;
		double sum_oben_qh = 0;
		double sum_oben_qe = 0;
		double sum_oben_qc = 0;
		double sum_oben_sovh = 0;
		double sum_oben_sove = 0;
		double sum_oben_sovc = 0;

		// summen
		for (double sov : db_array_sov) {
			sum_oben_sov += Math.pow((sov - armit_sov), 2);
		}

		for (double q3 : db_array_q3) {
			sum_oben_q3 += Math.pow((q3 - armit_q3), 2);
		}

		for (int i = 0; i<db_array_q3h.length-countqh;i++){
			sum_oben_qh += Math.pow((db_array_q3h[i] - mean_qh), 2);
		}
		
		for (int i = 0; i<db_array_q3e.length-countqe;i++){
			sum_oben_qe += Math.pow((db_array_q3e[i] - mean_qe), 2);
		}
		
		for (int i = 0; i<db_array_q3c.length-countqc;i++){
			sum_oben_qc += Math.pow((db_array_q3c[i] - mean_qc), 2);
		}

		for (int i = 0; i<db_array_sovh.length-countsovh;i++){
			sum_oben_sovh += Math.pow((db_array_sovh[i] - mean_sovh), 2);
		}
		
		for (int i = 0; i<db_array_sove.length-countsove;i++){
			sum_oben_sove += Math.pow((db_array_sove[i] - mean_sove), 2);
		}
		
		for (int i = 0; i<db_array_sovc.length-countsovc;i++){
			sum_oben_sovc += Math.pow((db_array_sovc[i] - mean_sovc), 2);
		}
		
		// varianzen
		double varianz_sov = sum_oben_sov / db_array_sov.length;
		double varianz_q3 = sum_oben_q3 / db_array_q3.length;
		double varianz_qh = sum_oben_qh / (db_array_q3h.length-countqh);
		double varianz_qe = sum_oben_qe / (db_array_q3e.length-countqe);
		double varianz_qc = sum_oben_qc / (db_array_q3c.length-countqc);
		double varianz_sovh = sum_oben_sovh / (db_array_sovh.length-countsovh);
		double varianz_sove = sum_oben_sove / (db_array_sove.length-countsove);
		double varianz_sovc = sum_oben_sovc / (db_array_sovc.length-countsovc);

		// STDV
		stdv_sov = Math.sqrt(varianz_sov);
		stdv_sovh = Math.sqrt(varianz_sovh);
		stdv_sove = Math.sqrt(varianz_sove);
		stdv_sovc = Math.sqrt(varianz_sovc);
		stdv_q3 = Math.sqrt(varianz_q3);
		stdv_qh = Math.sqrt(varianz_qh);
		stdv_qe = Math.sqrt(varianz_qe);
		stdv_qc = Math.sqrt(varianz_qc);

		if (daten.size() == 1) {
			min_q3 = daten.get(0).getResult().q3;
			max_q3 = daten.get(0).getResult().q3;

			max_sov = daten.get(0).getResult().sov;
			min_sov = daten.get(0).getResult().sov;
		}
	}

	public double getStdv_sovh() {
		return stdv_sovh;
	}

	public double getStdv_sove() {
		return stdv_sove;
	}

	public double getStdv_sovc() {
		return stdv_sovc;
	}

	public double getMax_sovh() {
		return max_sovh;
	}

	public double getMax_sove() {
		return max_sove;
	}

	public double getMax_sovc() {
		return max_sovc;
	}

	public double getMin_sovh() {
		return min_sovh;
	}

	public double getMin_sove() {
		return min_sove;
	}

	public double getMin_sovc() {
		return min_sovc;
	}

	public double getStdv_qh() {
		return stdv_qh;
	}

	public double getStdv_qe() {
		return stdv_qe;
	}

	public double getStdv_qc() {
		return stdv_qc;
	}

	public double getMax_qh() {
		return max_qh;
	}

	public double getMax_qe() {
		return max_qe;
	}

	public double getMax_qc() {
		return max_qc;
	}

	public double getMin_qh() {
		return min_qh;
	}

	public double getMin_qe() {
		return min_qe;
	}

	public double getMin_qc() {
		return min_qc;
	}

	public double getStdv_sov() {
		return stdv_sov;
	}

	public double getMean_sov() {
		return mean_sov;
	}

	public double getMax_sov() {
		return max_sov;
	}

	public double getMin_sov() {
		return min_sov;
	}

	public double getStdv_q3() {
		return stdv_q3;
	}

	public double getMean_q3() {
		return mean_q3;
	}

	public double getMax_q3() {
		return max_q3;
	}

	public double getMin_q3() {
		return min_q3;
	}

	public double getMean_sovh() {
		return mean_sovh;
	}

	public double getMean_sove() {
		return mean_sove;
	}

	public double getMean_sovc() {
		return mean_sovc;
	}

	public double getMean_qh() {
		return mean_qh;
	}

	public double getMean_qe() {
		return mean_qe;
	}

	public double getMean_qc() {
		return mean_qc;
	}
}
