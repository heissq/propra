package gorpack;

public class Test {
public static void main(String[] Args){
	Gor1Model g = new Gor1Model();
	int foo =  Useful.aaint('A');
	System.out.println(foo);
	g.train("AAAAAAAAAAAAAAAAAAAAAAAA", "CCCCEEEHCCCCEEEHCCCCEEEH");
	for(int i = 0; i < 3; i++){
		for(int j = 0; j < 20; j++){
			for (int k = 0; k < 17; k++){
				System.out.println(g.matrix[i][j][k]);
			}
		}
	}
	int[] bar = {3,2,2,3,3,3,4,4,4,1,1,2,2,3,4,4,4,4};
	int[] foobar = g.predict(bar);
	System.out.println(foobar[1]);
	//System.out.println(g.matrix[0][0][0]);
}
}
