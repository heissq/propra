package gorpack;

public class Sequence2 extends Sequence{
	private String rs;

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}
	
	//ps = primar struktur --> as oder aa sequenz und ss die predicted secondary structure
	public Sequence2(String id, String ps, String ss, String rs) {
		super(id,ps,ss);
		this.rs = rs;
	}
	
	@Override
	public String toString() {
		return ">"+getid()+"\nAS "+getps()+"\nPS "+getss()+"\nRS "+rs;
	}
}
