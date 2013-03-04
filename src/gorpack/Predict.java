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
		//everything fine
		Options opt = new Options();
		opt.addOption("", "probabilities", false, "Print Probabilities?");
		opt.addOption("", "model", true, "The model file to be used");
		opt.addOption("", "format", true, "The output format");
		opt.addOption("", "seq", true, "Sequence input");
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);
		String topred = "";
		String filename = "";
		boolean html = false;
		boolean probs = false;
		
		if(cmd.hasOption("probabilities")){
			probs = true;
		} else {
			System.out.println("What's this?");
		}
		
		if(cmd.hasOption("model")){
			filename = cmd.getOptionValue("model");
		} else {
			System.out.println("Y U no select model");
		}
		
		if(cmd.hasOption("format")){
			if(cmd.getOptionValue("format").equals("html")) html = true;
		} else {
			System.out.println("Aint nobody got time fo' dat");
		}
		
		if(cmd.hasOption("seq")){
			topred = cmd.getOptionValue("seq");
		} else {
			System.out.println("U stupid?");
		}
		
		int type = Useful.type(filename);
		Sequence[] prim = Useful.filetosequence(topred);
		Sequence p = new Sequence();
		String reality = "";
		String probabilities = "";
		String probabilitiesHtml = "";
		if(type == 4) {
			Gor3Model g = new Gor3Model();
			int[][][][] modelarr = Useful.read3model(filename);
			g.setmodel(modelarr);
			g.makematrix(modelarr);
			String prediction = g.predictString(prim[0].getps());
			reality = prim[0].getss();
			probabilities = g.predictStringProbs(prim[0].getps());
			probabilitiesHtml = g.predictStringProbsHtml(prim[0].getps());
			p = new Sequence(prim[0].getid(), prim[0].getps(), prediction);
		} else {
			Gor1Model g = new Gor1Model();
			int[][][] modelarr = Useful.readmodel(filename);
			g.setmodel(modelarr);
			g.makematrix(modelarr);
			String prediction = g.predictString(prim[0].getps());
			reality = prim[0].getss();
			probabilities = g.predictStringProbs(prim[0].getps());
			probabilitiesHtml = g.predictStringProbsHtml(prim[0].getps());
			p = new Sequence(prim[0].getid(), prim[0].getps(), prediction);
		}
		//String[] pvalues = g.predictProbsString(prim[0].getps());
		String content = "";
		if(reality.length() < 17){
			content = Useful.htmlstring(p);
		}
		else {
		content = Useful.htmlstring(p) + "" + "RS --------"+ reality.substring(8, reality.length()-8) + "--------";
		} 
		String content2 = Useful.htmlstring(p) + probabilitiesHtml;

		if(probs && !html){
		content = content + probabilities;
		System.out.println(Useful.makefastastring(p) + probabilities);
			//System.out.println(Useful.makefastastring(p, pvalues));
		}
		else if(!probs && !html) {
		System.out.print(Useful.makefastastring(p));
		System.out.println("RS --------"+ reality.substring(8, reality.length()-8) + "--------");
		}
		else if(!probs && html){
			System.out.println(Useful.htmlcode(content));
		}
		else {
			System.out.println(Useful.htmlcode(content2));
		}
	}
}
