package validation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// TODO klasse als innerclass von main aufrufen... muss ja nicht so komplex sein...
public class ReadFromFiles {
	public static void readToData(String file, DataSet ds)
			throws FileNotFoundException {
		try {
			FileReader input = new FileReader(file);
			BufferedReader br = new BufferedReader(input);
			String foo;
			String id = "";
			String as = "";
			String ps = "";
			String rs = "";
			//substring methode fängt mit 0 an??
			foo = " ";
			while (foo != null) {

				if (foo.startsWith("> ")) {
					//gehe von "> <hier id ab 3ter stelle>"
					if (!id.isEmpty()) {
						ds.addInputChunk(id, rs, ps, as);
					}
					id = foo.substring(2);
				} else if (foo.startsWith(">")) {
					if (!id.isEmpty()) {
						ds.addInputChunk(id, rs, ps, as);
					}
					id = foo.substring(1);
				} else if (foo.startsWith("AS"))
					as = foo.substring(3);
				else if (foo.startsWith("PS"))
					ps = foo.substring(3);
				else if (foo.startsWith("RS"))
					rs = foo.substring(3);
				foo = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}