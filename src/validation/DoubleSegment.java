package validation;

public class DoubleSegment {
	public Segment observed;
	public Segment predicted;
	public int id;

	public DoubleSegment(int id,Segment ob, Segment pred) {
		observed=ob;
		predicted=pred;
		this.id = id;
	}
}
