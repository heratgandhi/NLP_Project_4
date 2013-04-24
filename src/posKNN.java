
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class posKNN {

	public static int K = 3;
	private MaxentTagger tagger;
	private String sample, tagged, token, tag, review;
	private FileInputStream fstream;
	private DataInputStream in;
	private BufferedReader br;
	private FileWriter q;
	private BufferedWriter out;
	private HashMap<String, posData> trainingMap, testingMap;
	private String[] parts;
	private posData posDataObj, tempObj, trainingObj;
	private StringTokenizer tokenizer;
	private int verbCount, adverbCount, adjectiveCount, trainingVerbCount, 
	trainingAdverbCount, trainingAdjectiveCount;
	private double distance;
	private HashMap<posData, Double> distanceMap;

	public posKNN(){
		tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		trainingMap = new HashMap<String, posData>();
		testingMap = new HashMap<String, posData>();
		tag();
	}

	private void tag(){

		trainModel();
		testModel();
		processTestData();
	}

	private void trainModel(){

		try {
			fstream = new FileInputStream("training_file");

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while((sample=br.readLine()) != null) {
				parts = sample.split(";");
				parts[1] = parts[1].replaceAll("\"", "").toLowerCase();
				tagged = tagger.tagString(sample);

				posDataObj = processTags(tagged, parts[0], parts[1]);
				trainingMap.put(parts[1], posDataObj);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private posData processTags(String tags, String rating1, String review1){

		tempObj = new posData(rating1, review1);
		tokenizer = new StringTokenizer(tags); 

		while(tokenizer.hasMoreTokens()){
			token = tokenizer.nextToken();
			tag = token.substring(token.indexOf("/") + 1);

			if(tag.contains("JJ"))
				tempObj.incrementAdjectiveCount();
			else if(tag.contains("RB"))
				tempObj.incrementAdverbCount();
			else if(tag.contains("VB"))
				tempObj.incrementVerbCount();
		}

		return tempObj;
	}

	private void testModel(){

		try {
			fstream = new FileInputStream("test_file");

			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while((sample=br.readLine()) != null) {

				review = sample.substring(sample.indexOf(';')+1);
				review = review.replace("\"", "").toLowerCase();

				tagged = tagger.tagString(review);

				posDataObj = processTags(tagged, "", review);
				testingMap.put(review, posDataObj);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processTestData(){

		posData testPosDataObj;
		String rating;
		int counter = 0;

		for(Entry<String, posData> testEntry : testingMap.entrySet()){

			testPosDataObj = testEntry.getValue();
			rating = computeKNN(testPosDataObj);

			System.out.println("Review: " + testPosDataObj.getReview());
			System.out.println("Rating: " + rating);
			System.out.println();
			counter++;

		}//end testing for loop
		
		System.out.println("Review count: " + counter);
	}

	private String computeKNN(posData testPosDataObj){

		ArrayList<String> nearestNeighbors = new ArrayList<String>();
		distanceMap = new HashMap<posData, Double>();
		verbCount = testPosDataObj.getVerbCount();
		adverbCount = testPosDataObj.getAdverbCount();
		adjectiveCount = testPosDataObj.getAdjectiveCount();

		for(Entry<String, posData> trainingEntry : trainingMap.entrySet()){

			trainingObj = trainingEntry.getValue();
			trainingVerbCount = trainingObj.getVerbCount();
			trainingAdverbCount = trainingObj.getAdverbCount();
			trainingAdjectiveCount = trainingObj.getAdjectiveCount();

			//Taking weighted sums: 40, 40, 20 for adverbs, adjectives, verbs respectively
			distance = 0.4 * Math.pow((adverbCount - trainingAdverbCount), 2);
			distance += 0.4 * Math.pow((adjectiveCount - trainingAdjectiveCount), 2);
			distance += 0.2 * Math.pow((verbCount - trainingVerbCount), 2);
			distance = Math.sqrt(distance);

			distanceMap.put(trainingObj, distance);

		}

		double min = Double.MAX_VALUE;
		double tempDistance;
		posData minPosDataObj = null;

		for(int i=0; i < K; i++){

			if(distanceMap.isEmpty())
				break;

			for(Entry<posData, Double> distanceEntry : distanceMap.entrySet()){
				tempDistance = distanceEntry.getValue();

				if(tempDistance <= min){


					min = tempDistance;
					minPosDataObj = distanceEntry.getKey();

				}

			}//end inner for loop


			if(minPosDataObj != null){
				//directly storing the ratings to find the max rating later
				nearestNeighbors.add(minPosDataObj.getRating());
				distanceMap.remove(minPosDataObj);
			}

		}//end outer for loop (K loop)

		if(nearestNeighbors.isEmpty())
			return "0";

		int oneCount = 0;
		int zeroCount = 0;

		for(String rating : nearestNeighbors){

			if(rating.equals("0"))
				zeroCount++;
			else if(rating.equals("1"))
				oneCount++;

		}

		if(zeroCount >= oneCount)
			return "0";
		else
			return "1";

	}

	public static void main(String[] args){

		posKNN knn = new posKNN();
	}
}