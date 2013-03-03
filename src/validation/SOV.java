package validation;

import java.util.ArrayList;

public class SOV {
	private ArrayList<DoubleSegment> dbl_segments = new ArrayList<>();
	private ArrayList<Segment> non_overlap_segments = new ArrayList<>();
	public double resultscore;
	public double sov_h;
	public double sov_e;
	public double sov_c;

	public SOV(ArrayList<DoubleSegment> dbl_segs, ArrayList<Segment> non_ov_segs) {
		this.dbl_segments = dbl_segs;
		this.non_overlap_segments = non_ov_segs;
		calcSOVvalues();
	}

	public void calcSOVvalues() {
		// main sovscore
		resultscore = (100 * (1 / sumOv_NonOv(dbl_segments,non_overlap_segments)) * sumOv(dbl_segments));

		// for 'H'
		//temp doublesegment arraylist
		ArrayList<DoubleSegment> dbs_h = new ArrayList<>();
		for (DoubleSegment dbs : dbl_segments) {
			if (dbs.observed.ch == 'H')
				dbs_h.add(dbs);
		}

		//temp segment arraylist
		ArrayList<Segment> non_h = new ArrayList<>();
		for (Segment s : non_overlap_segments) {
			if (s.ch == 'H')
				non_h.add(s);
		}

		//result
		sov_h = (100 * (1 / sumOv_NonOv(dbs_h, non_h)) * sumOv(dbs_h));

		// for 'E'
		//temp doublesegment arraylist
		ArrayList<DoubleSegment> dbs_e = new ArrayList<>();
		for (DoubleSegment dbs : dbl_segments) {
			if (dbs.observed.ch == 'E')
				dbs_e.add(dbs);
		}

		//temp segment arraylist
		ArrayList<Segment> non_e = new ArrayList<>();
		for (Segment s : non_overlap_segments) {
			if (s.ch == 'E')
				non_e.add(s);
		}

		//result
		sov_e = (100 * (1 / sumOv_NonOv(dbs_e, non_e)) * sumOv(dbs_e));

		// for 'C'
		//temp doublesegment arraylist
		ArrayList<DoubleSegment> dbs_c = new ArrayList<>();
		for (DoubleSegment dbs : dbl_segments) {
			if (dbs.observed.ch == 'C')
				dbs_c.add(dbs);
		}

		//temp segment arraylist
		ArrayList<Segment> non_c = new ArrayList<>();
		for (Segment s : non_overlap_segments) {
			if (s.ch == 'C')
				non_c.add(s);
		}

		//result
		sov_c = (100 * (1 / sumOv_NonOv(dbs_c, non_c)) * sumOv(dbs_c));
	}

	private double sumOv_NonOv(ArrayList<DoubleSegment> dbs,ArrayList<Segment> nonov) {
		int sumov = 0;
		int sumnonov = 0;

		for (DoubleSegment dbseg : dbs)
			sumov += dbseg.observed.endpos - dbseg.observed.startpos + 1;

		for (Segment seg : nonov)
			sumnonov += seg.endpos - seg.startpos + 1;

		return sumov + sumnonov;
	}

	private double sumOv(ArrayList<DoubleSegment> dbs) {
		double sum = 0.0;
		for (DoubleSegment dbseg : dbs) {
			double minov = Math.min(dbseg.observed.endpos,
					dbseg.predicted.endpos)
					- Math.max(dbseg.observed.startpos,
							dbseg.predicted.startpos) + 1;
			double maxov = Math.max(dbseg.observed.endpos,
					dbseg.predicted.endpos)
					- Math.min(dbseg.observed.startpos,
							dbseg.predicted.startpos) + 1;

			double del = delta(minov, maxov,
					dbseg.observed.endpos
					- dbseg.observed.startpos + 1, dbseg.predicted.endpos
					- dbseg.predicted.startpos + 1);
			sum += ((minov + del) / (double) maxov)
					* (dbseg.observed.endpos - dbseg.observed.startpos + 1);
		}
		return sum;
	}

	private double delta(double minov, double maxov, int s1l, int s2l) {
		double a = (double) Math.min(maxov - minov, minov);
		double b = (double) Math.min(Math.abs((double) s1l / 2),
				Math.abs((double) s2l / 2));
		return Math.min(a, b);
	}

	public void printSOVscore() {
		System.out.println(resultscore);
	}
}