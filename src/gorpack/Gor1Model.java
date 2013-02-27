package gorpack;

import java.io.FileNotFoundException;

public class Gor1Model implements GorModel {
// C -> 0 E -> 1 H -> 2
	// Model -> (0...oo)
	// Matrix -> loged
	final static int nstates = 3;
	final static int naa = 21;
	final static int windowsize = 17;
	public final static String head = "// Matrix3D";
	int whalf = (int) Math.floor(windowsize/2.0);
	int[][][] model = new int[nstates][naa][windowsize];
	//Windowsize flexibel
	double[][][] matrix = new double[nstates][naa][windowsize];
	int[] numss = {1,1,1};
	public String readfile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Gor1Model(){
		this.model = new int[nstates][naa][windowsize];
		this.matrix = new double[nstates][naa][windowsize];
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < windowsize; k++) {
					this.model[i][j][k] = 0;
				}
			}
		}
	}
//works
	
	public static int[] summatrix(int[][][] mod){
		int[] ct = new int[3];
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < windowsize; k++) {
					ct[i] += mod[i][j][k];
				}
			}
		}
		return ct;
	}
	
	// Fills matrix according to training results
	public void makematrix(int[][][] mod){
		int[] summ = summatrix(mod);
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < windowsize; k++) {
					matrix[i][j][k] =  Math.log((double)mod[i][j][k]/(mod[0][j][k] + mod[1][j][k] + mod[2][j][k] - mod[i][j][k])) +  Math.log((summ[0]+summ[1]+summ[2]-summ[i])/(double)summ[i]);
				}
			}
		}
	}
	//Trains Gor1 Model
	@Override
	public void train(String prim, String sek) {
		// TODO Auto-generated method stub
		int[] ss = Useful.sstoint(sek);
		int[] ps = Useful.aatoint(prim);
		for(int i = whalf; i < prim.length()-whalf; i++){
			for(int j = 0; j < windowsize; j++){
				model[ss[i]][ps[i+j-8]][j]++;
				numss[ss[i]]++;
			}
		}
		//numss[0] += Useful.countss(sek)[0];
		//numss[1] += Useful.countss(sek)[1];
		//numss[2] += Useful.countss(sek)[2];
		makematrix(this.model);
	}
	//Trains Gor1 Model based on file path
	public void train(String path){
		try {
			Sequence[] data = Useful.filetosequence(path);
			for(int i = 0; i < data.length; i++){
				this.train(data[i].getps(), data[i].getss());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(numss[0]);
		System.out.println(numss[1]);
		System.out.println(numss[2]);
		
	}
	
	//Calculates most likely Secondary Structure
	public double[] prob(int[] ps, int pos){
		double[] p = new double[4];
		for(int i = 0; i < windowsize; i++){
			p[0] += matrix[0][ps[pos+i-whalf]][i];
			p[1] += matrix[1][ps[pos+i-whalf]][i];
			p[2] += matrix[2][ps[pos+i-whalf]][i];
		}
		if(p[0] > p[1] && p[0] > p[2]) p[3] = 0;
		else if(p[1] > p[0] && p[1] > p[2]) p[3] = 1;
		else if(p[2] > p[0] && p[2] > p[1]) p[3] = 2;
		else p[3] = 3;
		return p;
	}
	
	public int[] predict(int[] ps){
		int[] r = new int[ps.length];
		for(int i = 8; i < ps.length-8; i++){
			double[] p = prob(ps, i);
			r[i] = (int) p[3];
			//System.out.println("foobar");
		}
		
		return r;
	}
	
	public String predictString(int[] ps){
		int[] num = predict(ps);
		return Useful.makess(num);
	}
	
	public String predictString(String ps){
		int[] foo = Useful.aatoint(ps);
		int[] num = predict(foo);
		//System.out.println(num[1]);
		return Useful.makess(num);
	}
	
	@Override
	public void writefile() {
		// TODO Auto-generated method stub
	}
	
	public int[][][] getmodel(){
		return this.model;
	}
	
	public double[][][] getmatrix(){
		return this.matrix;
	}
	
	public void setmodel(int[][][] arg){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j<20; j++){
				for(int k = 0; k<17; k++){
					this.model[i][j][k] = arg[i][j][k];
				}
			}
		}
	}
	
	public void setmatrix(int[][][] arg){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j<20; j++){
				for(int k = 0; k<17; k++){
					this.matrix[i][j][k] = arg[i][j][k];
				}
			}
		}
	}
	
}
