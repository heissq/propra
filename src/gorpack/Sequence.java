package gorpack;

public class Sequence {
private String id;
private String ps;
private String ss;

public Sequence(String id, String ps, String ss){
	this.id = id;
	this.ps = ps;
	this.ss = ss;
}

public Sequence(){
	this.id = null;
	this.ps = null;
	this.ss = null;
}

public String getps(){
	return this.ps;
}

public String getss(){
	return this.ss;
}
public String getid(){
	return this.id;
}
public void setid(String id){
	this.id = id;
}
public void setps(String ps){
	this.ps = ps;
}
public void setss(String ss){
	this.ss = ss;
}
}
