package gorpack;

public class Gor1Model implements GorModel {
// C -> 0 E -> 1 H -> 2
	// Model -> (0...oo)
	// Matrix -> loged
	final static int nstates = 3;
	final static int naa = 20;
	final static int windowsize = 17;
	int whalf = (int) Math.floor(windowsize/2.0);
	int[][][] model = new int[nstates][naa][windowsize];
	//Windowsize flexibel
	int[][][] matrix = new int[nstates][naa][windowsize];
	int[] numss = {1,1,1};
	public String readfile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Gor1Model(){
		this.model = new int[nstates][naa][windowsize];
		this.matrix = new int[nstates][naa][windowsize];
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < windowsize; k++) {
					this.model[i][j][k] = 1;
				}
			}
		}
	}
//works
	// Fills matrix according to training results
	public void makematrix(int[][][] foo){
		for(int i = 0; i < nstates; i++){
			for(int j = 0; j < naa; j++){
				for(int k = 0; k < windowsize; k++) {
					matrix[i][j][k] = (int) (Math.log(model[i][j][k]/(model[0][j][k] + model[1][j][k] + model[2][j][k] - model[i][j][k])) + Math.log(numss[i]/(numss[0]+numss[1]+numss[2]-numss[i])));
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
		for(int i = whalf; i < prim.length()-9; i++){
			for(int j = 0; j < windowsize; j++){
				model[ss[i]][ps[i+j-8]][j]++;
				numss[ss[i]]++;
			}
		}
		this.makematrix(model);
		numss[0] += Useful.countss(sek)[0];
		numss[1] += Useful.countss(sek)[1];
		numss[2] += Useful.countss(sek)[2];
	}
	//Trains Gor1 Model based on file path
	public void train(String path){
		
	}
	
	//Calculates most likely Secondary Structure
	public int prob(int[] ps, int pos){
		int[] p = new int[3];
		for(int i = 0; i < windowsize; i++){
			p[0] += matrix[0][ps[pos+i-whalf]][i];
			p[1] += matrix[1][ps[pos+i-whalf]][i];
			p[2] += matrix[2][ps[pos+i-whalf]][i];
		}
		if(p[0] > p[1] && p[0] > p[2]) return 0;
		else if(p[1] > p[0] && p[1] > p[2]) return 1;
		else if(p[2] > p[0] && p[2] > p[1]) return 2;
		else return 3;
	}
	
	public int[] predict(int[] ps){
		int[] r = new int[ps.length];
		for(int i = 8; i < ps.length-8; i++){
			r[i] = prob(ps, i);
		}
		return r;
	}
	
	@Override
	public void writefile() {
		// TODO Auto-generated method stub
	}
	
	public int[][][] getmodel(){
		return this.model;
	}
	
	public int[][][] getmatrix(){
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
