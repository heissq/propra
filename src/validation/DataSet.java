package validation;

import java.util.ArrayList;

public class DataSet {
	// filenames
	private String predictions_filename = "";
	private String dssp_filename = "";
	private boolean isempty = true;

	// daten als datenpaket in dataset gespeichert
	// bestehend aus daten
	private static ArrayList<Data> daten = new ArrayList<>();

	public DataSet() {
		predictions_filename = Main.predictions_file;
		dssp_filename = Main.dssp_file;
	}
	
	public void setFiles(String pred,String dssp) {
		predictions_filename = pred;
		dssp_filename = dssp;
	}

	public void addInputChunk(String id, String rs, String ps, String as) {
		if (!isempty) {
			for (Data d : daten) {
				if (d.pdbid == id) {
					// TODO prüfen ob daten richtig noch nicht wirklich
					// implementiert...
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
				+ "; file with predictions = " + predictions_filename;
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

	public void calcSOVvalues() {
		for (Data data : daten) {
			data.calcSOVvalues();
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
		ArrayList<Data> tmp = new ArrayList<>();
		for (Data data : daten) {
			tmp.add(data);
		}
		return tmp;
	}
}
