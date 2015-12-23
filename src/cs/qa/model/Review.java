package cs.qa.model;

public class Review implements IReview {
	
	/*
	 * This class represents a review (text and rating)
	 * It implements the "IReview" interface.
	 */
	
	private String text;
	private int rating;

	public Review() {
		super();
	}
	
	//Getters and Setters
	
	public Review(String text, int rating) {
		super();
		this.text = text;
		this.rating = rating;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

}
