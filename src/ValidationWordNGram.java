import java.io.*;
import java.util.*;

public class ValidationWordNGram {
	public static int N = 2;
	public static int K = 3;
	
	public static void main(String[] args) {
		try {
			//Training Portion
			BufferedReader br = new BufferedReader(new FileReader("traind"));
			String line="";
			
			ArrayList<NGramsTuple> dictionary = new ArrayList<NGramsTuple>();
			String[] parts;
			NGramsTuple ti;
			while((line=br.readLine()) != null) {
				parts = line.split(",");
				parts[2] = parts[2].replaceAll("\"", "").toLowerCase();
				ti = new NGramsTuple(parts[0],parts[2]);
				dictionary.add(ti);
			}
			br.close();
			
			//Testing Portion
			BufferedReader brt = new BufferedReader(new FileReader("validationd"));
			BufferedWriter bwt = new BufferedWriter(new FileWriter("wordncount_output_valid"));
			
			String review = "";
			double[] max = new double[K];
			String[] val = new String[K];
			String[] words;
			String ngram;
			Hashtable<String,Integer> test_dict;
			double distance = 0;
			int i,j;
			int ii,jj;
			int zvotes,ovotes;
			int k_cnt;
			
			for(ii=0;ii<K;ii++) val[ii] = "";
			
			int correctop = 0;
			int total = 0;

			while((line=brt.readLine()) != null) {
				review = line.split(",")[2];
				//review = line.substring(line.indexOf(';')+1);
				review = review.replace("\"", "").toLowerCase();
				
				test_dict = new Hashtable<String, Integer>();
				words = review.split(" ");
				for(i=0;i<words.length;i++) {
					ngram = words[i];
					for(j=1;j<WordNGramKNN.N && (i+j) < words.length;j++) {
						ngram += " " + words[i+j];
					}
					if(test_dict.get(ngram) == null) {
						test_dict.put(ngram, 1);
					} else {
						test_dict.put(ngram, test_dict.get(ngram)+1);
					}			
				}
				
				k_cnt = 0;
				for(NGramsTuple t : dictionary) {
					distance = 0;
					
					Enumeration<String> enumKey = test_dict.keys();
					while(enumKey.hasMoreElements()) {
					    String key = enumKey.nextElement();
					    Integer value = test_dict.get(key);
					    if(t.ngrams_hash.get(key) != null) {
					    	distance += Math.pow(value - t.ngrams_hash.get(key), 2);
					    } else {
					    	distance += Math.pow(value, 2);
					    }
					}
					distance = Math.sqrt(distance);
					
					//System.out.println(distance);
					
					if(k_cnt == K) {
						jj = 0;
						for(ii=1;ii<K;ii++) {
							if(max[ii] > max[jj]) {
								jj = ii;
							}
						}
						if(max[jj] > distance) {
							max[jj] = distance;
							val[jj] = t.rating;
						}
					} else {
						max[k_cnt] = distance;
						val[k_cnt] = t.rating;
						k_cnt++;
					}					
				}
				zvotes = 0;
				ovotes = 0;
				for(ii=0;ii<K;ii++) {
					if(val[ii].equals("0") || val[ii].equals("")) { 
						zvotes++;
					}
					else { 
						ovotes++;
					}
				}
				if(ovotes > zvotes) {
					System.out.println("1");
					bwt.write("1\n");
					if(Integer.parseInt(line.split(",")[0]) == 1) {
						correctop++;
					}
				} else {
					System.out.println("0");
					bwt.write("0\n");
					if(Integer.parseInt(line.split(",")[0]) == 0) {
						correctop++;
					}
				}
				total++;
			}
			System.out.println(correctop/(float)total);
			bwt.close();
			brt.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
