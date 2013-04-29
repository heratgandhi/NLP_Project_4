import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;


public class OutputForKaggle 
{
	FileReader fr,fr1,fr2;
	BufferedReader br,br1;
	
	public void generateKaggleFile(String filename) throws IOException
	{
		fr=new FileReader(filename);
		br=new BufferedReader(fr);
		File fileTwo=new File("try_kaggle.txt");
		FileOutputStream fos=new FileOutputStream(fileTwo);
		PrintWriter pw=new PrintWriter(fos);
		String line="";
		String words[];
		line=br.readLine();
		while((line=br.readLine())!=null)
		{
			//words=line.split("");
			String sign=line.substring(0,1);
			System.out.println(sign);
			String signmid;
			if(sign.equals("-"))
			{
				signmid="0";
				//signmid=sign.replace('0', '1');
				//sign="-"+signmid;
			}
			else
			{
				signmid="1";
			}
			pw.println(sign);
			//System.out.println(words.length);

		}
	}
	public static void main(String args[]) throws IOException
	{
		OutputForKaggle op=new OutputForKaggle();
		op.generateKaggleFile("output_for_kaggle");
	}
}
