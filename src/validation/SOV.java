package validation;

import java.util.ArrayList;

public class SOV {
	private ArrayList<DoubleSegment> dbl_segments = new ArrayList<>();
	private ArrayList<Segment> non_overlap_segments = new ArrayList<>();
	public double resultscore;

	public SOV(ArrayList<DoubleSegment> dbl_segs, ArrayList<Segment> non_ov_segs) {
		this.dbl_segments = dbl_segs;
		this.non_overlap_segments = non_ov_segs;
		calcSOVvalues();
	}

	public void calcSOVvalues() {
		resultscore = (100 * (1 / sumOv_NonOv()) * sumOv());
	}

	private double sumOv_NonOv() {
		int sumov = 0;
		int sumnonov = 0;

		for (DoubleSegment dbseg : dbl_segments)
			sumov += dbseg.observed.endpos - dbseg.observed.startpos + 1;

		for (Segment seg : non_overlap_segments)
			sumnonov += seg.endpos - seg.startpos + 1;

		return sumov + sumnonov;
	}

	private double sumOv() {
		double sum = 0.0;
		for (DoubleSegment dbseg : dbl_segments) {
			int minov = -1;
			int maxov = -1;
			double del = -1;
			if (dbseg.observed.startpos <= dbseg.predicted.startpos
					&& dbseg.observed.endpos >= dbseg.predicted.endpos) {
				minov = dbseg.predicted.endpos - dbseg.predicted.startpos + 1;
				maxov = dbseg.observed.endpos - dbseg.observed.startpos + 1;
			} else if (dbseg.observed.startpos > dbseg.predicted.startpos
					&& dbseg.observed.endpos < dbseg.predicted.endpos) {
				minov = dbseg.observed.endpos - dbseg.observed.startpos + 1;
				maxov = dbseg.predicted.endpos - dbseg.predicted.startpos + 1;
			} else if (dbseg.observed.startpos > dbseg.predicted.startpos) {
				minov = dbseg.predicted.endpos - dbseg.observed.startpos + 1;
				maxov = dbseg.observed.endpos - dbseg.predicted.startpos + 1;
			} else {
				minov = dbseg.observed.endpos - dbseg.predicted.startpos + 1;
				maxov = dbseg.predicted.endpos - dbseg.observed.endpos + 1;
			}
			del = delta(minov, maxov,
 dbseg.observed.endpos
					- dbseg.observed.startpos + 1, dbseg.predicted.endpos
					- dbseg.predicted.startpos + 1);
			sum += ((minov + del) / maxov)
					* (dbseg.observed.endpos - dbseg.observed.startpos + 1);
		}
		return sum;
	}

	private double delta(int minov, int maxov, int s1l, int s2l) {
		double a = (double) Math.min(maxov - minov, minov);
		double b = (double) Math.min(Math.abs(s1l / 2), Math.abs(s2l / 2));
		return Math.min(a, b);
	}

	public void printSOVscore() {
		System.out.println(resultscore);
	}
}