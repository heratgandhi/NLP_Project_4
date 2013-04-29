
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SVMTrainingFile 
{

	public static int lastID=0;
	HashMap<String, Integer> w_unigramMap=new HashMap<String, Integer>();
	HashMap<String, Integer> w_bigramMap=new HashMap<String, Integer>();
	HashMap<String, Integer> char_tetragramMap=new HashMap<String, Integer>();
	HashMap<String,Integer> char_pentagramMap=new HashMap<String, Integer>();
	HashMap<String, Integer> temp_uni=new HashMap<String, Integer>();
	HashMap<String, Integer> temp_bi=new HashMap<String, Integer>();
	HashMap<String, Integer> temp_tetra=new HashMap<String, Integer>();
	HashMap<String, Integer> temp_penta=new HashMap<String, Integer>();
	HashMap<String, Integer> posTagVerbs=new HashMap<String, Integer>();
	HashMap<String, Integer> posTagAdjective=new HashMap<String,Integer>();
	HashMap<String, Integer> posTagAdverbs=new HashMap<String, Integer>();
	FileReader fr,fr1,fr2;
	BufferedReader br,br1;
	
	public void readTrainingFile(String filename) throws IOException
	{

		fr=new FileReader(filename);
		br=new BufferedReader(fr);
		String line="";
		String review="",review1="",review2="",review3="",review4="";
		String[] words_unigram, words_bigram, words_char_tetragram, words_char_pentagram,words_postag;
		String unigram="", bigram="", char_tetragram="", char_pentagram="", postagstr="";
		
		while((line=br.readLine())!=null)
		{
			review = line.substring(line.indexOf(',')+3);
			review = review.replace("\"", "").toLowerCase();

			words_unigram = review.split(" ");
			for(int i=0;i<words_unigram.length;i++) 
			{
				unigram = words_unigram[i];
				for(int j=1;j<1 && (i+j) < words_unigram.length;j++) 
				{
					unigram += " " + words_unigram[i+j];
				}
				if(!w_unigramMap.containsKey(unigram)) 
				{
					w_unigramMap.put(unigram, ++lastID);
				}
			}
			review1 = line.substring(line.indexOf(',')+3);
			review1 = review.replace("\"", "").toLowerCase();

			words_bigram = review1.split(" ");
			for(int ii=0;ii<words_bigram.length;ii++) 
			{
				bigram = words_bigram[ii];
				for(int jj=1;jj<2 && (ii+jj) < words_bigram.length;jj++) 
				{
					bigram += " " + words_bigram[ii+jj];
				}
				if(!w_bigramMap.containsKey(bigram)) 
				{
					w_bigramMap.put(bigram, ++lastID);
				}
			}
			review2 = line.substring(line.indexOf(',')+3);
			review2 = review2.replace("\"", "").toLowerCase();
			words_char_tetragram = review2.split(" ");

			for(int pi=0;pi<words_char_tetragram.length;pi++) 
			{
				for(int pl=0 ; pl <= words_char_tetragram[pi].length()-4 ; pl++) 
				{
					char_tetragram="";
					for(int pj=pl ;pj < pl+4 ; pj++) 
					{
						char_tetragram += "" + words_char_tetragram[pi].charAt(pj);
					}

					if(char_tetragram.trim() != "" && char_tetragram.length() ==4) 
					{
						if(!char_tetragramMap.containsKey(char_tetragram)) 
						{
							char_tetragramMap.put(char_tetragram, ++lastID);
						}
					}
				}
			}

			review3=line.substring(line.indexOf(',')+3);
			review3=review3.replace("\"", "").toLowerCase();
			words_char_pentagram = review3.split(" ");

			for(int pi=0;pi<words_char_pentagram.length;pi++) 
			{
				for(int pl=0 ; pl <= words_char_pentagram[pi].length()-5 ; pl++) 
				{
					char_pentagram="";
					for(int pj=pl ;pj < pl+5 ; pj++) 
					{
						char_pentagram += "" + words_char_pentagram[pi].charAt(pj);
					}

					if(char_pentagram.trim() != "" && char_pentagram.length() ==5) 
					{
						if(!char_pentagramMap.containsKey(char_pentagram)) 
						{
							char_pentagramMap.put(char_pentagram, ++lastID);
						}
					}
				}
			}

		}
		fr1=new FileReader("post_test");
		br1=new BufferedReader(fr1);
		String theLine="";
		String theReview="";
		String words[];
		
		while((theLine=br1.readLine())!=null)
		{
			theReview = theLine.substring(theLine.indexOf(',')+3);
			theReview = theReview.replace("\"", "").toLowerCase();
			words = theReview.split(" ");
			for(int i=0;i<words.length;i++) 
			{
				postagstr = words[i];
				if(postagstr.substring(postagstr.indexOf('_')).equals("_nn"))
				{
					if(!posTagAdjective.containsKey(postagstr))
						posTagAdjective.put(postagstr, ++lastID);
				}
				else if(postagstr.substring(postagstr.indexOf('_')).equals("_rb"))
				{
					if(!posTagAdverbs.containsKey(postagstr))
						posTagAdverbs.put(postagstr, ++lastID);
						
				}
				else if(postagstr.substring(postagstr.indexOf('_')).equals("_vb"));
				{
					if(!posTagVerbs.containsKey(postagstr))
						posTagVerbs.put(postagstr, ++lastID);
				}
			}
		}
		
		//System.out.println(lastID);
	}
	public void createInputFile() throws IOException
	{
		fr2=new FileReader("Train data");
		br=new BufferedReader(fr2);
		String line="";
		String review="";
		String[] words;
		String unigram,bigram,char_tetragram,char_pentagram, postagstr;
		File fileTwo=new File("svmTraining.txt");
		FileOutputStream fos=new FileOutputStream(fileTwo);
		PrintWriter pw=new PrintWriter(fos);
		HashMap<String,Integer> w_unigramCount;
		HashMap<String,Integer> w_bigramCount;
		HashMap<String, Integer> char_tetragramCount;
		HashMap<String, Integer> char_pentagramCount;
		HashMap<String,Integer> adjCount;
		HashMap<String,Integer> adverbCount;
		HashMap<String , Integer> verbCount;
		while((line=br.readLine())!=null)
		{
			w_unigramCount=new HashMap<String, Integer>();
			w_bigramCount=new HashMap<String, Integer>();
			char_tetragramCount=new HashMap<String, Integer>();
			char_pentagramCount=new HashMap<String, Integer>();
			adjCount=new HashMap<String, Integer>();
			adverbCount=new HashMap<String, Integer>();
			verbCount=new HashMap<String, Integer>();
			String sign=line.substring(0,1);
			String signmid;
			if(sign.charAt(0)=='0')
			{
				signmid=sign.replace('0', '1');
				sign="-"+signmid;
			}
			pw.print(sign);
			review = line.substring(line.indexOf(',')+3);
			review = review.replace("\"", "").toLowerCase();
			words=review.split(" ");
			for(int i=0;i<words.length;i++) 
			{
				unigram = words[i];
				

				if(w_unigramCount.get(unigram)==null)
					w_unigramCount.put(unigram, 1);
				else
					w_unigramCount.put(unigram, w_unigramCount.get(unigram)+1);
			}
			for(int i=0;i<words.length;i++) 
			{
				unigram = words[i];
				if(temp_uni.get(unigram)==null)
					temp_uni.put(unigram, 1);
				else
					temp_uni.put(unigram, temp_uni.get(unigram)+1);
				if(temp_uni.get(unigram)>1)
					continue;

				if(w_unigramMap.containsKey(unigram) && temp_uni.get(unigram)<2)
				{
					pw.print(" "+w_unigramMap.get(unigram)+":"+(float)w_unigramCount.get(unigram)/words.length+" ");

				}
			}
			for(int i=0;i<words.length;i++)
			{
				unigram= words[i];

				for(int jj=1;jj<2 && (i+jj) < words.length;jj++) 
				{
					unigram += " " + words[i+jj];
					if(w_bigramCount.get(unigram)==null)
						w_bigramCount.put(unigram, 1);
					else
						w_bigramCount.put(unigram, w_bigramCount.get(unigram)+1);
				}
			}
			for(int i=0;i<words.length;i++)
			{
				unigram= words[i];

				for(int jj=1;jj<2 && (i+jj) < words.length;jj++) 
				{
					unigram += " " + words[i+jj];
					if(temp_bi.get(unigram)==null)
						temp_bi.put(unigram, 1);
					else
						temp_bi.put(unigram, temp_bi.get(unigram)+1);
					if(temp_bi.get(unigram)>1)
						continue;
					if(w_bigramMap.containsKey(unigram) && temp_bi.get(unigram)<2)
					{
						pw.append(w_bigramMap.get(unigram)+":"+(float)w_bigramCount.get(unigram)/words.length+" ");
						break;
					}
				}

			}
			for(int pi=0;pi<words.length;pi++) 
			{
				for(int pl=0 ; pl <= words[pi].length()-4 ; pl++) 
				{
					char_tetragram="";
					for(int pj=pl ;pj < pl+4 ; pj++) 
					{
						char_tetragram += "" + words[pi].charAt(pj);
					}

					if(char_tetragram.trim() != "" && char_tetragram.length() ==4) 
					{
						if(char_tetragramCount.get(char_tetragram)==null)
							char_tetragramCount.put(char_tetragram, 1);
						else
							char_tetragramCount.put(char_tetragram, char_tetragramCount.get(char_tetragram)+1);
					}
				}
			}
			for(int pi=0;pi<words.length;pi++) 
			{
				for(int pl=0 ; pl <= words[pi].length()-4 ; pl++) 
				{
					char_tetragram="";
					for(int pj=pl ;pj < pl+4 ; pj++) 
					{
						char_tetragram += "" + words[pi].charAt(pj);
					}

					if(char_tetragram.trim() != "" && char_tetragram.length() ==4) 
					{
						if(temp_tetra.get(char_tetragram)==null)
							temp_tetra.put(char_tetragram, 1);
						else
							temp_tetra.put(char_tetragram, temp_tetra.get(char_tetragram)+1);
						if(temp_tetra.get(char_tetragram)>1)
							continue;
						if(char_tetragramMap.containsKey(char_tetragram) && temp_tetra.get(char_tetragram)<2) 
						{
							pw.append(char_tetragramMap.get(char_tetragram)+":"+(float)char_tetragramCount.get(char_tetragram)/words.length+" ");
						}

					}
				}
			}

			for(int qi=0;qi<words.length;qi++) 
			{
				for(int ql=0 ; ql <= words[qi].length()-5 ; ql++) 
				{
					char_pentagram="";
					for(int qj=ql ;qj < ql+5 ; qj++) 
					{
						char_pentagram += "" + words[qi].charAt(qj);
					}

					if(char_pentagram.trim() != "" && char_pentagram.length() ==5) 
					{
						if(char_pentagramCount.get(char_pentagram)==null)
							char_pentagramCount.put(char_pentagram, 1);
						else
							char_pentagramCount.put(char_pentagram, char_pentagramCount.get(char_pentagram)+1);
					}
				}
			}
			for(int qi=0;qi<words.length;qi++) 
			{
				for(int ql=0 ; ql <= words[qi].length()-5 ; ql++) 
				{
					char_pentagram="";
					for(int qj=ql ;qj < ql+5 ; qj++) 
					{
						char_pentagram += "" + words[qi].charAt(qj);
					}

					if(char_pentagram.trim() != "" && char_pentagram.length() ==5) 
					{
						if(temp_penta.get(char_pentagram)==null)
							temp_penta.put(char_pentagram, 1);
						else
							temp_penta.put(char_pentagram, temp_penta.get(char_pentagram)+1);
						if(temp_penta.get(char_pentagram)>1)
							continue;
						if(char_pentagramMap.containsKey(char_pentagram) && temp_penta.get(char_pentagram)<2) 
						{
							pw.append(char_pentagramMap.get(char_pentagram)+":"+(float)char_pentagramCount.get(char_pentagram)/words.length+" ");
						}

					}
				}
			}
			pw.print('\n');
		}
		pw.flush();
		pw.close();
		fos.close();
		

	}
	public static void main(String argsp[]) throws IOException
	{
		SVMTrainingFile stf=new SVMTrainingFile();
		stf.readTrainingFile("Train data");
		stf.createInputFile();
	}
}
