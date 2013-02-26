package gorpack;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.cli.*;

public class Train {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Options opt = new Options();
		CommandLineParser parser = new GnuParser();
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
	}

}
