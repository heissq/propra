package gorpack;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

public class Predict {

	/**
	 * @param args
	 */
	public final static int aa = 21;
	public final static int windowsize = 17;
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Options opt = new Options();
		CommandLineParser parser = new GnuParser();
		String p = "/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db";
		Sequence[] prim = Useful.filetosequence(p);
	}

}
