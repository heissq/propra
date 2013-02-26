package gorpack;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Train {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		String filename = "";
		// TODO Auto-generated method stub
		Gor1Model g = new Gor1Model();
		PrintWriter out = new PrintWriter(filename);
		//g.train("MFKVYGYDSNIHKCVYCDNAKRLLTVKKQPFEFINIMPEKGVFDDEKIAELLTKLGRDTQIGLTMPQVFAPDGSHIGGFDQLREYFK", "CEEEEECCCCCCCCHHHHHHHHHHHHCCCCEEEEECCCECCECCHHHHHHHHHHHCCCCCCCCCCCEEECCCCCEEECHHHHHHHCC");
		g.train(p);
		String s = "";
		for(int i = 0; i < 1; i++){
			for(int j = 0; j < 21; j++){
				for (int k = 0; k < 17; k++){
					out.print(g.matrix[i][j][k] + "\t");
				}
				out.println();
			}
			out.println();
		}
		
		
		//int[] bar = {3,3,8,16,8,2,5,7,1,0,3,1,1,2,2,3,4,4,4,4};
		//int[] foobar = g.predict(bar);
		//System.out.println(foobar[1]);
		//System.out.println(g.matrix[0][0][0]);
	}

}
