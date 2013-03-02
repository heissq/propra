package validation;

public class CrossValidation {
	public int identifier;
	public int k;
	public int n;
	private DataSet dataset;
	
	//variablen noch nicht sicher
	private ArrayList<Data>
	
	//kann dataset auch über Main.dataset beziehen... bin mir nur nicht sicher, deshalb: doppelt hält besser ^^
	
	public CrossValidation(DataSet ds,int k, int n) {
		this.n = n;
		this.k = k;
		dataset = ds;
	}

	
}
