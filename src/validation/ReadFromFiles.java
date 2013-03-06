package validation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// TODO klasse als innerclass von main aufrufen... muss ja nicht so komplex sein...
public class ReadFromFiles {
	public static void readToData(String file, DataSet ds, boolean is_dssp)
			throws FileNotFoundException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
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
						rs = "";
						ps = "";
						as = "";
					}
					id = foo.substring(2);
				} else if (foo.startsWith(">")) {
					if (!id.isEmpty()) {
						ds.addInputChunk(id, rs, ps, as);
						rs = "";
						ps = "";
						as = "";
					}
					id = foo.substring(1);
				} else if (foo.startsWith("AS"))
					as = foo.substring(3);
				else if (foo.startsWith("PS") || (foo.startsWith("SS") && !is_dssp))
					ps = foo.substring(3);
				else if (foo.startsWith("RS") || (foo.startsWith("SS") && is_dssp))
					rs = foo.substring(3);
				foo = br.readLine();
			}
			if (!id.isEmpty()){
				ds.addInputChunk(id, rs, ps, as);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei:"+file+" nicht gefunden.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
