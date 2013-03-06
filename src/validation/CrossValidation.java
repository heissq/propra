package validation;

import gorpack.Sequence;
import gorpack.Sequence2;
import gorpack.TrainPredict2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CrossValidation {
	public int identifier;
	private int n;
	private int trainn;
	private DataSet dataset;
	private ArrayList<DataSet> ds_set = new ArrayList<DataSet>();

	// variablen noch nicht sicher
	private ArrayList<Data> daten;

	// kann dataset auch über Main.dataset beziehen... bin mir nur nicht sicher,
	// deshalb: doppelt hält besser ^^

	// rauspicken von daten...deswegen dataset als gesamtes datenpaket inklusiv

	/**
	 * CrossValidation Konstruktor bekommt nur anzahl der elemente für chunksize
	 * -> testdaten größe
	 * 
	 * @param ds
	 *            dataset inklusiver aller vorhandenen daten
	 * @param chunkSize
	 *            größe des testdatenpaket
	 */
	public CrossValidation(DataSet ds, int n, int trainn) {
		dataset = ds;
		makeDataPackage();
		this.n = n;
		// prozentualer anteil
		this.trainn = trainn;
	}

	// alle daten in package --> n zählen oder size daten
	public void makeDataPackage() {
		daten = dataset.getDataPackage();
	}

	public void oneWholeCV(boolean gor3) {
		for (int i = 0; i < n; i++) {
			ds_set.add(oneIteration(i, gor3));
			ds_set.get(i).calcSummaryStatistics();
		}
	}

	public void repeatedCV(int anzahlshuffles, boolean gor3, String filename,
			String filename2, boolean is_html) {
		for (int i = 0; i < anzahlshuffles; i++) {
			Collections.shuffle(daten);
			oneWholeCV(gor3);
			try {
				if (is_html) {
				}
				createSummaries("r" + i + "_" + filename, is_html);
				createDetailedSummaries("r" + i + "_" + filename2, is_html);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ds_set.clear();
		}
	}

	// iteration geht von 0-9 bei n = 10
	public DataSet oneIteration(int iteration, boolean gor3) {
		int size = daten.size();
		int chunksize = (int) (Math.abs((double) size / n) * trainn);
		ArrayList<Data> training_set = new ArrayList<Data>();
		ArrayList<Data> test_set = new ArrayList<Data>();
		// System.out.println(chunksize+"="+size+"/"+n);
		for (int i = 0; i < size; i++) {
			if ((int) (iteration * chunksize) <= i
					&& i <= (int) ((iteration + 1) * chunksize)) {
				test_set.add(daten.get(i));
			} else {
				training_set.add(daten.get(i));
			}
		}

		Sequence[] model = new Sequence[training_set.size()];
		Sequence[] test = new Sequence[test_set.size()];

		for (int i = 0; i < training_set.size(); i++) {
			// String as = training_set.get(i).getAs();
			// String rs = training_set.get(i).getRs();
			// String pdbid = training_set.get(i).pdbid;
			// model[i] = new Sequence(pdbid,as,rs);
			model[i] = convertDataToSequence(training_set.get(i), true);
		}

		for (int i = 0; i < test_set.size(); i++) {
			// String as = test_set.get(i).getAs();
			// String rs = test_set.get(i).getRs();
			// String pdbid = test_set.get(i).pdbid;
			// test[i] = new Sequence(pdbid,as,rs);
			test[i] = convertDataToSequence(test_set.get(i), false);
		}

		// für return sequence2 nehmen
		Sequence2[] returnarray = new Sequence2[test_set.size()];

		returnarray = TrainPredict2.predictTrain(test, model, gor3);
		// System.out.println(returnarray[2]);
		DataSet tmp = new DataSet("Iteration = "+String.valueOf(iteration+1));
		tmp.toString();
		for (Sequence2 sequence2 : returnarray) {
			convertSequenceToResult(sequence2, tmp);
		}
		return tmp;
	}

	// für model oder prediction
	public Sequence convertDataToSequence(Data d, boolean model) {
		if (model)
			return new Sequence(d.pdbid, d.getAs(), d.getRs());
		else
			// falls kein model sondern zu predicten struktur d.getRs()
			// eigentlich sinnlos... aber vllt für validierung
			return new Sequence(d.pdbid, d.getAs(), d.getRs());
	}

	// wenn predicted structure zurückkommt von trainpredict2.java
	public void convertSequenceToResult(Sequence2 s, DataSet ds) {
		ds.addInputChunk(s.getid(), s.getRs(), s.getss(), s.getps());
	}

	public void createSummaries(String filename, boolean is_html)
			throws IOException {
		CreateSummary csum = new CreateSummary();
		int i = 0;
		String tmp = filename.replaceAll(".txt", ".html");
		for (DataSet ds : ds_set) {
			i++;
			boolean printeddebug = false;
			if (is_html) {
				if (!printeddebug) {
					csum.createSummaryFileHtml(ds_set,tmp,n);
					csum.createCVTableData(ds_set, filename+".txt");
				}
				printeddebug = true;
			}

			else
				csum.createSummaryFileTxt(ds, String.valueOf(i) + filename);
		}
	}

	public void createDetailedSummaries(String filename, boolean is_html)
			throws IOException {
		CreateSummary csum = new CreateSummary();
		int i = 0;
		for (DataSet ds : ds_set) {
			i++;
			if (is_html) {
				String tmp = filename.replaceAll(".txt", ".html");
				csum.createDetailedFileHtml(ds.getDataPackage(), i + tmp, false);
			} else
				csum.createDetailedFileTxt(ds.getDataPackage(), i + filename,
						false);
		}
	}
	// public void makeSummaries
}
