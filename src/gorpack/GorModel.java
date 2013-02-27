package gorpack;

public interface GorModel {
	public final static int nstates = 3;
	public final static int naa = 21;
	public final static int windowsize = 17;
	public String readfile(String filename);
	public void train(String sek, String prim);
	public void writefile();
}

