package validation;

import java.util.ArrayList;

public class DataSet {
	// filenames
	private String predictions_filename = "";
	private String dssp_filename = "";
	private boolean isempty = true;
	
	//sov
	private double stdv_sov = -1; //standarddeviation
	private double mean_sov = -1; //mean ....
	private double max_sov = -1;
	private double min_sov = 101;
	
	//q3
	private double stdv_q3 = -1; //standarddeviation
	private double mean_q3 = -1; //mean ....
	private double max_q3 = -1;
	private double min_q3 = 101;

	// daten als datenpaket in dataset gespeichert
	// bestehend aus daten
	private ArrayList<Data> daten = new ArrayList<Data>();
	
	public void setFiles(String pred,String dssp) {
		predictions_filename = pred;
		dssp_filename = dssp;
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
		return "file with training data = " + dssp_filename
				+ "; file with predictions = " + predictions_filename +"\nNumberOfData = " + daten.size() + 
				"\nValidierungsDatenenthalten?:" 
				+ (daten.size() > 0 ? daten.get(0).containsSomeResult(): "");
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
	
	//whole data return
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
		for (Data d : daten){
			if (!d.containsCompleteResults())
				return false;
		}
		return true;
	}
	
	public void calcSummaryStatistics () {
		// arrays um werte für varianz auszurechnen
		double[] db_array_sov = new double[daten.size()];
		double[] db_array_q3 = new double[daten.size()];
		
		double sum_sov = 0;
		double sum_q3 = 0;
		
		for (int i = 0; i<daten.size();i++){
			db_array_sov[i] = daten.get(i).getResult().sov;
			db_array_q3[i] = daten.get(i).getResult().q3;
			
			//arithmetisches mittel
			sum_sov += db_array_sov[i];
			sum_q3 += db_array_q3[i];
			
			//max und min für q3 plus sov
			max_q3 = Math.max(daten.get(i).getResult().q3,max_q3);
			min_q3 = Math.min(daten.get(i).getResult().q3,min_q3);
			max_sov = Math.max(daten.get(i).getResult().sov,max_sov);
			min_sov = Math.min(daten.get(i).getResult().sov,min_sov);
		}
		
		//arithmetisches mittel
		double armit_sov = sum_sov/db_array_sov.length;
		double armit_q3 = sum_q3/db_array_q3.length;
		//wenn median gleich arithmetisches mittel anscheinend ist dann:
		mean_q3 = armit_q3;
		mean_sov = armit_sov;
		
		//oben werte... kein plan...wie man das nennen soll
		double sum_oben_sov = 0;
		double sum_oben_q3 = 0;
		
		//summen
		for (double sov : db_array_sov ) {
			sum_oben_sov += Math.pow((sov-armit_sov),2);
		}
		
		for (double q3 : db_array_q3 ) {
			sum_oben_q3 += Math.pow((q3-armit_q3),2);
		}
		
		//varianzen
		double varianz_sov = sum_oben_sov/db_array_sov.length;
		double varianz_q3 = sum_oben_q3/db_array_q3.length;
		
		//STDV
		stdv_sov = Math.sqrt(varianz_sov);
		stdv_q3 = Math.sqrt(varianz_q3);
	}

	public double getStdv_sov() {
		return stdv_sov;
	}

	public void setStdv_sov(double stdv_sov) {
		this.stdv_sov = stdv_sov;
	}

	public double getMean_sov() {
		return mean_sov;
	}

	public void setMean_sov(double mean_sov) {
		this.mean_sov = mean_sov;
	}

	public double getMax_sov() {
		return max_sov;
	}

	public void setMax_sov(double max_sov) {
		this.max_sov = max_sov;
	}

	public double getMin_sov() {
		return min_sov;
	}

	public void setMin_sov(double min_sov) {
		this.min_sov = min_sov;
	}

	public double getStdv_q3() {
		return stdv_q3;
	}

	public void setStdv_q3(double stdv_q3) {
		this.stdv_q3 = stdv_q3;
	}

	public double getMean_q3() {
		return mean_q3;
	}

	public void setMean_q3(double mean_q3) {
		this.mean_q3 = mean_q3;
	}

	public double getMax_q3() {
		return max_q3;
	}

	public void setMax_q3(double max_q3) {
		this.max_q3 = max_q3;
	}

	public double getMin_q3() {
		return min_q3;
	}

	public void setMin_q3(double min_q3) {
		this.min_q3 = min_q3;
	}
}
