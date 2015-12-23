package cs.qa.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs.qa.enums.Domain;
import cs.qa.enums.PosType;
import cs.qa.model.AnalyzedReview;
import cs.qa.model.AnalyzedWord;
import cs.qa.model.NGram;
import cs.qa.model.Review;
import cs.qa.model.Sentiment;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class ReviewAnalyzer {

	/*
	 * This class does review analysis; the input is a review and the output is an analyzed review
	 * The analysis includes: 	1) Tokenization
	 * 							2) Lemmatization
	 * 							3) POS tagging
	 * 							4) Exclusion of any word that is not NOUN, NOUN_PROP, ADV, ADJ, ADV or INTERJ
	 * 							5) Generation of uniframs, bigrams and trigrams
	 * 							6) Average sentiment calculation for the whole review.
	 */
	
	private static final String REGEX_NOUN_PROP = "^NNP.*$";
	private static final String REGEX_NOUN = "^NN.*$";
	private static final String REGEX_ADJECTIVE = "^JJ.*$";
	private static final String REGEX_VERB = "^VB.*$";
	private static final String REGEX_ADVERB = "^RB.*$";
	private static final String REGEX_INTERJECTION = "^UH.*$";
	
	private static final String REGEX_NEGATION = "^(no|not|never)$";
	private static final String REGEX_EXCESS = "^(lot|many|much|more|most)$";
	private static final String REGEX_LACK = "^(few|little|less|least|bit)$";
	private static final String PROP_NOUN_LEMMA = "prop_noun";
	
	public static AnalyzedReview analyze(Review review, Domain domain){
		AnalyzedReview analyzedReview = new AnalyzedReview(domain);
		try{
			//The output list of the analyzed words for the provided review
			List<AnalyzedWord> allAnalyzedWords = new ArrayList<AnalyzedWord>();
			
			//Initialzie the Ngrams
			Map<Integer, Map<String, List<NGram>>> ngrams = new HashMap<Integer, Map<String, List<NGram>>>();
			ngrams.put(new Integer(1), new HashMap<String, List<NGram>>());
			ngrams.put(new Integer(2), new HashMap<String, List<NGram>>());
			ngrams.put(new Integer(3), new HashMap<String, List<NGram>>());
		
			//Data cleanup
			review.setText(review.getText().replaceAll("(.)(\\1)(\\1)+", "$1"));
			
			//Annotate the title for tokenization, sentence-split, POS and lemma
			Annotation document = new Annotation(review.getText());
			ResourceFactory.getStanfordCoreNLP().annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			
			String previoisWord = "";
			int index = -1;
			Sentiment totalSentiment = new Sentiment(0, 0);
			
			String tokenizedText = "";
			
			//Loop over the sentences in the document
			for(int i=0; i<sentences.size(); i++) {
				CoreMap sentence = sentences.get(i);
				List<CoreLabel> coreLabels = sentence.get(TokensAnnotation.class);
				
				//current analyzed words
				List<AnalyzedWord> analyzedWords = new ArrayList<AnalyzedWord>();
								
				//Loop over the tokens in the document to generate lemma and size information
				for (int j=0; j<coreLabels.size(); j++,index++) {					
					//Text
					String word = coreLabels.get(j).get(TextAnnotation.class); //word
					tokenizedText += word+" ";

					//POS
					String pos = coreLabels.get(j).get(PartOfSpeechAnnotation.class); //POS
					PosType posType = null;
					if(pos.matches(REGEX_NOUN_PROP)){
						posType = PosType.PROP_NOUN;
					}else if(pos.matches(REGEX_NOUN)){
						posType = PosType.NOUN;
					}else if(pos.matches(REGEX_ADJECTIVE)){
						posType = PosType.ADJ;
					}else if(pos.matches(REGEX_VERB)){
						posType = PosType.VERB;
					}else if(pos.matches(REGEX_ADVERB)){
						posType = PosType.ADV;
					}else if(pos.matches(REGEX_INTERJECTION)){
						posType = PosType.INTERJ;
					}
					
					//Exclude any words that is not NOUN, NOUN_PROP, ADV, ADJ, ADV or INTERJ
					if(posType==null)
						continue;
				
					//Lemma
					String lemma = word; 
					//Replace proper noun with a constant string "prop_noun"
					if(posType == PosType.PROP_NOUN){
						lemma = PROP_NOUN_LEMMA;
					}else{
						lemma = coreLabels.get(j).get(LemmaAnnotation.class).toLowerCase();
					}
					
					//Exclude lemmas that have character other than letters and "_".
					if(!lemma.matches("^[a-z\\_]+$"))
						continue;
					
					//Accumulate the sentiment
					Sentiment sentiment = SentimentHandler.getSentiment(lemma, posType, previoisWord.matches(REGEX_NEGATION), previoisWord.matches(REGEX_EXCESS), previoisWord.matches(REGEX_LACK));
					totalSentiment.setPositiveScore(totalSentiment.getPositiveScore()+sentiment.getPositiveScore());
					totalSentiment.setNegativeScore(totalSentiment.getNegativeScore()+sentiment.getNegativeScore());	
					
					//Record the current analyzed word
					
					analyzedWords.add(new AnalyzedWord(index, word, lemma, posType, sentiment));
					previoisWord = word;
				}
				
				//Generate the ngrams for the current sentence
				NGramGenerator.generateNGrams(analyzedWords, ngrams);
				
				//Append the words of the current sentence
				allAnalyzedWords.addAll(analyzedWords);
				
			}

			//Calculate the avaerage sentiment
			Sentiment averageSentiment = new Sentiment(0,0);
			averageSentiment.setPositiveScore(totalSentiment.getPositiveScore()/allAnalyzedWords.size());
			averageSentiment.setNegativeScore(totalSentiment.getNegativeScore()/allAnalyzedWords.size());
			averageSentiment.setCount(allAnalyzedWords.size());
			
			//Record the analysis information
			analyzedReview.setText(review.getText());
			analyzedReview.setTokenizedText(tokenizedText);
			analyzedReview.setAnalyzedWords(allAnalyzedWords);
			analyzedReview.setUniGrams(ngrams.get(1));
			analyzedReview.setBiGrams(ngrams.get(2));
			analyzedReview.setTriGrams(ngrams.get(3));
			analyzedReview.setSentiment(averageSentiment);
			analyzedReview.setText(review.getText());
			analyzedReview.setRating(review.getRating());
			
		}catch(Exception e){
			System.out.println("Error analyzing the review "+review);
		}
		return analyzedReview;
	}
	
	
}
