package validation;

public class Data {
	// pdb_id
	public String id;
	// real sequence from dssp file (from extract-dssp.pl)
	private String rs = "";
	// predicted sec sequence from predictions file
	private String ps = "";
	// amino_acid sequence from dssp file
	private String as = "";
	// class containing q3, sov, ...
	private Result result;

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Data() {
		// TODO Auto-generated constructor stub
	}

	// nur input daten, da berechnung erst danach passiert ... sollte zumindest
	// so sein
	public Data(String id, String rs, String ps, String as) {
		this.id = id;
		this.rs = rs;
		this.ps = ps;
		this.as = as;
	}

	// hier die ergebnisse mit einfügen
	public void addResult(double q3, double sov) {
		if (result == null)
			result = new Result();
		result.setQ3(q3);
		result.setSov(sov);
	}

	public void addResult(double q3, double qe, double qh, double qc) {
		if (result == null)
			result = new Result();
		result.setQ3(q3);
		result.setQh(qh);
		result.setQc(qc);
		result.setQe(qe);
	}

	public void addRS(String rs) {
		this.rs = rs;
	}

	@Override
	public String toString() {
		// TODO results vllt noch mit rein
		// ist aber nur zu testzwecken also nicht zwingend nötig
		return "ID: " + id + "\n" + "amino acid:\n" + as
				+ "\nsecondary_structure:\n" + rs
				+ "\npredicted secondary structure:\n" + ps;
	}

	public void calcQ3values() {
		Q3 q = new Q3(rs,ps);
		this.addResult(q.q3result, q.qeresult, q.qhresult, q.qcresult);
		q.print();
	}

	// TODO es fehlen noch weitere arten des hinzufügens
}
