package validation;

public class Segment {
	// sequence observed oder predicted in Data class
	public int id;
	public char ch;
	public int startpos;
	public int endpos;

	public Segment(int id, char c, int start, int end) {
		this.id = id;
		this.ch = c;
		this.startpos = start;
		this.endpos = end;
	}

	public Segment() {
		this.ch = 'C';
		this.startpos = -1;
		this.endpos = -1;
		this.id = -1;
	}
}
