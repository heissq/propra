package gorpack;

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
		}
		else{
		throw new IllegalArgumentException("Not a legal Protein sequence");
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
	
	public static int[] sstoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i < aa.length(); i++){
			if(aa.charAt(i) == 'C'){ret[i] = 0;}
			if(aa.charAt(i) == 'E'){ret[i] = 1;}
			if(aa.charAt(i) == 'H'){ret[i] = 2;}
		}
		return ret;
	}
	
	public static int[] aatoint(String aa){
		int[] ret = new int[aa.length()];
		for(int i = 0; i< aa.length(); i++){
			ret[i] = aaint(aa.charAt(i));
		}
		return ret;
	}
	
	public static String filetostring(String path){
		
	}

}
