package validation;

import java.util.ArrayList;

public class ResultSetCV {
	private ArrayList<CVResult[]> results;
	private CVResult[] current_resultset;
	
	private ArrayList<Data> daten;
	
	private int current_iteration;
	private int current_iteration_set;
	
	public ResultSetCV(DataSet ds) {
		this.init(ds);
	}
	
	public void init(DataSet ds) {
		daten = ds.getDataPackage();
	}
	
	public void oneIteration() {
		
	}
}
