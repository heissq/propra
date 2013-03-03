package validation;

public class ResIdentity {
	public String pdbid;
	public int iteration;
	public int shuffle_set_iteration;
	
	public ResIdentity() {
	}

	public ResIdentity(String pdbid, int iteration, int shuffle_set_iteration) {
		this.pdbid = pdbid;
		this.iteration = iteration;
		this.shuffle_set_iteration = shuffle_set_iteration;
	}
}
