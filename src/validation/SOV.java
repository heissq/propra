package validation;

import java.util.ArrayList;

import gorpack.*;

public class SOV {
public int[][] segments = new int[3][];

public static Segment[][] makesegments(String a, String b){
	Segment[][] ret = new Segment[2][Math.max(a.length(), b.length())];
	/*for(int i = 0; i < 2; i++){
		for (int j = 0; j < Math.max(a.length(), b.length()); j++) {
			ret[i][j] = new Segment('C',0,0);
		}
	}*/
	
	char achar = a.charAt(0);
	char bchar = b.charAt(0);
	int cta = 0;
	int ctb = 0;
	int astart = 0;
	int bstart = 0;
	int foo = Math.min(a.length(), b.length());
	//System.out.println(foo);
	for(int i = 0; i < foo; i++){
		if(a.charAt(i) == achar && b.charAt(i) == bchar) { achar = a.charAt(i)  ; bchar = b.charAt(i);}
		else if(a.charAt(i) == achar){
			//System.out.println(b.charAt(i)+" 2 "+i + " "+ ctb);
			ret[1][ctb] = new Segment(b.charAt(i-1), bstart, i-1);
			ctb++;
			bstart = i;/*
			ret[1][ctb].c = b.charAt(i-1);
			ret[1][ctb].end = i-1;
			ctb++;
			ret[1][ctb].start = i;*/
			bchar = b.charAt(i);
		}
		else if(b.charAt(i) == bchar){
			//System.out.println(a.charAt(i)+" 1 " +i + " " + cta);
			ret[0][cta] = new Segment(a.charAt(i-1), astart, i-1);
			cta++;
			astart = i;
			/*ret[0][cta].c = a.charAt(i-1);
			ret[0][cta].end = i			-1;
			ret[0][cta].start = i;*/		
			achar = a.charAt(i);			
		}		
		else {	
			//System.out.println(a.charAt(i)+" 12 "+i + " " +cta);
			ret[0][cta] = new Segment(a.charAt(i-1), astart, i-1);
			ret[1][ctb] = new Segment(b.charAt(i-1), bstart, i-1);
			cta++;
			ctb++;
			astart = i;
			bstart = i;
			
			achar = a.charAt(i);
			bchar = b.charAt(i);
		}
	}
	return ret;
}


/*public int[][][][] discard(String a, String b){
	// [a,b][Anf,End][C,E,H][positionen]
	int[][][][] ret = new int[2][2][3][Math.max(a.length(), b.length())];
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
}*/
/*
public static Segment[][] overlaps(Segment[][] all){
	//Define return values
	Segment[][] ret = new Segment[10000][2];
	for(int i = 0; i < 2; i++){
		for (int j = 0; j < Math.max(all[0].length, all[1].length); j++) {
			ret[j][i] = new Segment('C',0,0);
		}
	}
	
	int ct = 0;
	for(int i = 0; i < all[0].length; i++){
		for(int j = 0; j < all[1].length; j++){
			if(all[1][j].start > all[0][i].end || all[0][i].start > all[1][j].end) {
				j = all[1].length;
			}
			else if(overlap(all, i, j)) {
				//System.out.println(ct);
				ret[ct][0] = new Segment(all[0][i].c, all[0][i].start, all[0][i].end);
				//System.out.println(ret[ct][0].c);
				//System.out.println(all[0][i].start);
				ret[ct][1] = new Segment(all[1][j].c, all[1][j].start, all[1][j].end);
				ct++;
			}
		}
		if(i > 0 && all[0][i].start == 0) i = all[0].length;
	}
	return ret;
} */

public static Segment[][] overlaps(String a, String b){
	//Define return values
	Segment[][] all = new Segment[2][Math.max(a.length(), b.length())];
	for(int i = 0; i < 2; i++){
		for(int j = 0; j < Math.max(a.length(), b.length()); j++){
			if (all[i][j] == null);
			else{
			all[i][j] = new Segment(makesegments(a,b)[i][j].c, makesegments(a,b)[i][j].start, makesegments(a,b)[i][j].end);
			System.out.println(all[i][j].end);
		}
		}
	}
	Segment[][] ret = new Segment[10000][2];
	for(int i = 0; i < 2; i++){
		for (int j = 0; j < Math.max(all[0].length, all[1].length); j++) {
			ret[j][i] = new Segment('C',0,0);
		}
	}
	int ct = 0;
	for(int i = 0; i < all[0].length; i++){
		for(int j = 0; j < all[1].length; j++){
			if(all[1][j].start > all[0][i].end || all[0][i].start > all[1][j].end) {
				j = all[1].length;
			}
			else if(overlap(all, i, j)) {
				//System.out.println(ct);
				ret[ct][0] = new Segment(all[0][i].c, all[0][i].start, all[0][i].end);
				//System.out.println(ret[ct][0].c);
				//System.out.println(all[0][i].start);
				ret[ct][1] = new Segment(all[1][j].c, all[1][j].start, all[1][j].end);
				ct++;
			}
		}
		if(i > 0 && all[0][i].start == 0) i = all[0].length;
	}
	return ret;
}

public static int numberoverlaps(String a, String b){
	//Define return values
	Segment[][] all = new Segment[2][Math.max(a.length(), b.length())];
	for(int i = 0; i < 2; i++){
		for(int j = 0; j < Math.max(a.length(), b.length()); j++){
			all[i][j] = makesegments(a,b)[i][j];
		}
	}
	int ct = 0;
	for(int i = 0; i < all[0].length; i++){
		for(int j = 0; j < all[1].length; j++){
			if(all[i][j] == null) ;
			else if(all[1][j].start > all[0][i].end || all[0][i].start > all[1][j].end) {
				j = all[1].length;
			}
			else if(overlap(all, i, j)) {
				ct++;
			}
		}
		if(i > 0 && all[0][i].start == 0) i = all[0].length;
	}
	return ct;
}


/*
public static int[][][][] overlaps(int[][][][] all){
	//Define return values
	int[][][][] ret = new int[2][2][10000][2];
	int ct = 0;
	for(int i = 0; i < all[0].length; i++){
		for(int j = 0; j < all[1].length; j++){
			if(all[1][j].start > all[0][i].end || all[0][i].start > all[1][j].end) {
				j = all[1].length;
			}
			else if(overlap(all, i, j)) {
				//System.out.println(ct);
				ret[ct][0] = new Segment(all[0][i].c, all[0][i].start, all[0][i].end);
				//System.out.println(ret[ct][0].c);
				System.out.println(all[0][i].start);
				ret[ct][1] = new Segment(all[1][j].c, all[1][j].start, all[1][j].end);
				ct++;
			}
		}
		if(i > 0 && all[0][i].start == 0) i = all[0].length;
	}
	return ret;
}
*/

public static int len(Segment s){
	return s.end - s.start;
}


public static double sov_value2(String a, String b){
	Segment[][] foo = new Segment[2][Math.max(a.length(), b.length())];
	for(int i = 0; i < 2; i++){
		for(int j = 0; j < Math.max(a.length(), b.length()); j++){
			foo[i][j] = makesegments(a,b)[i][j];
		}
	}
	Segment[][] ret = new Segment[10000][2];
	for(int i = 0; i < 2; i++){
		for (int j = 0; j < Math.max(all[0].length, all[1].length); j++) {
			ret[j][i] = new Segment('C',0,0);
		}
	}
	int ct = 0;
	for(int i = 0; i < all[0].length; i++){
		for(int j = 0; j < all[1].length; j++){
			if(all[1][j].start > all[0][i].end || all[0][i].start > all[1][j].end) {
				j = all[1].length;
			}
			else if(overlap(all, i, j)) {
				//System.out.println(ct);
				ret[ct][0] = new Segment(all[0][i].c, all[0][i].start, all[0][i].end);
				//System.out.println(ret[ct][0].c);
				//System.out.println(all[0][i].start);
				ret[ct][1] = new Segment(all[1][j].c, all[1][j].start, all[1][j].end);
				ct++;
			}
		}
		if(i > 0 && all[0][i].start == 0) i = all[0].length;
	}
	double r = 0;
	double c = 0;
	for(int i = 0; i < numberoverlaps(a,b); i++){
		for(int j = 0; j < numberoverlaps(a,b); i++){
			if(i > all.length || j > all.length) {System.out.println(i); j = numberoverlaps(a,b);}
			else if(maxov(all, i, j) != 0){
				System.out.println("foo");
				System.out.println(all[i][0].end);
				c += ((minov(all, i, j) + delta(all, i, j)) / (double) maxov(all,i,j)) * len(all[0][i]);
			}
		}
	}
	ret = ((double) 100) / numberoverlaps(a,b) * c;
	return ret;
}

public static double sov_value(String a, String b){
	Segment[][] all = new Segment[Math.max(a.length(), b.length())][2];
	for(int i = 0; i < 2; i++){
		for(int j = 0; j < numberoverlaps(a,b); j++){
			all[j][i] = new Segment(overlaps(a,b)[j][i].c, overlaps(a,b)[j][i].start, overlaps(a,b)[j][i].end);
			System.out.println(all[j][i].start);
		}
	}
	double ret = 0;
	double c = 0;
	for(int i = 0; i < numberoverlaps(a,b); i++){
		for(int j = 0; j < numberoverlaps(a,b); i++){
			if(i > all.length || j > all.length) {System.out.println(i); j = numberoverlaps(a,b);}
			else if(maxov(all, i, j) != 0){
				System.out.println("foo");
				System.out.println(all[i][0].end);
				c += ((minov(all, i, j) + delta(all, i, j)) / (double) maxov(all,i,j)) * len(all[0][i]);
			}
		}
	}
	ret = ((double) 100) / numberoverlaps(a,b) * c;
	return ret;
}


public static int maxov(Segment[][] segments, int pos1, int pos2){
	if(segments.length <= pos1 || segments.length <= pos2) return 0;
	if(segments[pos1][0] == null || segments[pos2][1] == null) return 0;
	if(overlap2(segments, pos1, pos2)){
		return Math.max (segments[pos1][0].end - segments[pos2][1].start, segments[pos2][1].end - segments[pos1][0].start);
	}
	else return 0;
}

public static int minov(Segment[][] segments, int pos1, int pos2){
	if(overlap2(segments, pos1, pos2)){
		return Math.min (segments[pos1][0].end - segments[pos2][1].start, segments[pos2][1].end - segments[pos1][0].start);
	}
	else return 0;
}

public static int delta(Segment[][] segments, int pos1, int pos2){
	return (int) Math.min(Math.min(Math.floor((segments[0][pos1].end - segments[0][pos1].start) / 2.0), Math.floor((segments[1][pos2].end - segments[1][pos2].start) / 2.0)),
			Math.min(maxov(segments, pos1, pos2) - minov(segments, pos1, pos2), minov(segments,pos1,pos2)));
}
/*
public static Segment leftoverlap(Segment[][] a, int pos){
	for (int i = 0; i < a[0][pos].end; i++){
		if(overlap(a, pos, i)) return a[1][i]; 
	}
	return null;
}

public static int rightoverlap(Segment[][] a, int pos){
	int foo = 0;
	for (int i = 0; i < a[0][pos].end; i++){
		if(a[0][pos].end >= a[1][i].start && a[0][pos].start <= a[1][i].end &&  a[0][pos].c == a[1][i].c) return i; 
	}
	return foo;
}*/

public static boolean overlap(Segment[][] a, int pos1, int pos2){
	if(pos1 > a[0].length || pos2 > a[1].length) return false;
	if(((a[0][pos1].end >= a[1][pos2].start && a[0][pos1].start <= a[1][pos2].end) || (a[0][pos1].start <= a[1][pos2].end && a[0][pos1].end >= a[1][pos2].start)) && a[0][pos1].c == a[1][pos2].c) return true;
	else return false;
}


public static boolean overlap2(Segment[][] a, int pos1, int pos2){
	if(pos1 > a.length || pos2 > a.length) return false;
	if(((a[pos1][0].end >= a[pos2][1].start && a[pos1][0].start <= a[pos2][1].end) || (a[pos1][0].start <= a[pos2][1].end && a[pos1][0].end >= a[pos2][1].start)) && a[pos1][0].c == a[pos2][1].c) return true;
	else return false;
}
/*

public static boolean overlap(int[][][][] a, int pos1, int pos2){
	for(int i = 0; i < 3; i++){
	if(((a[0][1][i][pos1] >= a[1][0][i][pos2] && a[0][0][i][pos1] <= a[1][1][i][pos2]) || (a[0][pos1].start <= a[1][pos2].end && a[0][pos1].end >= a[1][pos2].start)) && a[0][pos1].c == a[1][pos2].c) return true;
	}
	return false;
}*/


}
