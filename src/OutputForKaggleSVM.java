import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;


public class OutputForKaggleSVM 
{
	FileReader fr;
	BufferedReader br;
	
	public void generateKaggleFile(String filename) throws IOException
	{
		fr=new FileReader(filename);
		br=new BufferedReader(fr);
		File fileTwo=new File("try_kaggle.txt");
		FileOutputStream fos=new FileOutputStream(fileTwo);
		PrintWriter pw=new PrintWriter(fos);
		String line="";
		line=br.readLine();
		while((line=br.readLine())!=null)
		{
			char sign=line.charAt(0);
			String signmid;
			if(sign=='-')
			{
				if(line.charAt(1)=='0' && (line.charAt(3)=='0'||line.charAt(3)=='1'||line.charAt(3)=='2'
						||line.charAt(3)=='3'||line.charAt(3)=='4'||line.charAt(3)=='5'||line.charAt(3)=='6'))
						{
							signmid="1";
						}
						else 
							signmid="0";
			}
			else
			{
				signmid="1";
			}
			pw.println(signmid);
		}
		pw.flush();
		pw.close();
		fos.close();

	}
	public static void main(String args[]) throws IOException
	{
		OutputForKaggleSVM op=new OutputForKaggleSVM();
		op.generateKaggleFile("bigram_output");
	}
}
