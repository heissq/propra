package gorpack;

public class Sequence2 extends Sequence{
	private String rs;

	public String getSs() {
		return rs;
	}

	public void setSs(String ss) {
		this.rs = ss;
	}
	
	//ps = primar struktur --> as oder aa sequenz und ss die predicted secondary structure
	public Sequence2(String id, String ps, String ss, String rs) {
		super(id,ps,ss);
		this.rs = rs;
	}
}
