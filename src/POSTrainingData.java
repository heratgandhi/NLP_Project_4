import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class POSTrainingData 
{
	FileReader fr;
	BufferedReader br;
	String inputFileName;
	
	public void attachPOS(String filename) throws Exception
	{
		
		try
		{
			File myfile=new File("post_test");
			FileOutputStream fos=new FileOutputStream(myfile);
			PrintWriter pw=new PrintWriter(fos);
			MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
			fr=new FileReader(filename);
			br=new BufferedReader(fr);
			String line;
			String review;
			String tempstr="";
			String word,pos;
			String []words;
			while((line=br.readLine())!=null)
			{
				
				review = line.substring(line.indexOf(',')+3);
				review = review.replace("\"", "").toLowerCase();
				tempstr=tagger.tagString(review);
				pw.append(line.substring(0,1)+" "+tempstr);
				pw.print('\n');
				
			}
			
			pw.flush();
			pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		
	}
	public static void main(String args[]) throws Exception
	{
		
		POSTrainingData pt=new POSTrainingData();
		pt.attachPOS("Train data");
		
		
	}

}
