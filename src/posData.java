
public class posData {

	private String rating, review;
	private int verbCount, adverbCount, adjectiveCount;

	public posData(String rating1, String review1){
		rating = rating1;
		review = review1;
		verbCount = 0;
		adverbCount = 0;
		adjectiveCount = 0;
	}
	
	public void incrementVerbCount(){
		verbCount++;
	}
	
	public int getVerbCount(){
		return verbCount;
	}
	
	public void incrementAdverbCount(){
		adverbCount++;
	}
	
	public int getAdverbCount(){
		return adverbCount;
	}
	
	public void incrementAdjectiveCount(){
		adjectiveCount++;
	}
	
	public int getAdjectiveCount(){
		return adjectiveCount;
	}
	
	public String getRating(){
		return rating;
	}
	
	public String getReview(){
		return review;
	}
}
