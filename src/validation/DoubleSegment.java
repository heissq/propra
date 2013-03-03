package validation;

public class DoubleSegment {
	public Segment observed;
	public Segment predicted;
	public int id;

	/**
	 * 2 Segmente in objekt für berechnung sov
	 * 
	 * @param id
	 *            durch berechnung von minpos.observedsegment *
	 *            minpos.predictedsegment
	 * @param ob
	 *            observed segment
	 * @param pred
	 *            predicted segment
	 */
	public DoubleSegment(int id,Segment ob, Segment pred) {
		observed=ob;
		predicted=pred;
		this.id = id;
	}
}
