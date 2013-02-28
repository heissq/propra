package validation;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
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
		} else {
			System.out.println("Default value = dssp.txt");
		}

		// detailed file
		if (cmd.hasOption("d")) {
			detailed_file = cmd.getOptionValue("d");
		} else {
			System.out.println("Default value = detailed_summary.txt");
		}

		// summary file
		if (cmd.hasOption("s")) {
			summary_file = cmd.getOptionValue("s");
		} else {
			System.out.println("Default value = summary/detailed_summary.txt");
		}

		if (cmd.hasOption("f")) {
			switch (cmd.getOptionValue("f")) {
			// TODO noch option für mehrere
			// quirin fragen
			case "txt":

			case "txt:":

			case "html":

			case "html:":

			default:
				System.out.println("default value");
			}
		} else {
			System.out.println("Default value = summary.txt");
		}
	}

}
