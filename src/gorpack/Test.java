package gorpack;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
public static void main(String[] Args) throws IOException{
	Gor1Model g = new Gor1Model();
	//g.train("MFKVYGYDSNIHKCVYCDNAKRLLTVKKQPFEFINIMPEKGVFDDEKIAELLTKLGRDTQIGLTMPQVFAPDGSHIGGFDQLREYFK", "CEEEEECCCCCCCCHHHHHHHHHHHHCCCCEEEEECCCECCECCHHHHHHHHHHHCCCCCCCCCCCEEECCCCCEEECHHHHHHHCC");
	g.train("/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db");
	for(int i = 0; i < 3; i++){
		for(int j = 0; j < 20; j++){
			for (int k = 0; k < 17; k++){
				System.out.print(g.matrix[i][j][k] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}
	//int[] bar = {3,3,8,16,8,2,5,7,1,0,3,1,1,2,2,3,4,4,4,4};
	//int[] foobar = g.predict(bar);
	//System.out.println(foobar[1]);
	//System.out.println(g.matrix[0][0][0]);
	Sequence[] bla = Useful.filetosequence("/home/proj/biocluster/praktikum/bioprakt/Data/GOR/CB513DSSP.db");
	//System.out.println(bla[0].getss());
	System.out.println(g.predictString("TTYAD"));
	System.out.println(g.summatrix(g.model)[1]);
	int[][][] tests = Useful.readmodel("/home/proj/biocluster/praktikum/bioprakt/Data/commandline/gor_examples/gor1_cb513_model.txt");
	System.out.println(tests[0][0][0]);
	//System.out.println(Useful.aaint('Y'));
/*	int[][][][][][] t2 = Useful.read4model("/home/proj/biocluster/praktikum/bioprakt/Data/commandline/gor_examples/gor4_cb513_model.txt");
*/	/*System.out.println(t2[0][0][0][0][0][1]);*/
	/*Gor4Model g4 = new Gor4Model();
	g4.setmodel(t2);
	Useful.writemodelfile("/home/proj/biocluster/praktikum/bioprakt/progprakt6/Solution4/test3.txt", g4);
	System.out.println(g4.predictString("TTYADFIASGRTYGRRNAIHDTTYADFIASGRTYGRRNAIHD"));*/
}
}
