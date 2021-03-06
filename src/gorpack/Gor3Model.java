package gorpack;

import java.io.FileNotFoundException;

public class Gor3Model {
	// C -> 0 E -> 1 H -> 2
	// Model -> (0...oo)
	// Matrix -> loged
	final static int nstates = 3;
	final static int naa = 21;
	final static int windowsize = 17;
	public int type = 3;
	public final static String head = "// Matrix4D";
	int whalf = (int) Math.floor(windowsize / 2.0);
	int[][][][] model;
	// Windowsize flexibel
	double[][][][] matrix;
	int[] numss = { 1, 1, 1 };

	public String readfile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	public Gor3Model() {
		this.model = new int[nstates][naa][naa][windowsize];
		this.matrix = new double[nstates][naa][naa][windowsize];
		for (int i = 0; i < nstates; i++) {
			for (int j = 0; j < naa; j++) {
				for (int k = 0; k < naa; k++) {
					for (int l = 0; l < windowsize; l++) {
						this.model[i][j][k][l] = 0;
					}
				}
			}
		}
	}

	// works

	public static int[] summatrix(int[][][][] mod) {
		int[] ct = new int[3];
		for (int i = 0; i < nstates; i++) {
			for (int j = 0; j < naa; j++) {
				for (int k = 0; k < naa; k++) {
					for (int l = 0; l < windowsize; l++) {
						ct[i] += mod[i][j][k][l];
					}
				}
			}
		}
		return ct;
	}

	public static int[] summatrix(int[][][][] mod, int aa) {
		int[] ct = new int[3];
		for (int i = 0; i < nstates; i++) {
			for (int k = 0; k < naa; k++) {
				for (int l = 0; l < windowsize; l++) {
					ct[i] += mod[i][aa][k][l];
				}
			}
		}
		return ct;
	}

	// Fills matrix according to training results
	public void makematrix(int[][][][] mod) {
		this.matrix = new double[nstates][naa][naa][windowsize];
		// int[] summ = summatrix(mod);
		for (int i = 0; i < nstates; i++) {
			for (int j = 0; j < naa - 1; j++) {
				int[] summ = summatrix(mod, j);
				for (int k = 0; k < naa - 1; k++) {
					for (int l = 0; l < windowsize; l++) {
						matrix[i][j][k][l] = Math.log((double) mod[i][j][k][l]
								/ (mod[0][j][k][l] + mod[1][j][k][l]
										+ mod[2][j][k][l] - mod[i][j][k][l]))
								+ Math.log((summ[0] + summ[1] + summ[2] - summ[i])
										/ (double) summ[i]);
					}
				}
			}
		}
	}

	// Trains Gor1 Model
	public void train(String prim, String sek) {
		// TODO Auto-generated method stub
		int[] ss = Useful.sstoint(sek);
		int[] ps = Useful.aatoint(prim);
		for (int i = whalf; i < prim.length() - whalf; i++) {
			for (int j = 0; j < windowsize; j++) {
				model[ss[i]][ps[i]][ps[i + j - 8]][j]++;
				numss[ss[i]]++;
			}
		}
		// numss[0] += Useful.countss(sek)[0];
		// numss[1] += Useful.countss(sek)[1];
		// numss[2] += Useful.countss(sek)[2];
		makematrix(this.model);
	}

	// Trains Gor1 Model based on file path
	public void train(String path) {
		try {
			Sequence[] data = Useful.filetosequence(path);
			for (int i = 0; i < data.length; i++) {
				this.train(data[i].getps(), data[i].getss());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * train model
	 * 
	 * @param data mit sequence
	 */
	public void train2(Sequence[] data) {
		for (int i = 0; i < data.length; i++) {
			this.train(data[i].getps(), data[i].getss());
		}
	}

	public String[] probabilities(int[] ps) {
		String[] r = new String[3];
		for (int i = whalf; i < ps.length - whalf; i++) {
			double[] p = prob(ps, i);
			r[0] = r[0] + "" + (int) (p[0] * 10);
			r[1] = r[1] + "" + (int) (p[1] * 10);
			r[2] = r[2] + "" + (int) (p[2] * 10);
		}
		// System.out.println("foobar");
		return r;
	}

	// Calculates most likely Secondary Structure
	public double[] prob(int[] ps, int pos) {
		double[] p = new double[4];
		double[] scores = new double[4];
		for (int i = 0; i < windowsize; i++) {
			if (i != whalf || true) {
				scores[0] += matrix[0][ps[pos]][ps[pos + i - whalf]][i];
				scores[1] += matrix[1][ps[pos]][ps[pos + i - whalf]][i];
				scores[2] += matrix[2][ps[pos]][ps[pos + i - whalf]][i];
			}
		}
		p[0] = (Math.exp(scores[0]) / (1.0 + Math.exp(scores[0])));
		p[1] = (Math.exp(scores[1]) / (1.0 + Math.exp(scores[1])));
		p[2] = (Math.exp(scores[2]) / (1.0 + Math.exp(scores[2])));
		if (p[0] > p[1] && p[0] > p[2])
			p[3] = 0;
		else if (p[1] > p[0] && p[1] > p[2])
			p[3] = 1;
		else if (p[2] > p[0] && p[2] > p[1])
			p[3] = 2;
		else
			p[3] = 3;
		return p;
	}

	public int[] predict(int[] ps) {
		if (ps.length < windowsize)
			Useful.tooshort();
		int[] r = new int[ps.length];
		for (int x = 0; x < whalf; x++) {
			r[x] = 3;
			r[ps.length - x - 1] = 3;
		}
		for (int i = whalf; i < ps.length - whalf; i++) {
			double[] p = prob(ps, i);
			r[i] = (int) p[3];
			// System.out.println("foobar");
		}

		return r;
	}

	public String predictStringProbs(String ps) {
		int[] foo = Useful.aatoint(ps);
		int[] num = predict(foo);
		String[] probs = probabilities(foo);
		// System.out.println(num[1]);
		return "PH --------" + probs[2].substring(4) + "--------\n"
				+ "PE --------" + probs[1].substring(4) + "--------\n"
				+ "PC --------" + probs[0].substring(4) + "--------";
	}

	public String predictStringProbsHtml(String ps) {
		int[] foo = Useful.aatoint(ps);
		int[] num = predict(foo);
		String[] probs = probabilities(foo);
		// System.out.println(num[1]);
		return "PH --------" + probs[2].substring(4) + "--------<br>"
				+ "PE --------" + probs[1].substring(4) + "--------<br>"
				+ "PC --------" + probs[0].substring(4) + "--------";
	}

	// public int[][] probabilities(int[] ps){
	// int[][] r = new int[ps.length][nstates];
	// for(int i = 8; i < ps.length-8; i++){
	// double[] p = prob(ps, i);
	// for(int j = 0; j < nstates; j++){
	// r[i][j] = (int) (p[j] / p[0]+p[1]+p[2]+p[3]) * 10;
	// }
	// }
	// //System.out.println("foobar");
	// return r;
	// }

	public String predictString(int[] ps) {
		int[] num = predict(ps);
		return Useful.makess(num);
	}

	public String predictString(String ps) {
		int[] foo = Useful.aatoint(ps);
		int[] num = predict(foo);
		// System.out.println(num[1]);
		return Useful.makess(num);
	}

	/*
	 * public int[][] predictProbs(String ps){ int[][] res = new
	 * int[3][ps.length()]; int[] foo = Useful.aatoint(ps); for(int i = 0; i <
	 * ps.length(); i++){ res[0][i] = probabilities(foo)[0][i]; res[1][i] =
	 * probabilities(foo)[1][i]; res[2][i] = probabilities(foo)[2][i]; } return
	 * res; }
	 */

	/*
	 * public String[] predictProbsString(String ps){ int[][] foo =
	 * predictProbs(ps); String[] ret = new String[3]; String c = ""; String e =
	 * ""; String h = ""; for(int i = 0; i < foo[0].length; i++){ c = c +
	 * String.valueOf(foo[0][i]); e = e + String.valueOf(foo[1][i]); h = h +
	 * String.valueOf(foo[2][i]); } ret[0] = c; ret[1] = e; ret[2] = h; return
	 * ret; }
	 */

	public void writefile() {
		// TODO Auto-generated method stub
	}

	public int[][][][] getmodel() {
		return this.model;
	}

	public double[][][][] getmatrix() {
		return this.matrix;
	}

	public void setmodel(int[][][][] arg) {
		this.model = new int[nstates][naa][naa][windowsize];
		for (int i = 0; i < nstates; i++) {
			for (int j = 0; j < naa; j++) {
				for (int k = 0; k < naa; k++) {
					for (int l = 0; l < windowsize; l++) {
						this.model[i][j][k][l] = arg[i][j][k][l];
					}
				}
			}
		}
	}

	public void setmatrix(int[][][][] arg) {
		for (int i = 0; i < nstates; i++) {
			for (int j = 0; j < naa; j++) {
				for (int k = 0; k < naa; k++) {
					for (int l = 0; l < windowsize; l++) {
						this.model[i][j][k][l] = arg[i][j][k][l];
					}
				}
			}
		}
	}

}
