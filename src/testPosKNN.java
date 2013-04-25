
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class testPosKNN {

	public static int K = 3;
	private MaxentTagger tagger;
	private String sample, tagged, token, tag, review, rating, actualRating;
	private FileInputStream fstream;
	private DataInputStream in;
	private BufferedReader br;
	private HashMap<String, posData> trainingMap, testMap;
	private posData posDataObj, tempObj, trainingObj;
	private StringTokenizer tokenizer;
	private int verbCount, adverbCount, adjectiveCount, trainingVerbCount, 
	trainingAdverbCount, trainingAdjectiveCount, readTrainingCount, readValidationCount;
	private double distance;
	private HashMap<posData, Double> distanceMap;
	private double numerator, denominator;
	private FileWriter fw;

	public testPosKNN(){
		tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		trainingMap = new HashMap<String, posData>();
		testMap = new HashMap<String, posData>();
		numerator = 0;
		denominator = 0;
		readTrainingCount = 0;
		readValidationCount = 0;
		
		tag();
	}

	private void tag(){

		trainModel();
		testModel();
		//processValidationData();
		
//		System.out.println("Numerator: " + numerator);
//		System.out.println("Denominator: " + denominator);
//		System.out.println("Accuracy: " + (numerator/denominator));

	}

	private void trainModel(){

		try {
			fstream = new FileInputStream("traind");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while((sample=br.readLine()) != null) {
				rating = sample.substring(0,1);
				review = sample.substring(4);
				tagged = tagger.tagString(review);

				//System.out.println("Review: " + review);
				//System.out.println("Tagged: " + tagged);
				
				posDataObj = processTags(tagged, rating, review);
				trainingMap.put(review, posDataObj);
				
				readTrainingCount++;
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
			fstream = new FileInputStream("testd");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			fw = new FileWriter("posTags.txt");

			actualRating = "";
			
			while((sample=br.readLine()) != null) {

//				actualRating = sample.substring(0,1);
				review = sample.substring(4);
				tagged = tagger.tagString(review);
				
				//System.out.println("Review: " + review);
				//System.out.println("Tagged: " + tagged);

				posDataObj = processTags(tagged, actualRating, review);
				testMap.put(review, posDataObj);
				//readValidationCount++;

				rating = computeKNN(posDataObj);
				fw.write(rating + "\n");
				
//				if(actualRating.equals(rating))
//					numerator++;
//				
//				denominator++;
				
			}

			br.close();
			fw.close();
			
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

		for(Entry<String, posData> testEntry : testMap.entrySet()){

			testPosDataObj = testEntry.getValue();
			rating = computeKNN(testPosDataObj);
			actualRating = testPosDataObj.getRating();
			
			System.out.println("Review: " + testPosDataObj.getReview());
			System.out.println("Rating: " + rating);
			System.out.println("Actual Rating: " + actualRating);
			System.out.println();
			
			if(actualRating.equals(rating))
				numerator++;
			
			denominator++;

		}//end testing for loop

		System.out.println("Review count: " + testMap.size());
		System.out.println("Numerator: " + numerator);
		System.out.println("Denominator: " + denominator);
		System.out.println("Accuracy: " + (numerator/denominator));
		
//		System.out.println("ReadTrainingCount: " + readTrainingCount);
//		System.out.println("ReadValidationCount: " + readValidationCount);
//		System.out.println("trainingMapSize: " + trainingMap.size());
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

		testPosKNN knn = new testPosKNN();
	}
}