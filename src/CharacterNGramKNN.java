import java.io.*;
import java.util.*;

class CharNGramsTuple extends Tuple {

	Hashtable<String,Integer> ngrams_hash;
	
	CharNGramsTuple(String rating, String review) {
		super(rating, review);
		
		//put ngrams with counter in the hashtable
		String[] words;
		String ngram;
		words = review.split(" ");
		ngrams_hash = new Hashtable<String, Integer>();
		for(int i=0;i<words.length;i++) {
			for(int l=0 ; l <= words[i].length()-CharacterNGramKNN.N ; l++) {
				ngram = "";
				for(int j=l ;j < l+CharacterNGramKNN.N ; j++) {
					ngram += "" + words[i].charAt(j);
				}
				if(ngram.trim() != "" && ngram.length() == CharacterNGramKNN.N) {
					if(ngrams_hash.get(ngram) == null) {
						ngrams_hash.put(ngram, 1);
					} else {
						ngrams_hash.put(ngram, ngrams_hash.get(ngram)+1);
					}
				}
			}					
		}
		// TODO Auto-generated constructor stub
	}
	
}

public class CharacterNGramKNN {
	public static int N = 3;
	public static int K = 3;
	
	public static void main(String[] args) {
		try {
			//Training Portion
			BufferedReader br = new BufferedReader(new FileReader("traind"));
			String line="";
			
			ArrayList<CharNGramsTuple> dictionary = new ArrayList<CharNGramsTuple>();
			String[] parts;
			CharNGramsTuple ti;
			while((line=br.readLine()) != null) {
				parts = line.split(",");
				parts[2] = parts[2].replaceAll("\"", "").toLowerCase();
				ti = new CharNGramsTuple(parts[0],parts[2]);
				dictionary.add(ti);
			}
			br.close();
			
			/*for(CharNGramsTuple t : dictionary) {
				Enumeration<String> enumKey = t.ngrams_hash.keys();
				while(enumKey.hasMoreElements()) {
				    String key = enumKey.nextElement();
				    Integer value = t.ngrams_hash.get(key);
				    System.out.println(key+" "+value);
				}
			}*/
			//Testing Portion
			BufferedReader brt = new BufferedReader(new FileReader("testd"));
			BufferedWriter bwt = new BufferedWriter(new FileWriter("charncount_output"));
			
			String review = "";
			double[] max = new double[K];
			String[] val = new String[K];
			String[] words;
			String ngram;
			Hashtable<String,Integer> test_dict;
			double distance = 0;
			int i,j,l;
			int ii,jj;
			int zvotes,ovotes;
			int k_cnt;
			
			for(ii=0;ii<K;ii++) val[ii] = "";

			while((line=brt.readLine()) != null) {
				review = line.split(",")[2];
				//review = line.substring(line.indexOf(';')+1);
				review = review.replace("\"", "").toLowerCase();
				
				test_dict = new Hashtable<String, Integer>();
				words = review.split(" ");
				/*for(i=0;i<words.length;i++) {
					ngram = words[i];
					for(j=1;j<N && (i+j) < words.length;j++) {
						ngram += " " + words[i+j];
					}
					if(test_dict.get(ngram) == null) {
						test_dict.put(ngram, 1);
					} else {
						test_dict.put(ngram, test_dict.get(ngram)+1);
					}			
				}*/
				for(i=0;i<words.length;i++) {
					for(l=0 ; l <= words[i].length()-CharacterNGramKNN.N ; l++) {
						ngram = "";
						for(j=l ;j < l+CharacterNGramKNN.N ; j++) {
							ngram += "" + words[i].charAt(j);
						}
						if(ngram.trim() != "" && ngram.length() == CharacterNGramKNN.N) {
							if(test_dict.get(ngram) == null) {
								test_dict.put(ngram, 1);
							} else {
								test_dict.put(ngram, test_dict.get(ngram)+1);
							}
						}
					}
				}
				
				k_cnt = 0;
				for(CharNGramsTuple t : dictionary) {
					distance = 0;
					
					Enumeration<String> enumKey = test_dict.keys();
					while(enumKey.hasMoreElements()) {
					    String key = enumKey.nextElement();
					    Integer value = test_dict.get(key);
					    if(t.ngrams_hash.get(key) != null) {
					    	distance += Math.abs(value - t.ngrams_hash.get(key));
					    } else {
					    	distance += Math.abs(value);
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
					bwt.write("1\n");
					System.out.println("1");
				} else {
					bwt.write("0\n");
					System.out.println("0");
				}
			}
			bwt.close();
			brt.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
