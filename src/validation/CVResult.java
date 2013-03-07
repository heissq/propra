package validation;

public class CVResult extends Result {
	// für cv oder unterschiedliche typen von ergebnissen basierend
	public int iteration = 0;
	public int shuffle_set_iteration = 0;

	public CVResult() {
	}

	public CVResult(int iteration, int iterationshuffle, double q3, double sov,
			double qh, double qe, double qc, double sovh, double sove,
			double sovc) {
		super(q3, sov, qh, qe, qc, sovh, sove, sovc);
		this.iteration = iteration;
		this.shuffle_set_iteration = iterationshuffle;
	}
}
