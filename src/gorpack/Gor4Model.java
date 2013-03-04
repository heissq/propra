package gorpack;

import java.io.FileNotFoundException;

public class Gor4Model {
	final static int nstates = 3;
	final static int naa = 21;
	final static int windowsize = 17;
	public int type = 4;
	public final static String head = "// Matrix6D";
	int whalf = (int) Math.floor(windowsize/2.0);
	int[][][][][][] model;
	double[][][][][][] matrix;
	int[] numss = {1,1,1};
	//C,E,H -- Aminosäure -- Aminosäure an Stelle -- Stelle -- Aminosäure in Tabelle - Position
	

	public Gor4Model(){
		this.model = new int[nstates][naa][naa][windowsize][naa][windowsize];
		this.matrix = new double[nstates][naa][naa][windowsize][naa][windowsize];
		//BEGIN OF LOOP
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < naa; k++) {
					for(int l = 0; l < windowsize; l++){
						for(int n = 0; n < naa; n++){
							for(int p = 0; p < windowsize; p++){
								this.model[i][j][k][l][n][p] = 0;
							}
						}
					}
				}
			}
		}
	}
	//END OF LOOP
	
	public void train(String prim, String sek){
		int[] ss = Useful.sstoint(sek);
		int[] ps = Useful.aatoint(prim);
		for(int i = whalf; i < prim.length()-whalf; i++){
			for(int j = 0; j < windowsize; j++){
				for(int k = j+1; k < windowsize; k++){
				model[ss[i]][ps[i]][ps[i+j-whalf]][j][ps[i+k-whalf]][k]++;
				}
			}
		}
	}
	
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
		
	}
	//Anzahl der Vorkommen der Sekundärstrukturen im Modell für AA pspred in der Mitte, AA ps
	//an Position pos
	public int [] summatrix(int[][][][][][] model, int pspred, int ps, int pos){
		int[] ret = {1,1,1};
		for(int i = 0; i < naa; i++){
			for(int j = 0; j < windowsize; j++){
				if(true){
				ret[0]+= model[0][pspred][ps][pos][i][j];
				ret[1]+= model[1][pspred][ps][pos][i][j];
				ret[2]+= model[2][pspred][ps][pos][i][j];
				}
			}
		}
		return ret;
	}
	
	
	
	//Calculates most likely Secondary Structure
	public double[] prob(int[] ps, int pos){
		double[] p = new double[4];
		double[] r = new double[4];
		double[] q = new double[4];
		for(int g = 0; g < windowsize; g++){
			if(true){
				p[0] = 1.0;
				p[1] = 1.0;
				p[2] = 1.0;
			for(int i = g+1; i < windowsize; i++){
					p[0] += model[0][ps[pos]][ps[pos+g-whalf]][g][ps[pos+i-whalf]][i];
					p[1] += model[1][ps[pos]][ps[pos+g-whalf]][g][ps[pos+i-whalf]][i];
					p[2] += model[2][ps[pos]][ps[pos+g-whalf]][g][ps[pos+i-whalf]][i];
			}
			r[0] += Math.log(p[0]/(double)(p[1]+p[2]));
			r[1] += Math.log(p[1]/(double)(p[0]+p[2]));
			r[2] += Math.log(p[2]/(double)(p[1]+p[0]));
			q[0] += Math.log(summatrix(model, ps[pos], ps[pos+g-whalf], g)[0] / (double) (summatrix(model, ps[pos], ps[pos+g-whalf], g)[1] + summatrix(model, ps[pos], ps[pos+g-whalf], g)[2])) ;
			q[1] += Math.log(summatrix(model, ps[pos], ps[pos+g-whalf], g)[1] / (double) (summatrix(model, ps[pos], ps[pos+g-whalf], g)[0] + summatrix(model, ps[pos], ps[pos+g-whalf], g)[2])) ;
			q[2] += Math.log(summatrix(model, ps[pos], ps[pos+g-whalf], g)[2] / (double) (summatrix(model, ps[pos], ps[pos+g-whalf], g)[1] + summatrix(model, ps[pos], ps[pos+g-whalf], g)[0])) ;
			}
		}
		p[0] = (2.0/windowsize) * r[0] * (windowsize-2.0) / (windowsize) * q[0];
		p[1] = (2.0/windowsize) * r[1] * (windowsize-2.0) / (windowsize) * q[1];
		p[2] = (2.0/windowsize) * r[2] * (windowsize-2.0) / (windowsize) * q[2];
		if(p[0] > p[1] && p[0] > p[2]) p[3] = 0;
		else if(p[1] > p[0] && p[1] > p[2]) p[3] = 1;
		else if(p[2] > p[0] && p[2] > p[1]) p[3] = 2;
		else p[3] = 3;
		return p;
	}
	
	public int[] predict(int[] ps){
		if(ps.length < windowsize) Useful.tooshort();
		int[] r = new int[ps.length];
		for(int x = 0; x < whalf; x++){
			r[x] = 3;
			r[ps.length - x -1] = 3;
		}
		for(int i = whalf; i < ps.length-whalf; i++){
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
	public void setmodel(int[][][][][][] arg){
		this.model = new int[nstates][naa][naa][windowsize][naa][windowsize];
		//BEGIN OF LOOP
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < naa; k++) {
					for(int l = 0; l < windowsize; l++){
						for(int n = 0; n < naa; n++){
							for(int p = 0; p < windowsize; p++){
								this.model[i][j][k][l][n][p] = arg[i][j][k][l][n][p];
							}
						}
					}
				}
			}
		}
	}
	
}
