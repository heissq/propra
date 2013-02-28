package validation;

public class Segment {
public char c;
public int start;
public int end;

public Segment(char c, int start, int end){
	this.c = c;
	this.start = start;
	this.end = end;
}
public Segment(){
	this.c = 'C';
	this.start = 0;
	this.end = 0;
//	this.c = c;
//	this.start = start;
//	this.end = end;
}
public char getC() {
	return c;
}
public void setC(char c) {
	this.c = c;
}
public int getStart() {
	return start;
}
public void setStart(int start) {
	this.start = start;
}
public int getEnd() {
	return end;
}
public void setEnd(int end) {
	this.end = end;
}



}
