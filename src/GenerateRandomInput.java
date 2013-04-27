import java.io.*;

public class GenerateRandomInput {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader("testd"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("modtestd"));
			
			String line = "";
			String[] parts;
			while((line = br.readLine()) != null) {
				parts = line.split(",");
				if(parts[2].contains(".")) {
					System.out.println(parts[2].substring(0, parts[2].indexOf(".")));
					bw.write(parts[2].substring(0,parts[2].indexOf("."))+"\n");
				} else {
					System.out.println(parts[2].substring(0, parts[2].indexOf(";")));
					bw.write(parts[2].substring(0,parts[2].indexOf(";"))+"\n");
				}
				
			}			
			br.close();
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
