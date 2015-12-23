package cs.qa.model;

import cs.qa.enums.PosType;

public class AnalyzedWord {
	
	/*
	 * This class represents an analyzed word:
	 * The analysis includes: lemmatization, POS tagging and sentiment assignment.
	 */
	
	private int index;
	private String word;
	private String lemma;
	private PosType posType;
	private Sentiment sentiment;

	public AnalyzedWord(int index, String word, String lemma, PosType posType, Sentiment sentiment) {
		this.index = index;
		this.word = word;
		this.lemma = lemma;
		this.posType = posType;
		this.sentiment = sentiment;
	}
		
	//Getters and Setters
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}

	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public PosType getPosType() {
		return posType;
	}
	public void setPosType(PosType posType) {
		this.posType = posType;
	}
	
	public Sentiment getSentiment() {
		return sentiment;
	}
	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

}
