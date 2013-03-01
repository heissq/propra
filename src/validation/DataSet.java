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
	
	public void addQ3Chunk(String id, double q3) {
		for (Data d : daten) {
			if (d.id == id) {
				d.addResult(q3);
				return;
			}
		}

		// wenn nicht gefunden neues element anlegen
		System.out.println("id nicht gefunden bitter erst anlegen");
		// TODO case wenn id noch nicht vorhanden und strings etc erst angelegt
		// werden müssen sollte lieber erst input haben bevor output
	}

	@Override
	public String toString() {
		return "file with training data = " + dssp_filename
				+ "; file with predictions = " + predictions_filename;
	}

	public void printeverything() {
		for (Data data : daten) {
			System.out.println(data);
		}
	}
}
