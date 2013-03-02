package validation;

import java.util.ArrayList;

public class CrossValidation {
	public int identifier;
	public int n;
	public int chunkSize;
	private DataSet dataset;

	// variablen noch nicht sicher
	private ArrayList<Data> daten;

	// kann dataset auch über Main.dataset beziehen... bin mir nur nicht sicher,
	// deshalb: doppelt hält besser ^^

	// rauspicken von daten...deswegen dataset als gesamtes datenpaket inklusiv

	/**
	 * CrossValidation Konstruktor bekommt nur anzahl der elemente für chunksize -> testdaten größe
	 * 
	 * @param ds dataset inklusiver aller vorhandenen daten
	 * @param chunkSize größe des testdatenpaket
	 */
	public CrossValidation(DataSet ds, int chunkSize) {
		this.chunkSize = chunkSize;
		dataset = ds;
		makeDataPackage();
	}
	//TODO unterschiedliche varianten für konstruktor

	// alle daten in package --> n zählen oder size daten
	// TODO anzahl der elemente in extra methode - polymorphie
	public void makeDataPackage() {
		daten = dataset.getDataPackage();
	}
	
	
}
