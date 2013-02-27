package gorpack;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Predict {

	/**
	 * @param args
	 */
	public final static int aa = 21;
	public final static int windowsize = 17;
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		// TODO Auto-generated method stub
		//prim is generated
		//reading does not work here. reading cmd works for train. 15:54
		Options opt = new Options();
		Gor1Model g = new Gor1Model();
		opt.addOption("", "probabilities", true, "Print Probabilities?");
		opt.addOption("", "model", true, "The model file to be used");
		opt.addOption("", "format", true, "The output format");
		opt.addOption("", "seq", true, "Sequence input");
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);
		String topred = "";
		String filename = "";
		//String topred = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		//String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		//String filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
		if(cmd.hasOption("probabilities")){
		} else {
			System.out.println("Dont need this");
		}
		if(cmd.hasOption("model")){
			filename = cmd.getOptionValue("model");
		
		} else {
			System.out.println("Model not found");
		}
		if(cmd.hasOption("format")){
		} else {
			System.out.println("Dont need format");
		}
		if(cmd.hasOption("seq")){
			topred = cmd.getOptionValue("seq");
		} else {
			System.out.println("Sequence file not found");
		}
		
		//System.out.println(filename+topred);
		Sequence[] prim = Useful.filetosequence(topred);
		//System.out.println(prim[2].getps());
		int[][][] modelarr = Useful.readmodel(filename);
		
		//System.out.println(modelarr[0][0][0]);
		g.setmodel(modelarr);
		g.makematrix(modelarr);
		//System.out.println(prim[0].getps());
		String prediction = g.predictString(prim[0].getps());
		//System.out.println(prediction);
		Sequence p = new Sequence(prim[0].getid(), prim[0].getps(), prediction);
		System.out.println(Useful.makefastastring(p));
	}

}
