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
		int len = prim.length;
		for(int i = 0; i < len; i++){
			if(Useful.filetosequence(topred)[i] == null || Useful.filetosequence(topred)[i].getid().equals("foo")) i = 10000;
			else prim[i] = Useful.filetosequence(topred)[i];
		}
		Sequence[] p = new Sequence[len];
		for(int i = 0; i < len; i++){
			p[i] = new Sequence();
		}
		System.out.println(len);
		String[] reality = new String[len];
		String[] probabilities = new String[len];
		String[] probabilitiesHtml =new String[len];
		String[] prediction = new String[len];
		if(type == 4) {
			Gor3Model g = new Gor3Model();
			int[][][][] modelarr = Useful.read3model(filename);
			g.setmodel(modelarr);
			g.makematrix(modelarr);
			for(int i = 0; i < len; i ++){
				if(prim[i].getid() == "foo") i = len;
				else {
					prediction[i] = g.predictString(prim[i].getps());
					reality[i] = prim[i].getss();
					probabilities[i] = g.predictStringProbs(prim[0].getps());
					probabilitiesHtml[i] = g.predictStringProbs(prim[0].getps());
					p[i] = new Sequence(prim[i].getid(), prim[i].getps(), prediction[i]);
				}
			}
		} else {
			Gor1Model g = new Gor1Model();
			int[][][] modelarr = Useful.readmodel(filename);
			g.setmodel(modelarr);
			g.makematrix(modelarr);
			for(int i = 0; i < len; i ++){
				if(prim[i].getid() == "foo") i = len;
				else {
					prediction[i] = g.predictString(prim[i].getps());
					reality[i] = prim[i].getss();
					probabilities[i] = g.predictStringProbs(prim[0].getps());
					probabilitiesHtml[i] = g.predictStringProbs(prim[0].getps());
					p[i] = new Sequence(prim[i].getid(), prim[i].getps(), prediction[i]);
				}
			}
		}
		//String[] pvalues = g.predictProbsString(prim[0].getps());
		String content = "";
		String content2 = "";
		for(int i = 0; i < len; i++){
			if(prim[i].getid() == "foo") i = len;
			else {
				if(reality[i].length() < 17){
					System.out.println("Sequence must be at least 17 bases long");
					i = len-1;
				}
				else {
					content += Useful.htmlstring(p[i]) + "" + "RS --------"+ reality[i].substring(8, reality[i].length()-8) + "--------";
				} 
				content2 += Useful.htmlstring(p[i]) + probabilitiesHtml[i];
				if(probs && !html){
					content = content + probabilities[i];
					System.out.println(Useful.makefastastring(p[i]) + probabilities[i]);
					//System.out.println(Useful.makefastastring(p, pvalues));
				}
				else if(!probs && !html) {
					System.out.print(Useful.makefastastring(p[i]));
					if(reality[i] == null || reality[i].equals(""));
					else System.out.println("RS --------"+ reality[i].substring(8, reality[i].length()-8) + "--------");
				}
			}
			}
		if(!probs && html){
		System.out.println(Useful.htmlcode(content));
		}
		else if (probs && html)
		{
			System.out.println(Useful.htmlcode(content2));
		}
	}
}
