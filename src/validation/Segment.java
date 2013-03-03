package validation;

public class Segment {
	// sequence observed oder predicted in Data class
	public int id;
	public char ch;
	public int startpos;
	public int endpos;
	public boolean overlap;

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
		this.overlap = false;
	}

	@Override
	public String toString() {
		String ret = "|\t" + ch + "\t|\t" + startpos + "\t|\t" + endpos + "\t|";
		return ret;
	}
}
