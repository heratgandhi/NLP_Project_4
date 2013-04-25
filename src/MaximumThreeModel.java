import java.io.*;

public class MaximumThreeModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader[] br = new BufferedReader[3];
			br[0] = new BufferedReader(new FileReader(args[0]));
			br[1] = new BufferedReader(new FileReader(args[1]));
			br[2] = new BufferedReader(new FileReader(args[2]));
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("FinalOutput"));
			
			String line = "",line1,line2;
			int zv,ov;
			while((line=br[0].readLine())!= null) {
				line1 = br[1].readLine();
				line2 = br[2].readLine();
				
				zv = 0;
				ov = 0;
				
				if(Integer.parseInt(line) == 0) zv++;
				else ov++;
				if(Integer.parseInt(line1) == 0) zv++;
				else ov++;
				if(Integer.parseInt(line2) == 0) zv++;
				else ov++;
				
				if(zv < ov) {
					bw.write("1\n");
				} else {
					bw.write("0\n");
				}
			}
			
			bw.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
