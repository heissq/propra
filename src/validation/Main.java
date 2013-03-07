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
	public static void main(String[] args) throws ParseException, IOException {
		// variablen für optionen:
		String predictions_file = "predictions.txt";
		String dssp_file = "dssp.txt";
		String summary_file = "summary.txt";
		String detailed_file = "detailed_summary.txt";
		String gnuoutput = "raw_values_validation.txt";
		boolean is_cross_validation = false;
		boolean is_html = false;
		boolean is_gor3 = false;
		boolean is_gnuplot = false;

		int iterations_cross_validation = 10;
		int shuffles = 1;
		DataSet dataset = new DataSet();

		Options opt = new Options();
		opt.addOption("", "p", true, "<predictions>");
		opt.addOption("", "r", true, "<dssp-file>");
		opt.addOption("", "f", true, "<txt|html>");
		opt.addOption("", "s", true, "<summary file>");
		opt.addOption("", "d", true, "<detailed file>");
		opt.addOption("", "n", true, "<number of splits>");
		opt.addOption("", "gor3-model", false, "<gor3-model>");
		opt.addOption("", "cv", false, "<cross-validation>");
		opt.addOption("", "gnu", true, "<plot-output>");
		opt.addOption("", "n-shuffles", true, "<n of shuffles>");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);

		// html oder nicht?
		if (cmd.hasOption("f")) {
			String tmp = cmd.getOptionValue("f");
			if (tmp.equals("html") || tmp.equals("html:")) {
				detailed_file = detailed_file.replaceAll(".txt", ".html");
				summary_file = summary_file.replaceAll(".txt", ".html");
				is_html = true;
			}
		}

		// predictions file
		if (cmd.hasOption("p")) {
			predictions_file = cmd.getOptionValue("p");
			System.out.println("predictionsFile:\n" + predictions_file);
		} else {
			System.out.println("Default value = predictions.txt");
		}

		// dssp file
		if (cmd.hasOption("r")) {
			dssp_file = cmd.getOptionValue("r");
			System.out.println("dsspFile:\n" + dssp_file);
		} else {
			System.out.println("Default value = dssp.txt");
		}

		// detailed file
		if (cmd.hasOption("d")) {
			detailed_file = cmd.getOptionValue("d");
			System.out.println(detailed_file);
		} else {
			System.out.println("Default value = "
					+ (is_html ? "detailed_summary.html"
							: "detailed_summary.txt"));
		}

		// summary file
		if (cmd.hasOption("s")) {
			summary_file = cmd.getOptionValue("s");
			System.out.println(summary_file);
		} else {
			System.out.println("Default value = "
					+ (is_html ? "summary.html" : "summary.txt"));
		}

		// Output für Plotter Gnu oder R
		if (cmd.hasOption("gnu")) {
			gnuoutput = cmd.getOptionValue("gnu");
			is_gnuplot = true;
			System.out.println("GnuPlot Output = " + gnuoutput);
		}

		// number of shuffles
		if (cmd.hasOption("n-shuffles")) {
			shuffles = Integer.parseInt(cmd
					.getOptionValue("n-shuffles"));
			is_gnuplot = true;
			System.out.println("GnuPlot Output = " + gnuoutput);
		}
		
		// GOR3MODEL JA NEIN...
		if (cmd.hasOption("gor3-model")) {
			is_gor3 = true;
			System.out.println("GorModel = gor3model");
		} else {
			System.out.println("Default value = gor1model");
		}

		// cross validation parameter
		if (cmd.hasOption("n")) {
			iterations_cross_validation = Integer.parseInt(cmd
					.getOptionValue("n"));
			// is_cross_validation = true;
		}

		if (cmd.hasOption("cv")) {
			is_cross_validation = true;
			System.out.println("cross-validation is done with: "
					+ iterations_cross_validation);
		} else {
			System.out.println("Default value of splitting data for CV = 10");
		}

		// einlesen der daten
		ReadFromFiles.readToData(predictions_file, dataset, false);
		ReadFromFiles.readToData(dssp_file, dataset, true);

		if (!is_cross_validation) {
			ArrayList<Data> data_package = dataset.getDataPackage();
			CreateSummary csum = new CreateSummary("example", detailed_file);

			// berechnung der werte...
			dataset.calcQ3values();
			dataset.calcSOVvalues(8);
			dataset.calcSummaryStatistics();

			if (is_html) {
				csum.createDetailedFileHtml(data_package, detailed_file, false);
				csum.createSummaryFileHtml(dataset, summary_file);
			} else {
				csum.createDetailedFileTxt(data_package, detailed_file, false);
				csum.createSummaryFileTxt(dataset, summary_file);
			}
			if (is_gnuplot)
				csum.createTableData(dataset, gnuoutput, false, 1, 1);
		} else {
			// Cross validation
			/**
			 * letzter parameter gibt an wie viele k als testset benutzt werden
			 * sollen
			 */
			CrossValidation cv = new CrossValidation(dataset,
					iterations_cross_validation, 1);
			// true wenn gor3 benutzt werden soll sonst nur gor1
			// und gor1 noch fehler...
			cv.repeatedCV(shuffles, is_gor3, summary_file, detailed_file, is_html, gnuoutput);
		}
	}
}
