package validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	public static String predictions_file;
	public static String dssp_file;
	public static String summary_file;
	public static String detailed_file;
	public static DataSet dataset;
	public static int iterations_cross_validation;

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException,
			IOException {
//		 variablen für optionen:
		String predictions_file = "predictions.txt";
		String dssp_file = "dssp.txt";
		String summary_file = "summary.txt";
		String detailed_file = "detailed_summary.txt";
		int iterations_cross_validation = 10;
		DataSet dataset = new DataSet();

		Options opt = new Options();
		opt.addOption("", "p", true, "<predictions>");
		opt.addOption("", "r", true, "<dssp-file>");
		opt.addOption("", "f", true, "<txt|html>");
		opt.addOption("", "s", true, "<summary file>");
		opt.addOption("", "d", true, "<detailed file>");
		opt.addOption("", "cross-validation", true, "<cross-validation>");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);

		// predictions file
		if (cmd.hasOption("p")) {
			predictions_file = cmd.getOptionValue("p");
			System.out.println(predictions_file);
		} else {
			System.out.println("Default value = predictions.txt");
		}

		// dssp file
		if (cmd.hasOption("r")) {
			dssp_file = cmd.getOptionValue("r");
			System.out.println(dssp_file);
		} else {
			System.out.println("Default value = dssp.txt");
		}

		// detailed file
		if (cmd.hasOption("d")) {
			detailed_file = cmd.getOptionValue("d");
			System.out.println(detailed_file);
		} else {
			System.out
					.println("Default value = detailed_summary.html/detailed_summary.txt");
		}
		
		//cross validation parameter
		if (cmd.hasOption("cross-validation")) {
			iterations_cross_validation = Integer.parseInt(cmd.getOptionValue("cross-validation"));
			System.out.println(iterations_cross_validation);
		} else {
			System.out
			.println("Default value = 10");
		}

		// summary file
		if (cmd.hasOption("s")) {
			summary_file = cmd.getOptionValue("s");
			System.out.println(summary_file);
		} else {
			System.out.println("Default value = summary.html/summary.txt");
		}

		if (cmd.hasOption("f")) {
			String tmp = cmd.getOptionValue("f");
			if (tmp.equals("html") || tmp.equals("html:")){
				detailed_file = detailed_file.replaceAll(".txt", ".html");
				System.out.println(detailed_file+";"+summary_file);
			} else {
				System.out.println("Default Value = "+detailed_file+";"+summary_file);
			}
		}

		ReadFromFiles.readToData(predictions_file, dataset);
		ReadFromFiles.readToData(dssp_file, dataset);

		// ausdrucken der elemente

		// System.out.println("warum");
//		 System.out.println(dataset);
		// System.out.println("----------");
		
//		 dataset.printids();
		
//		dataset.printeverything();
		dataset.printDataByPDBId("11asB00");
		ArrayList<Data> data_package = dataset.getDataPackage();
		
		CreateSummary csum = new CreateSummary("example",detailed_file);
		csum.createDetailedFileTxt(data_package, detailed_file, false);
	}
}
