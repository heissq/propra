package validation;

import java.text.DecimalFormat;
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
	private ArrayList<Segment> nonoverlapping_segments = new ArrayList<>();

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
		result = new Result();

		int count = 0;
		while (ps.charAt(count) == '-') {
			count++;
		}
		calcSOVvalues(count);
		calcQ3values();
	}

	// hier die ergebnisse mit einfügen
	public void addResult(double q3, double sov) {
		if (result == null)
			result = new Result();
		result.q3 = q3;
		result.sov = sov;
	}

	public void addResult(double q3, double qh, double qe, double qc) {
		if (result == null)
			result = new Result();
		result.q3 = q3;
		result.qh = qh;
		result.qe = qe;
		result.qc = qc;
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
		this.addResult(q.q3result, q.qhresult, q.qeresult, q.qcresult);
	}

	public void printQ3() {
		String br = "-----------------------------------------------------------------";
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println(br);
		System.out.println("|\tQ3\t|\tQH\t|\tQE\t|\tQC\t|");
		System.out.println("|\t" + df.format(result.q3) + "\t|\t"
				+ df.format(result.qh) + "\t|\t"
 + df.format(result.qe) + "\t|\t"
 + df.format(result.qc) + "\t|");
		System.out.println(br);

	}

	public void calcSegments(int windowsize) {
		// observed segments
		char lastc = 'X';
		Segment tmp = null;
		for (int i = windowsize; i < rs.length() - windowsize; i++) {
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
			if (i == rs.length() - 1 - windowsize)
				observed_segments.add(tmp);
		}
		
		// predicted segments
		lastc = 'X';
		tmp = null;
		for (int i = windowsize; i < ps.length() - windowsize; i++) {
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
			if (i == rs.length() - 1 - windowsize)
				predicted_segments.add(tmp);
		}
		// deleteHyphenSegments();
		// deleteObsSegmentsOppositeofHyphens();
		calcDoubleSegments();
		calcNonOverlapSegments();
	}	
	
	public void printSegments() {
		System.out.println("Segments of observed:");
		for (Segment sobs : observed_segments) {
			System.out.println(sobs);
		}
		
		System.out.println("Segments of predicted:");
		for (Segment pobs : predicted_segments) {
			System.out.println(pobs);
		}

		System.out.println("Segments double:");
		for (DoubleSegment dbseg : dbl_segments) {
			System.out.println(dbseg);
		}

		System.out.println("non overlapping Segments in observed sequence:");
		for (Segment seg : nonoverlapping_segments) {
			System.out.println(seg);
		}
	}

	public void calcSOVvalues(int windowsize) {
		calcSegments(windowsize);
		SOV sov = new SOV(dbl_segments, nonoverlapping_segments);
		result.sov = sov.resultscore;
	}

	public void printSOVscore() {
		String br = "-----------------------------------------------------------------";
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println(br);
		System.out.println("|\tSOV\t|\tSOVH\t|\tSOVE\t|\tSOVC\t|");
		System.out.println("|\t" + df.format(result.sov) + "\t|\t"
				+ df.format(result.sov_h) + "\t|\t" + df.format(result.sov_e)
				+ "\t|\t" + df.format(result.sov_c) + "\t|");
		System.out.println(br);
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
					obsseg.overlap = true;

					// lastindex um position für nächsten obsseg segment zu
					// speichern und nicht wieder durch alle elemente davor
					// durchiterieren
					lastindex = i + 1;
				} else if (obsseg.endpos >= tmp.startpos
						&& obsseg.endpos <= tmp.endpos && obsseg.ch == tmp.ch) {

					// segment predicted rechts
					int identity = obsseg.startpos * tmp.startpos;
					dbl_segments.add(new DoubleSegment(identity, obsseg, tmp));
					obsseg.overlap = true;

					// lastindex
					lastindex = i + 1;
				} else if (obsseg.startpos <= tmp.startpos
						&& obsseg.endpos >= tmp.endpos && obsseg.ch == tmp.ch) {
					obsseg.overlap = true;

					// segment innerhalb
					int identity = obsseg.startpos * tmp.startpos;
					dbl_segments.add(new DoubleSegment(identity, obsseg, tmp));
				} else if (obsseg.startpos > tmp.startpos
						&& obsseg.endpos < tmp.endpos) {
					obsseg.overlap = true;

					// segment alles
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

	public void deleteObsSegmentsOppositeofHyphens() {
		int len = observed_segments.size();
		int windowsize = 8;
		int size = len > windowsize ? windowsize : len;

		// erste elemente
		for (int i = 0; i < (len > windowsize ? windowsize : len); i++) {
			Segment obs = observed_segments.get(i);
			if (obs.endpos < windowsize) {
				observed_segments.remove(i);
				len = observed_segments.size();
			}
		}

		// letzte elemente
		for (int i = len - 1; i > (len > windowsize ? len - windowsize : len); i--) {
			Segment obs = observed_segments.get(i);
			if (obs.startpos > len - (windowsize + 1)) {
				observed_segments.remove(i);
				len = observed_segments.size();
			}
		}
	}
	// ArrayList für nonoverlapping segments
	public void calcNonOverlapSegments() {
		for (Segment seg : observed_segments) {
			if (!seg.overlap)
				nonoverlapping_segments.add(seg);
		}
	}

	public void printeverything() {
		System.out.println(pdbid);
		System.out.println("AS " + as);
		System.out.println("RS " + rs);
		System.out.println("PS " + ps);
		printSegments();
		if (result.sov != -1)
			printSOVscore();
		if (result.q3 != -1)
			printQ3();
	}
}
