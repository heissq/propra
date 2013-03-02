package validation;

import java.lang.ProcessBuilder.Redirect;

public class Result {
	// parameters to calculate:
	private double q3 = -1;
	private double sov = -1;
	private double qh = -1;
	private double qe = -1;
	private double qc = -1;
	private double sov_h = -1;
	private double sov_e = -1;
	private double sov_c = -1;

	public Result() {

	}

	public Result(double q3) {
		this.q3 = q3;
	}

	public Result(double q3, double sov) {
		this.q3 = q3;
		this.sov = sov;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getSov() {
		return sov;
	}

	public void setSov(double sov) {
		this.sov = sov;
	}

	public double getQh() {
		return qh;
	}

	public void setQh(double qh) {
		this.qh = qh;
	}

	public double getQe() {
		return qe;
	}

	public void setQe(double qe) {
		this.qe = qe;
	}

	public double getQc() {
		return qc;
	}

	public void setQc(double qc) {
		this.qc = qc;
	}

	public double getSov_h() {
		return sov_h;
	}

	public void setSov_h(double sov_h) {
		this.sov_h = sov_h;
	}

	public double getSov_e() {
		return sov_e;
	}

	public void setSov_e(double sov_e) {
		this.sov_e = sov_e;
	}

	public double getSov_c() {
		return sov_c;
	}

	public void setSov_c(double sov_c) {
		this.sov_c = sov_c;
	}
}
