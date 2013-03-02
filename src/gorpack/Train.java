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
			String p = "";
			String filename = "";
			int type = 1;
			if(cmd.hasOption("db")){
			p = cmd.getOptionValue("db");
			} else {
			System.out.println("Select Database");
			}
			if(cmd.hasOption("method")){
			switch(cmd.getOptionValue("method")){
			case "gor1": type = 1;
			case "gor3": type = 3;
			case "gor4": type = 4;
			}
			} else {
			System.out.println("Default Method Gor1 selected");
			}
			if(cmd.hasOption("model")){
			filename = cmd.getOptionValue("model");
			} else {
			filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
			System.out.println("foo");
			}
			//String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
			//String filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
			// TODO Auto-generated method stub
			
			if(type == 3){
				Gor3Model g = new Gor3Model();
				g.train(p);
				Useful.writemodelfile(filename, g);
			}
			else if(type == 4){
				Gor4Model g = new Gor4Model();
				g.train(p);
				Useful.writemodelfile(filename, g);
			}
			else{
				Gor1Model g = new Gor1Model();
				g.train(p);
				Useful.writemodelfile(filename, g);
				}
			}

}
