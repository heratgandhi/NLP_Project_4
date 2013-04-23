import java.io.*;
import java.util.ArrayList;

public class CharacterNGramKNN {
	public static int N = 2;
	public static int K = 3;
	
	static int FindSubStrCount(String str,String findStr) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1) {
			   lastIndex = str.indexOf(findStr,lastIndex);

		       if( lastIndex != -1) {
		             count ++;
		             lastIndex+=findStr.length();
		      }
		}
		return count;
	}
		
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
				parts[1] = parts[1].replaceAll(" ", "");
				Tuple t = new Tuple(parts[0],parts[1]);
				dictionary.add(t);
			}
			br.close();
			
			//Testing Portion
			BufferedReader brt = new BufferedReader(new FileReader("test_file"));
			String review = "";
			int[] max = new int[K];
			String[] val = new String[K];
			String[] words;
			String ngram;
			String[] ngrams;
			int cnt;
			int i,j;
			int ii,jj,kk;
			int zvotes,ovotes;
			int k_cnt;
			
			for(ii=0;ii<K;ii++) val[ii] = "";

			while((line=brt.readLine()) != null) {
				review = line.substring(line.indexOf(';')+1);
				review = review.replace("\"", "").toLowerCase();
				review = review.replaceAll(" ", "");
				
				ngrams = new String[review.length()];
				for(i=0;i<ngrams.length;i+=N) {
					ngram = review.charAt(i)+"";
					for(j=0;j<N;j++) {
						ngram += "" + review.charAt(j);
					}
					ngrams[i] = ngram;
				}
				k_cnt = 0;
				for(Tuple t : dictionary) {
					cnt = 0;
					for(kk=0;kk<ngrams.length;kk++) {
						if(ngrams[kk] != null) {
							if(t.review.contains(ngrams[kk])) {
								cnt++;
							}
						}						
					}
					if( cnt > 0 ) {
						System.out.println(t.rating);
						if(k_cnt == K) {
							jj = 0;
							for(ii=1;ii<K;ii++) {
								if(max[ii] < max[jj]) {
									jj = ii;
								}
							}
							if(max[jj] < cnt) {
								max[jj] = cnt;
								val[jj] = t.rating;
							}
						} else {
							max[k_cnt] = cnt;
							val[k_cnt] = t.rating;
							k_cnt++;
						}
					}
				}
				zvotes = 0;
				ovotes = 0;
				for(ii=0;ii<K;ii++) {
					if(val[ii].equals("0")) { 
						zvotes++;
					}
					else { 
						ovotes++;
					}
				}
				if(ovotes > zvotes) {
					System.out.println("1");
				} else {
					System.out.println("0");
				}
			}
			
			brt.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}