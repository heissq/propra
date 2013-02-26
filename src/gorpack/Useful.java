package gorpack;

import java.io.*;

public class Useful {
//Tools needed in every GOR Model. Only static references possible
	
	public static int[] countss(String ss){
		int[] r = {0,0,0};
		for(int i = 0; i<ss.length(); i++){
			if(ss.charAt(i) == 'C') r[0]++;
			else if(ss.charAt(i) == 'E') r[1]++;
			else if (ss.charAt(i) == 'H') r[2]++;
		}
		return r;
	}
	
	public static String aminoacids = "ARNDCQEGHILKMFPSTWYV";
	
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
	
	public static Sequence[] filetosequence(String path) throws FileNotFoundException{
		Sequence[] out = new Sequence[10000];
		for(int i = 0; i<10000; i++){
			out[i] = new Sequence();
		}
		try{
		FileReader input = new FileReader(path);
		BufferedReader br = new BufferedReader(input);
		String foo;
		int ct = 0;
		foo = br.readLine();
		if(foo.startsWith(">")){ 
			out[0].setid(foo);
		}
		while((foo = br.readLine()) != null){
			if(foo.startsWith(">")) out[ct].setid(foo);
			else if(foo.startsWith("AS")) out[ct].setps(foo);
			else if(foo.startsWith("SS")) out[ct].setss(foo);
			else ct++;
		}
		br.close();
		} catch(FileNotFoundException e){
			System.out.println("Hallo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

}
