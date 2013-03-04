package gorpack;

import java.io.*;

public class Useful {
//Tools needed in every GOR Model. Only static references possible
	public final static int aa = 21;
	public final static int windowsize = 17;
	
	//counts number of secondary structure elements in String
	public static int[] countss(String ss){
		int[] r = {0,0,0};
		for(int i = 0; i<ss.length(); i++){
			if(ss.charAt(i) == 'C') r[0]++;
			else if(ss.charAt(i) == 'E') r[1]++;
			else if (ss.charAt(i) == 'H') r[2]++;
		}
		return r;
	}
	
	public static String aminoacids = "ACDEFGHIKLMNPQRSTVWY";
	
	//Amino Acid -> Integer
	public static int aaint(char aa){
		if(aminoacids.indexOf(aa) >= 0){
			return aminoacids.indexOf(aa);
		}else{
			return 20;
		}
	}
	
	//Integer -> Amino Acid
	public static char aachar(int k){
		if(k >= 0 && k < 20){
		return aminoacids.charAt(k);
		} else {
			throw new IllegalArgumentException("Not a legal value");
		}
	}
	
	// Secondary Structure -> Integer
	public static int[] sstoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i < aa.length(); i++){
			if(aa.charAt(i) == 'C'){ret[i] = 0;}
			if(aa.charAt(i) == 'E'){ret[i] = 1;}
			if(aa.charAt(i) == 'H'){ret[i] = 2;}
		}
		return ret;
	}
	
	
	// Secondary Structure -> Integer
		public static int ssint(char aa){
			switch(aa){
				case 'C': return 0;
				case 'E': return 1;
				case 'H': return 2;
			}
			return 3;
		}
	//Integer to Secondary Structure
	public static String sschar(int i){
		if(i == 0) return "C";
		else if(i == 1) return "E";
		else if(i == 2) return "H";
		else return "-";
	}
	
	//Amino Acid String to Integer Array
	public static int[] aatoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i< aa.length(); i++){
			ret[i] = aaint(aa.charAt(i));
		}
		return ret;
	}
	
	//Reads file and turns it into String Array; first one: ID second one: Primary Structure third one: Secondary Structure
	public static String[][] filetostring(String path) throws FileNotFoundException{
		String[][] out = null;
		try{
		FileReader input = new FileReader(path);
		BufferedReader br = new BufferedReader(input);
		String foo;
		String[] bar;
		int ct = 0;
		foo = br.readLine();
		while(foo != null){
			foo = br.readLine();
			if(foo.startsWith(">")) out[0][ct] = foo;
			else if(foo.startsWith("AS")) out[1][ct] = foo;
			else if(foo.startsWith("SS")) out[2][ct] = foo;
			ct++;
		}
		br.close();
		} catch(FileNotFoundException e){
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	//works as of 26-02 15:17 parses file to sequence
	public static Sequence[] filetosequence(String path, int tr) throws FileNotFoundException{
		Sequence[] out = new Sequence[10000];

		for(int i = 0; i<10000; i++){
			out[i] = new Sequence();
		}
		try{
		FileReader input = new FileReader(path);
		BufferedReader br = new BufferedReader(input);
		String line;
		int ct = 0;
		line = br.readLine();
		if(line.startsWith(">")){ 	
			out[0].setid(line);
		}
		while((line = br.readLine()) != null){
			//System.out.println("++++"+line);
			if(line.startsWith(">")) {
				String foo = line.substring(tr);
				out[ct].setid(foo);}
			else if(line.startsWith("AS")) {
				
				String foo = line.substring(3);
				out[ct].setps(foo);}
			else if(line.startsWith("SS")) {
				String foo = line.substring(3);
				out[ct].setss(foo);}
			else ct++;
			//System.out.println(">>>>"+out[0].getps());
		}
		br.close();
		} catch(FileNotFoundException e){
			//System.out.println("Hallo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return out;
	}
	
	//if no number of gaps between model tables is given, this method is called
	public static Sequence[] filetosequence(String path) throws FileNotFoundException{
		return filetosequence(path, 2);
	}
	
	//extracts real secondary structure 
	public static Sequence[] realss(String path) throws FileNotFoundException{
		return filetosequence(path, 2);
	}
	
	//makes Secondary Structure String from int array
	public static String makess(int[] ss){
		String sek = "";
		String k = "";
		for(int i = 0; i<ss.length; i++){
			k = k+sschar(ss[i]);
			//System.out.println(k);
		}
		return k;
	}
	//reads type of Gor Matrix file. Very extraordinary way of parsing
	public static int type(String path){
		int ret = 0;
		try{
			FileReader input = new FileReader(path);
			BufferedReader br = new BufferedReader(input);
			String line;
			line = br.readLine();
				if(line.startsWith("//")){ 
					ret = Integer.parseInt(line.substring(9,10));
					
				}
			br.close();
			input.close();
			} catch(FileNotFoundException e){
				//System.out.println("Hallo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ret;
	}
	
	//reads three dimensional model file
	public static int[][][] readmodel(String path){
		int[][][] out = new int[3][aa][windowsize];
		//Letzte Zeile des Modells auf 0 setzen
		try{
			FileReader input = new FileReader(path);
			BufferedReader br = new BufferedReader(input);
			String line;
			line = br.readLine();
			if(line.startsWith("//")){
				br.readLine();
			}
			int k = -1;
			while((line = br.readLine()) != null){
				if(line.startsWith("=")) {k++; br.readLine();}
				else if(line.startsWith("Y")) {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
					out[k][19][i] = Integer.parseInt(s[i+1]);
					}
					br.readLine(); br.readLine();
				} else {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
						out[k][aaint(c)][i] = Integer.parseInt(s[i+1]);
					}
				}
			}
			br.close();
		} catch(FileNotFoundException e){
			//System.out.println("Hallo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
}
	//reads four dimensional model file
	public static int[][][][] read3model(String path){
		int[][][][] out = new int[3][aa][aa][windowsize];
		try{
			FileReader input = new FileReader(path);
			BufferedReader br = new BufferedReader(input);
			String line;
			line = br.readLine();
			if(line.startsWith("//")){ 
				int dim = Integer.parseInt(line.substring(9,10));
				br.readLine();
			}
			int k = -1;
			int cha = 0;
			while((line = br.readLine()) != null){
				if(line.startsWith("=")) {
					cha = sstoint(line.substring(3,4))[0];
					k = aatoint(line.substring(1,2))[0];
					br.readLine();}
				else if(line.startsWith("Y")) {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
					out[cha][k][aaint(c)][i] = Integer.parseInt(s[i+1]);
					}
					br.readLine();
					br.readLine();}
				else {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
					out[cha][k][aaint(c)][i] = Integer.parseInt(s[i+1]);
					}
				}
			}
			br.close();
			} catch(FileNotFoundException e){
				//System.out.println("Hallo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return out;
	}
	
	//reads six dimensional model
	public static int[][][][][][] read4model(String path){
		//C,E,H -- Aminosäure -- Aminosäure an Stelle -- Stelle -- Aminosäure in Tabelle - Position
		int[][][][][][] out = new int[3][aa][aa][windowsize][aa][windowsize];
		try{
			FileReader input = new FileReader(path);
			BufferedReader br = new BufferedReader(input);
			String line;
			line = br.readLine();
			if(line.startsWith("//")){ 
				int dim = Integer.parseInt(line.substring(9,10));
				br.readLine();
			}
			int k = -1;
			int cha = 0;
			int cha2 = 0;
			int cha3 = 0; //window position
			while((line = br.readLine()) != null){
				if(line.startsWith("=")) {
					if(line.charAt(7) == '-'){
						cha3 = - (Integer.parseInt(line.substring(8,9))) + 8;
					}
					else cha3 = Integer.parseInt(line.substring(7,8)) + 8;
					cha2 = aatoint(line.substring(5,6))[0];
					cha = aatoint(line.substring(3,4))[0];
					k = sstoint(line.substring(1,2))[0];
					br.readLine();}
				else if(line.startsWith("Y")) {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
					out[k][cha][cha2][cha3][aaint(c)][i] = Integer.parseInt(s[i+1]);
					}
					br.readLine();}
				else if(line.startsWith("+")) {
					return out;
				}
				else {
					String[] s = line.split("\t");
					char c = s[0].charAt(0);
					for(int i = 0; i < s.length - 1; i++){
						out[k][cha][cha2][cha3][aaint(c)][i] = Integer.parseInt(s[i+1]);
					}
				}
			}
			br.close();
			} catch(FileNotFoundException e){
				//System.out.println("Hallo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return out;
	}
	
	
	//takes sequence, makes FASTA format output
	public static String makefastastring(Sequence s){
		String result = "";
		String a = s.getid();
		String b = s.getps();
		String c = s.getss();
		result = result  + a + "\n";
		result = result + "AS " + b + "\n";
		result = result + "PS " + c + "\n";
		return result;
	}
	//takes sequence, makes FASTA format output
		public static String htmlstring(Sequence s){
			String result = "";
			String a = s.getid();
			String b = s.getps();
			String c = s.getss();
			result = result + a + "<br>";
			result = result + "AS " + b + "<br>";
			result = result + "PS " + c + "<br>";
			return result;
		}
	
	/*public static String makefastastring(Sequence s, String[]pr){
		String result = "";
		String a = s.getid();
		String b = s.getps();
		String c = s.getss();
		result = result + ">" + a + "\n";
		result = result + "AS " + b + "\n";
		result = result + "SS " + c + "\n";
		result = result + "PH " + pr[2] + "\n";
		result = result + "PE " + pr[1] + "\n";
		result = result + "PC " + pr[0] + "\n";
		return result;
	}*/
	//takes model and writes values to file selected with path
	public static boolean writemodelfile(String path, Gor1Model m) throws IOException{
		FileWriter pw = new FileWriter(path);
		String s = "";
		pw.write(m.head + "\n" + "\n");
		for(int i = 0; i < 3; i++){
			pw.write("=" + Useful.sschar(i) + "=" + "\n" + "\n");
			for(int j = 0; j < m.naa-1; j++){
				pw.write(Useful.aachar(j) + "\t");
				for (int k = 0; k < m.windowsize; k++){
					pw.write(m.model[i][j][k] + "\t");
				}
				pw.write("\n");
			}
			pw.write("\n");
			pw.write("\n");
		}
		pw.flush();
		pw.close();
		return true;
	}
	//same for gor3
	public static boolean writemodelfile(String path, Gor3Model m) throws IOException{
		FileWriter pw = new FileWriter(path);
		String s = "";
		pw.write(m.head + "\n" + "\n");
		for(int i = 0; i < m.naa-1; i++){
			for(int j = 0; j < 3; j++){
			pw.write("=" + Useful.aachar(i) + "," + Useful.sschar(j) + "=" + "\n" + "\n");
				for(int k = 0; k < m.naa-1; k++){
					pw.write(Useful.aachar(k) + "\t");
					for (int l = 0; l < m.windowsize; l++){
						pw.write(m.model[j][i][k][l] + "\t");
					}
					pw.write("\n");
			}
			pw.write("\n");
			pw.write("\n");
			}
		}
		pw.flush();
		pw.close();
		return true;
	}
	//and for gor4
	public static boolean writemodelfile(String path, Gor4Model m) throws IOException{
		FileWriter pw = new FileWriter(path);
		String s = "";
		pw.write(m.head + "\n" + "\n");
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < m.naa-1; j++){
				for(int k = 0; k < m.naa-1; k++){
					for(int l = 0; l < m.windowsize; l++){
						pw.write("=" + Useful.sschar(i) + "," + Useful.aachar(j) + "," + Useful.aachar(k) +"," + (l-8) +"=" + "\n" + "\n");
						for(int n = 0; n < m.naa-1; n++){
							pw.write(Useful.aachar(n) + "\t");
							for(int p = 0; p < m.windowsize; p++){
								pw.write(m.model[i][j][k][l][n][p] + "\t");
							}
							pw.write("\n");
						}
						if(l != m.windowsize-1)pw.write("\n");
					}
					if(j != m.naa-2 || k!= m.naa-2) pw.write("\n");
				}
				if(j == m.naa-2) pw.write("\n");
			}
		}
		pw.flush();
		pw.close();
		return true;
	}

	public static void tooshort() {
		// TODO Auto-generated method stub
		System.out.println("Sequence too short. At least "+windowsize+" bp length required");
		System.exit(42);
	}
	
	public static String htmlcode(String content){
		String ret = "";
		ret = ret + "<html><head><title>Secondary Structure Prediction</title></head>";
		ret = ret + "<body><h2>" +content+ "</h2></body></html>";
		return ret;
	}
	

}
