package gorpack;

public class Gor1Model implements GorModel {
// C -> 0 E -> 1 H -> 2
	// Model -> (0...oo)
	// Matrix -> loged
	int[][][] model = new int[3][20][17];
	int[][][] matrix = new int[3][20][17];
	int[] numss = {1,1,1};
	public String readfile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Gor1Model(){
		this.model = new int[3][20][17];
		this.matrix = new int[3][20][17];
	}
//works
	public int[] countss(String ss){
		int[] r = {0,0,0};
		for(int i = 0; i<ss.length(); i++){
			if(ss.charAt(i) == 'C') r[0]++;
			else if(ss.charAt(i) == 'E') r[1]++;
			else if (ss.charAt(i) == 'H') r[2]++;
		}
		return r;
	}
	// converts AA to integer; works
	public int aaint(char aa){
		if(aa == 'A') return 0;
		if(aa == 'R') return 1;
		if(aa == 'N') return 2;
		if(aa == 'D') return 3;
		if(aa == 'C') return 4;
		if(aa == 'Q') return 5;
		if(aa == 'E') return 6;
		if(aa == 'G') return 7;
		if(aa == 'H') return 8;
		if(aa == 'I') return 9;
		if(aa == 'L') return 10;
		if(aa == 'K') return 11;
		if(aa == 'M') return 12;
		if(aa == 'F') return 13;
		if(aa == 'P') return 14;
		if(aa == 'S') return 15;
		if(aa == 'T') return 16;
		if(aa == 'W') return 17;
		if(aa == 'Y') return 18;
		if(aa == 'V') return 19;
		else return 999;
	}
	// Convert Secondary Structure Element to integer; only needed for training loop
	public int[] sstoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i < aa.length(); i++){
			if(aa.charAt(i) == 'C'){ret[i] = 0;}
			if(aa.charAt(i) == 'E'){ret[i] = 1;}
			if(aa.charAt(i) == 'H'){ret[i] = 2;}
		}
		return ret;
	}
	// Convert One-letter Code to integer
	public int[] aatoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i< aa.length(); i++){
			ret[i] = aaint(aa.charAt(i));
		}
		return ret;
	}
	// Fills matrix according to training results
	public void makematrix(int[][][] foo){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 20; j++){
				for(int k = 0; k < 17; k++) {
					matrix[i][j][k] = (int) (Math.log(model[i][j][k]/(model[0][j][k] + model[1][j][k] + model[2][j][k] - model[i][j][k])) + Math.log(numss[i]/(numss[0]+numss[1]+numss[2]-numss[i])));
				}
			}
		}
	}
	@Override
	public void train(String prim, String sek) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 20; j++){
				for(int k = 0; k < 17; k++) {
					model[i][j][k] = 1;
				}
			}
		}
		int[] ss = sstoint(sek);
		int[] ps = aatoint(prim);
		for(int i = 8; i < prim.length()-9; i++){
			for(int j = 0; j < 17; j++){
				model[ss[i]][ps[i+j-8]][j]++;
			}
		}
		this.makematrix(model);
		numss[0] += countss(sek)[0];
		numss[1] += countss(sek)[1];
		numss[2] += countss(sek)[2];
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

}
