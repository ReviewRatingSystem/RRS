package cs.qa.model;

public class Sentiment {
	
	/*
	 * This class represents sentiment scores (positive and negative).
	 */
	
	private float positiveScore;
	private float negativeScore;
	private int count;

	public Sentiment(float positiveScore, float negativeScore) {
		this.positiveScore = positiveScore;
		this.negativeScore = negativeScore;
	}
	
	public Sentiment(float positiveScore, float negativeScore, int count) {
		this.positiveScore = positiveScore;
		this.negativeScore = negativeScore;
		this.count = count;
	}

	//Getters and Setters
	
	public float getPositiveScore() {
		return positiveScore;
	}
	public void setPositiveScore(float positiveScore) {
		this.positiveScore = positiveScore;
	}
	
	public float getNegativeScore() {
		return negativeScore;
	}
	public void setNegativeScore(float negativeScore) {
		this.negativeScore = negativeScore;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
