import java.io.*;
import java.util.ArrayList;

class Tuple {
	String rating;
	String review;
	
	Tuple(String rating, String review) {
		this.rating = rating;
		this.review = review;
	}
}

public class KNN {
	public static int N = 1;
	public static int K = 3;
	
	public static void main(String[] args) {
		try {
			//Training Portion
			BufferedReader br = new BufferedReader(new FileReader("training_file"));
			String line="";
			
			ArrayList<Tuple> dictionary = new ArrayList<Tuple>();
			String[] parts;
			while((line=br.readLine()) != null) {
				parts = line.split(";");
				parts[1] = parts[1].replaceAll("\"", "").toLowerCase();
				Tuple t = new Tuple(parts[0],parts[1]);
				dictionary.add(t);
			}
			br.close();
			
			//Testing Portion
			BufferedReader brt = new BufferedReader(new FileReader("test_file"));
			String review = "";
			while((line=brt.readLine()) != null) {
				review = line.substring(line.indexOf(';')+1);
				review = review.replace("\"", "").toLowerCase();
				
				
			}
			brt.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
