package validation;

import java.util.ArrayList;

import gorpack.*;

public class SOV {
public int[][] segments = new int[3][];


public static Segment[][] makesegments(String a, String b){
	Segment[][] ret = new Segment[2][10000];
	for(int i = 0; i < 2; i++){
		for (int j = 0; j < 10000; j++) {
			ret[i][j] = new Segment('C',0,0);
		}
	}
	
	char achar = a.charAt(0);
	char bchar = b.charAt(0);
	int cta = 0;
	int ctb = 0;
	int foo = Math.min(a.length(), b.length());
	for(int i = 0; i < foo; i++){
		if(a.charAt(i) == achar && b.charAt(i) == bchar) { ;}
		else if(a.charAt(i) == achar){
			System.out.println(b.charAt(i)+" 1");
			ret[0][ctb].c = b.charAt(i);
			ret[0][ctb].end = i;
			ctb++;
			ret[0][ctb].start = i;
			bchar = b.charAt(i);
		}
		else if(b.charAt(i) == bchar){
			System.out.println(b.charAt(i)+" 2");
			ret[0][cta].c = a.charAt(i);
			ret[0][cta].end = i;
			cta++;
			ret[0][cta].start = i;
			achar = a.charAt(i);
		}
		else {
			System.out.println(b.charAt(i)+" 3");
			ret[0][cta].c = a.charAt(i);
			ret[0][cta].end = i;
			cta++;
			ret[0][cta].start = i;
			achar = a.charAt(i);
			ret[0][ctb].c = b.charAt(i);
			ret[0][ctb].end = i;
			ctb++;
			ret[0][ctb].start = i;
			bchar = b.charAt(i);
		}
	}
	return ret;
}


public int[][][][] discard(String a, String b){
	// [a,b][Anf,End][C,E,H][positionen]
	int[][][][] ret = new int[2][2][3][];
	char achar = a.charAt(0);
	char bchar = b.charAt(0);
	int cta[] = {0,0,0};
	int ctb[] = {0,0,0};
	for(int i = 0; i < a.length(); i++){
		if(a.charAt(i) == achar || b.charAt(i) == bchar) ;
		else if(a.charAt(i) == achar) { 
			ret[0][1][Useful.ssint(achar)][cta[Useful.ssint(achar)]] = i;
			cta[Useful.ssint(achar)]++;
			achar = a.charAt(i);
			ret[0][0][Useful.ssint(achar)][cta[Useful.ssint(achar)]] = i;
			cta[Useful.ssint(achar)]++;
			} 
		else if(b.charAt(i) == bchar) {
			ret[1][1][Useful.ssint(bchar)][ctb[Useful.ssint(bchar)]] = i;
			ctb[Useful.ssint(bchar)]++;
			bchar = b.charAt(i);
			ret[1][0][Useful.ssint(bchar)][ctb[Useful.ssint(bchar)]] = i;
			ctb[Useful.ssint(bchar)]++;
		} else {
			ret[0][1][Useful.ssint(achar)][cta[Useful.ssint(achar)]] = i;
			cta[Useful.ssint(achar)]++;
			achar = a.charAt(i);
			ret[0][0][Useful.ssint(achar)][cta[Useful.ssint(achar)]] = i;
			cta[Useful.ssint(achar)]++;
			ret[1][1][Useful.ssint(bchar)][ctb[Useful.ssint(bchar)]] = i;
			ctb[Useful.ssint(bchar)]++;
			bchar = b.charAt(i);
			ret[1][0][Useful.ssint(bchar)][ctb[Useful.ssint(bchar)]] = i;
			ctb[Useful.ssint(bchar)]++;
		}
		
	}
	return ret;
}

public int maxov(Segment[][] segments, int pos){
	int len1 = segments[0][pos].end - segments[1][leftoverlap(segments, pos)].start;
	if(len1 == 0) {
		len1 = segments[1][rightoverlap(segments, pos)].end - segments[0][pos].start;
	}
	return len1;
}

public int minov(Segment[][] segments, int pos){
	int len1 = segments[0][pos].end - segments[1][rightoverlap(segments, pos)].start;
	int len2 = segments[1][rightoverlap(segments, pos)].end - segments[0][pos].start;
	return len1;
}


public int leftoverlap(Segment[][] a, int pos){
	int foo = 0;
	for (int i = 0; i < pos; i++){
		if(a[0][pos].start <= a[1][i].end && a[0][pos].c == a[1][i].c) return i; 
	}
	return foo;
}

public int rightoverlap(Segment[][] a, int pos){
	int foo = 0;
	for (int i = 0; i < pos; i++){
		if(a[0][pos].start <= a[1][i].end && a[0][pos].c == a[1][i].c) return i; 
	}
	return foo;
}

public double overlap(String a, String b){
	int[][][][] segments = makesegments(a,b);
	double sov;
	for(int i = 0; i < segments[0][0].length; i++){
		
		
		
	}
	
	
	
}


}
