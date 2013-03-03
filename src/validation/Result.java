package validation;


public class Result {
	// parameters to calculate:
	public double q3 = -1;
	public double sov = -1;
	public double qh = -1;
	public double qe = -1;
	public double qc = -1;
	public double sov_h = -1;
	public double sov_e = -1;
	public double sov_c = -1;

	public Result() {

	}

	public Result(double q3) {
		this.q3 = q3;
	}

	public Result(double q3, double sov) {
		this.q3 = q3;
		this.sov = sov;
	}
}
