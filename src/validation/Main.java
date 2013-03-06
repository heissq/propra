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
		boolean is_cross_validation = false;
		boolean is_html = false;
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
			System.out.println("is cross validation");
			is_cross_validation = true;
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
				summary_file = summary_file.replaceAll(".txt", ".html");
				System.out.println(detailed_file+";"+summary_file);
				is_html = true;
			} else {
				System.out.println("Default Value = "+detailed_file+";"+summary_file);
			}
		}

		ReadFromFiles.readToData(predictions_file, dataset,false);
		ReadFromFiles.readToData(dssp_file, dataset,true);
		dataset.calcSummaryStatistics();
		if (!is_cross_validation) {
			ArrayList<Data> data_package = dataset.getDataPackage();
			CreateSummary csum = new CreateSummary("example",detailed_file);
			
			//berechnung der werte...
			dataset.calcQ3values();
			dataset.calcSOVvalues(8);
			dataset.calcSummaryStatistics();
			
			if (is_html){
				csum.createDetailedFileHtml(data_package, detailed_file, false);
				csum.createSummaryFileHtml(dataset, summary_file);
			} else {
				csum.createDetailedFileTxt(data_package, detailed_file, false);
				csum.createSummaryFileTxt(dataset, summary_file);
			}
		} else {
			//Cross validation
			/**
			 * letzter parameter gibt an wie viele k als testset benutzt werden sollen
			 */
			CrossValidation cv = new CrossValidation(dataset,iterations_cross_validation,1);
			// true wenn gor3 benutzt werden soll sonst nur gor1
			// und gor1 noch fehler...
			cv.repeatedCV(1, false, summary_file,detailed_file,is_html);
		}
	}
}
