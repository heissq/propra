package validation;

import java.util.ArrayList;

public class Data {
	// pdb_id
	public String pdbid;
	// real sequence from dssp file (from extract-dssp.pl)
	private String rs = "";
	// predicted sec sequence from predictions file
	private String ps = "";
	// amino_acid sequence from dssp file
	private String as = "";
	// class containing q3, sov, ...
	private Result result;
	private ArrayList<Segment> observed_segments = new ArrayList<>();
	private ArrayList<Segment> predicted_segments = new ArrayList<>();
	private ArrayList<DoubleSegment> dbl_segments = new ArrayList<>();

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
	public Data(String pdbid, String rs, String ps, String as) {
		this.pdbid = pdbid;
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
		return "ID: " + pdbid + "\n" + "amino acid:\n" + as
				+ "\nsecondary_structure:\n" + rs
				+ "\npredicted secondary structure:\n" + ps;
	}

	public void calcQ3values() {
		Q3 q = new Q3(rs, ps);
		this.addResult(q.q3result, q.qeresult, q.qhresult, q.qcresult);
	}
	public void printQ3() {
		System.out.println(result);
	}

	//FIXME glaube nicht vollständiges letztes segment für jede sequenz
	public void calcSegments() {
		// observed segments
		char lastc = 'X';
		Segment tmp = null;
		for (int i = 0; i < rs.length(); i++) {
			if (lastc != rs.charAt(i))
			{
				if (lastc != 'X')
					observed_segments.add(tmp);
				tmp = new Segment(i,rs.charAt(i),i,i);
			}
			if (lastc == rs.charAt(i))
				tmp.endpos = i;
				
			// nun speichern des letzten chars..
			lastc = rs.charAt(i);
		}
		
		// predicted segments
		lastc = 'X';
		tmp = null;
		for (int i = 0; i < ps.length(); i++) {
			if (lastc != ps.charAt(i))
			{
				if (lastc != 'X')
					predicted_segments.add(tmp);
				tmp = new Segment(i,ps.charAt(i),i,i);
			}

			if (lastc == ps.charAt(i))
				tmp.endpos = i;

			// nun speichern des letzten chars..
			lastc = ps.charAt(i);
		}
	}	
	
	//TODO doublesegments fehlen noch
	public void printSegments() {
		System.out.println("Segments of observed:");
		for (Segment sobs : observed_segments) {
			System.out.println(sobs.id+";"+sobs.ch+";"+sobs.startpos+";"+sobs.endpos);
		}
		
		System.out.println("Segments of predicted:");
		for (Segment pobs : predicted_segments) {
			System.out.println(pobs.id+";"+pobs.ch+";"+pobs.startpos+";"+pobs.endpos);
		}
	}

	public void calcSOVvalues() {
		// TODO Auto-generated method stub
		calcSegments();
	}
}
