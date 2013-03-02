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


	public void addInputChunk(String id, String rs, String ps, String as) {
		if (!isempty) {
			for (Data d : daten) {
				if (d.id == id) {
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
			System.out.println(data.id);
		}
	}

	public Data getDataFromId(String searchid) {
		for (Data data : daten) {
			if (data.id == searchid)
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
}
