package gorpack;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.cli.*;

public class Train {
	public final static int aa = 21;
	public final static int windowsize = 17;
	public final static String head = "// Matrix3D";

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		Options opt = new Options();
		opt.addOption("", "db", true, "The File used for Training");
		opt.addOption("", "method", true, "The method used for Training");
		opt.addOption("", "model", true, "The model file to be created");
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);
		if(cmd.hasOption("db")){
			String p = cmd.getOptionValue("db");
		}
		if(cmd.hasOption("method")){}
		if(cmd.hasOption("model")){
			String filename = cmd.getOptionValue("model");
		}
		String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		String filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
		// TODO Auto-generated method stub
		Gor1Model g = new Gor1Model();
		g.train(p);
//		FileWriter pw = new FileWriter(filename);
//		//g.train("MFKVYGYDSNIHKCVYCDNAKRLLTVKKQPFEFINIMPEKGVFDDEKIAELLTKLGRDTQIGLTMPQVFAPDGSHIGGFDQLREYFK", "CEEEEECCCCCCCCHHHHHHHHHHHHCCCCEEEEECCCECCECCHHHHHHHHHHHCCCCCCCCCCCEEECCCCCEEECHHHHHHHCC");
//		g.train(p);
//		String s = "";
//		pw.write(head + "\n" + "\n");
//		for(int i = 0; i < 3; i++){
//			pw.write("=" + Useful.sschar(i) + "=" + "\n" + "\n");
//			for(int j = 0; j < aa-1; j++){
//				pw.write(Useful.aachar(j) + "\t");
//				for (int k = 0; k < windowsize; k++){
//					pw.write(g.model[i][j][k] + "\t");
//				}
//				pw.write("\n");
//			}
//			pw.write("\n");
//			pw.write("\n");
//		}
//		pw.flush();
//		pw.close();
		
		Useful.writemodelfile(filename, g);
	}

}
