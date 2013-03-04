package gorpack;

import java.io.IOException;

import javax.swing.text.AbstractDocument.Content;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TrainPredict2 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		Options opt = new Options();
		opt.addOption("", "db", true, "The File used for Training");
		opt.addOption("", "method", true, "The method used for Training");
		opt.addOption("", "probabilities", false, "Print Probabilities?");
		opt.addOption("", "seq", true, "Sequence input");
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);
		String database = "";
		String topred = "";
		String reality = "";
		String probabilities = "";
		String probabilitiesHtml = "";
		boolean probs = false;
		
		int type = 1;
		if(cmd.hasOption("db")){
				database = cmd.getOptionValue("db");
			} else {
				System.out.println("Select Database");
		}
		if(cmd.hasOption("method")){
			if (cmd.getOptionValue("method").equals("gor1")) {type = 1;}
			else if (cmd.getOptionValue("method").equals("gor3")) {type = 3;}
			}
			else {
				System.out.println("Default Method Gor1 selected");
		}
		if(cmd.hasOption("probabilities")){
			probs = true;
			} else {
			System.out.println("What's this?");
		}
		if(cmd.hasOption("seq")){
			topred = cmd.getOptionValue("seq");
			} else {
			System.out.println("U stupid?");
		}
		Sequence[] prim = Useful.filetosequence(topred);
		Sequence[] p = new Sequence[prim.length];
		//String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		//String filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
		// TODO Auto-generated method stub
		String content = "";
		String content2 = "";
		if(type == 3){
			Gor3Model g = new Gor3Model();
			g.train(database);
			for ( int i = 0; i<prim.length;i++){
				String prediction = g.predictString(prim[i].getps());
				reality = prim[i].getss();
				p[i] = new Sequence(prim[i].getid(), prim[i].getps(), prediction);
				}
		}
		else{
			Gor1Model g = new Gor1Model();
			g.train(database);
			for ( int i = 0; i<prim.length;i++){
				String prediction = g.predictString(prim[i].getps());
				reality = prim[i].getss();
				p[i] = new Sequence(prim[i].getid(), prim[i].getps(), prediction);
			}
		}
		
		}
	}
