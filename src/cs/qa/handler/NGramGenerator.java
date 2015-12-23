package cs.qa.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.qa.model.AnalyzedWord;
import cs.qa.model.NGram;
import cs.qa.model.Sentiment;

public class NGramGenerator {
	
	/*
	 * This class read a sentence in a form of "List<AnalyzedWord>" and returns the unigrams, bigrams and trigrams in the sentence.
	 * The generated ngrams are appended to the provided map of ngrams.
	 * The map of ngrams has the key as the degree (1,2 or 3), while the values are lists of ngram objects. 
	 */
	
	public static Map<Integer, Map<String, List<NGram>>> generateNGrams(List<AnalyzedWord> analyzedWords, Map<Integer, Map<String, List<NGram>>> ngrams){
		try{
			//Loop over the provided list of words
			for(int i=0; i<analyzedWords.size(); i++){
				for(int degree=1; degree<=3; degree++){ //degrees: 1,2 or 3
					if(i<=analyzedWords.size()-degree){
						
						String ngramLemma = "";
						Sentiment ngramSentiment = new Sentiment(0, 0); //initial ngram sentiment
						int startIndex = analyzedWords.get(i).getIndex(); //ngram start index
						int endIndex = analyzedWords.get(i+degree-1).getIndex(); //ngram end index
						
						for(int j=i; j<i+degree; j++){
							//Add words into the ngram
							ngramLemma += analyzedWords.get(j).getLemma() + (j<i+degree-1?" ":"");
							//Increment the sentiment numbers for the added words.
							ngramSentiment.setPositiveScore(ngramSentiment.getPositiveScore()+analyzedWords.get(j).getSentiment().getPositiveScore());
							ngramSentiment.setNegativeScore(ngramSentiment.getNegativeScore()+analyzedWords.get(j).getSentiment().getNegativeScore());
						}
						
						//The sentiment of an ngram is the average sentiment of all the words in it.
						ngramSentiment.setPositiveScore(ngramSentiment.getPositiveScore()/degree);
						ngramSentiment.setNegativeScore(ngramSentiment.getNegativeScore()/degree);
						ngramSentiment.setCount(degree);
						
						//Append the generated ngrams to the provided list of ngrams
						if(ngramLemma != null){
		        			if(!ngrams.get(degree).containsKey(ngramLemma)){
		        				ngrams.get(degree).put(ngramLemma, new ArrayList<NGram>());
		        			}
		        			ngrams.get(degree).get(ngramLemma).add(new NGram(degree, ngramLemma, startIndex, endIndex, ngramSentiment));
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println("Error generating the ngrams");
		}
		return ngrams;
	}

}
