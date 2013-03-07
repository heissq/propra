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
	
	public Result(double q3, double sov, double qh, double qe, double qc, double sovh, double sove, double sovc) {
		this.q3 = q3;
		this.qh = qh;
		this.qe = qe;
		this.qc = qc;
		this.sov = sov;
		this.sov_h = sovh;
		this.sov_e = sove;
		this.sov_c = sovc;
	}
	
	@Override
	public String toString() {
		return q3+" "+sov+"\n";
	}
}
