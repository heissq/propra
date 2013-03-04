package validation;

public class CVResult extends Result {
	// für cv oder unterschiedliche typen von ergebnissen basierend
	private ResIdentity identitiy;

	public CVResult() {
	}

	//TODO super(params) fehlt
	public CVResult(String pdbid, int iteration, int iterationshuffle) {
		identitiy.iteration = iteration;
		identitiy.shuffle_set_iteration = iterationshuffle;
		identitiy.pdbid = pdbid;
	}
}
