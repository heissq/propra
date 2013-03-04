package gorpack;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TrainPredict {

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
		Sequence p = new Sequence();
		//String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		//String filename = "/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test.txt";
		// TODO Auto-generated method stub
		
		if(type == 3){
			Gor3Model g = new Gor3Model();
			g.train(database);
			String prediction = g.predictString(prim[0].getps());
			reality = prim[0].getss();
			p = new Sequence(prim[0].getid(), prim[0].getps(), prediction);
			probabilities = g.predictStringProbs(prim[0].getps());
			probabilitiesHtml = g.predictStringProbsHtml(prim[0].getps());
			
		}
		else{
			Gor1Model g = new Gor1Model();
			g.train(database);
			String prediction = g.predictString(prim[0].getps());
			reality = prim[0].getss();
			p = new Sequence(prim[0].getid(), prim[0].getps(), prediction);
			probabilities = g.predictStringProbs(prim[0].getps());
			probabilitiesHtml = g.predictStringProbsHtml(prim[0].getps());
			}
		
		String content = "";
		if(reality.length() < 17){
			content = Useful.htmlstring(p);
		}
		else {
		content = Useful.htmlstring(p) + "" + "RS --------"+ reality.substring(8, reality.length()-8) + "--------";
		} 
		String content2 = Useful.htmlstring(p) + probabilitiesHtml;

		if(!probs){
			System.out.println(Useful.htmlcode(content));
		}
		else {
			System.out.println(Useful.htmlcode(content2));
		}
		
		}
	}
