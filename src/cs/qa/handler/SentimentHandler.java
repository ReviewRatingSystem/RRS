package cs.qa.handler;

import cs.qa.enums.PosType;
import cs.qa.model.Sentiment;

public class SentimentHandler {
	
	/*
	 * This class returns the sentiment of a (lemma, POS) pair by looking it up in SentiWordHandler.
	 * If the lemma is preceded by a negation, then flip the positive and negative scores.
	 * If a lemma is preceded by an excess expression, then multiple the scores by "2" (but do not exceed "1").
	 * If a lemma is preceded by a lack expression, then divide the scores by "2".
	 */
	
	public static Sentiment getSentiment(String lemma, PosType pos, boolean negation, boolean excess, boolean lack){
		Sentiment sentiment = new Sentiment(0,0);
		try{
			//Read the sentiment scores from SentiWordNet
			if(ResourceFactory.getSentiWordNet().containsKey(lemma)){
				if(ResourceFactory.getSentiWordNet().get(lemma).containsKey(pos)){
					sentiment = ResourceFactory.getSentiWordNet().get(lemma).get(pos);
				}else{
					sentiment = ResourceFactory.getSentiWordNet().get(lemma).get(PosType.ANY);
				}
			}else{
				sentiment = new Sentiment(0,0);
			}
			
			//If the lemma is preceded by a negation, then flip the positive and negative scores.
			if(negation){
				float temp = sentiment.getPositiveScore();
				sentiment.setPositiveScore(sentiment.getNegativeScore());
				sentiment.setNegativeScore(temp);
			}
			
			//If a lemma is preceded by an excess expression, then multiple the scores by "2" (but do not exceed "1").
			if(excess){
				sentiment.setPositiveScore(sentiment.getPositiveScore()*2>1?1:sentiment.getPositiveScore()*2);
				sentiment.setNegativeScore(sentiment.getNegativeScore()*2>1?1:sentiment.getNegativeScore()*2);
			}
			
			//If a lemma is preceded by a lack expression, then divide the scores by "2".
			if(lack){
				sentiment.setPositiveScore(sentiment.getPositiveScore()/2);
				sentiment.setNegativeScore(sentiment.getNegativeScore()/2);
			}
		}catch(Exception e){
			System.out.println("Error reading the sentiment for "+lemma+","+pos.getValue()+"!");
		}
		return sentiment;
	}

}
