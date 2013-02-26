package gorpack;

public class Gor1Predictor {

	public Gor1Model g;
	
	public Gor1Predictor(Gor1Model g){
		this.g = g;
	}
	
	public int[][][] model = g.getmodel();
	//public int[][][] matrix = g.getmatrix();
	
	public int[] pred;
	public int[] protseq;
	
	
}
