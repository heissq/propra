package validation;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	public static String predictions_file = "predictions.txt";
	public static String dssp_file = "dssp.txt";
	public static String summary_file = "summary.txt";
	public static String detailed_file = "detailed_summary.txt";
	public static DataSet dataset = new DataSet();

	/**
	 * @param args
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws ParseException,
			FileNotFoundException {
		// variablen für optionen:
		String predictions_file = "predictions.txt";
		String dssp_file = "dssp.txt";
		String summary_file = "summary.txt";
		String detailed_file = "detailed_summary.txt";

		Options opt = new Options();
		opt.addOption("", "p", true, "<predictions>");
		opt.addOption("", "r", true, "<dssp-file>");
		opt.addOption("", "f", true, "<txt|html>");
		opt.addOption("", "s", true, "<summary file>");
		opt.addOption("", "d", true, "<detailed file>");

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

		// summary file
		if (cmd.hasOption("s")) {
			summary_file = cmd.getOptionValue("s");
			System.out.println(summary_file);
		} else {
			System.out.println("Default value = summary.html/summary.txt");
		}

		if (cmd.hasOption("f")) {
			switch (cmd.getOptionValue("f")) {
			// TODO noch option für mehrere
			// quirin fragen
			case "txt":
				break;
			case "txt:":
				break;
			case "html":
				break;
			case "html:":
				break;
			default:
				System.out.println("default value");
				break;
			}
		} else {
			System.out.println("Default value = summary.txt");
		}

		ReadFromFiles.readToData(predictions_file, dataset);
		ReadFromFiles.readToData(dssp_file, dataset);

		// ausdrucken der elemente

		System.out.println("warum");
		System.out.println(dataset);
		System.out.println("----------");
		dataset.printeverything();
	}

}
