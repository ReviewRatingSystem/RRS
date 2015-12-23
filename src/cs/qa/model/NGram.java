package cs.qa.model;

public class NGram {
	
	/*
	 * This class represent an ngram expression
	 * "degree" is the value of N of the Ngram.
	 * The "text" parameter is lemmatized (lemmas not words).
	 * "startIndex" and "endText" are the actual indexes if the original review after tokenization and before any further processing.
	 * "sentiment" is the average sentiment of the lemmas in the ngram. 
	 */
	
	private int degree;
	private String text;
	private int startIndex;
	private int endIndex;
	private Sentiment sentiment;
	
	public NGram(int degree, String text, int startIndex, int endIndex, Sentiment sentiment) {
		super();
		this.degree = degree;
		this.text = text;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.sentiment = sentiment;
	}
	
	//Getters and Setters
	
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public Sentiment getSentiment() {
		return sentiment;
	}
	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

}
