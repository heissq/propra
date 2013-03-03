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
			if (i == rs.length()-1)
				observed_segments.add(tmp);
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
			if (i == rs.length()-1)
				predicted_segments.add(tmp);
		}
		deleteHyphenSegments();
		calcDoubleSegments();
	}	
	
	public void printSegments() {
		System.out.println("Segments of observed:");
		for (Segment sobs : observed_segments) {
			System.out.println(sobs.id+";"+sobs.ch+";"+sobs.startpos+";"+sobs.endpos);
		}
		
		System.out.println("Segments of predicted:");
		for (Segment pobs : predicted_segments) {
			System.out.println(pobs.id+";"+pobs.ch+";"+pobs.startpos+";"+pobs.endpos);
		}

		System.out.println("Segments double:");
		for (DoubleSegment dbseg : dbl_segments) {
			System.out.println(dbseg.id);
			System.out.println(dbseg.observed.id + ";" + dbseg.observed.ch
					+ ";" + dbseg.observed.startpos + ";"
					+ dbseg.observed.endpos);
			System.out.println(dbseg.predicted.id + ";" + dbseg.predicted.ch
					+ ";" + dbseg.predicted.startpos + ";"
					+ dbseg.predicted.endpos);
		}
	}

	public void calcSOVvalues() {
		// TODO Auto-generated method stub
		calcSegments();
	}

	public void calcDoubleSegments() {
		int i = 0;
		int lastindex = 0;
		for (int j = 0; j < observed_segments.size(); j++) {
			Segment obsseg = observed_segments.get(j);
			for (i = lastindex; i < predicted_segments.size(); i++) {
				Segment tmp = predicted_segments.get(i);
				if (obsseg.startpos <= tmp.endpos
						&& obsseg.startpos >= tmp.startpos
						&& obsseg.ch == tmp.ch) {

					// segment predicted links
					int identity = obsseg.startpos * tmp.startpos;
					dbl_segments.add(new DoubleSegment(identity, obsseg, tmp));

					// lastindex um position für nächsten obsseg segment zu
					// speichern und nicht wieder durch alle elemente davor
					// durchiterieren
					lastindex = i;
				} else if (obsseg.endpos >= tmp.startpos
						&& obsseg.endpos <= tmp.endpos && obsseg.ch == tmp.ch) {

					// segment predicted rechts
					int identity = obsseg.startpos * tmp.startpos;
					dbl_segments.add(new DoubleSegment(identity, obsseg, tmp));

					// lastindex
					lastindex = i;
				} else if (obsseg.startpos <= tmp.startpos
						&& obsseg.endpos >= tmp.endpos && obsseg.ch == tmp.ch) {

					// segment innerhalb
					int identity = obsseg.startpos * tmp.startpos;
					dbl_segments.add(new DoubleSegment(identity, obsseg, tmp));
				}

				// in while schöner... dennoch zu faul das jetzt umzuändern
				if (obsseg.endpos < tmp.startpos)
					i = predicted_segments.size() + 1;
			}
		}
	}

	// segmente mit bindestrich nur vorne und hinten entfernen
	public void deleteHyphenSegments() {
		// erstes element
		if (predicted_segments.get(0).ch == '-')
			predicted_segments.remove(0);

		// letztes element
		if (predicted_segments.get(predicted_segments.size() - 1).ch == '-')
			predicted_segments.remove(predicted_segments.size() - 1);
	}
}
