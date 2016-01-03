package cs.qa.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import weka.core.Instance;
import cs.qa.enums.Domain;
import cs.qa.handler.ResourceFactory;
import cs.qa.handler.ReviewAnalyzer;

public class AnalyzedReview extends Review {
	
	/*
	 * This class represents an analyzed review:
	 * The analysis includes: tokenization, lemmatization, POS tagging, ngram generation and sentiment assignment.
	 * It extends the "Review" superclass, which implements the "IReview" interface.
	 */
	
	public static void main(String[] args) throws ServletException {
		new ResourceFactory().init();
		LinkedList<AnalyzedReview> l = new LinkedList<AnalyzedReview>();
		
		//String test = "[[not]] [[bad]]";
		//System.out.println(test.matches("[^(\\]\\])]*(not)(\\]\\])?(\\s)(.*)"));
		//l.add(ReviewAnalyzer.analyze(new Review("hello Michael, hello everybody. This is a review about a doctor that I do not like and hello. what's up", 2), Domain.HEALTH));
		l.add(ReviewAnalyzer.analyze(new Review("It's good, but I cannot accept the taste of some of their vegetables.........", 4), Domain.RESTAURANT));
		//AnalyzedReview r = ReviewAnalyzer.analyze(new Review("hello this is a review about a doctor that I do not like, it is terrible and awful.", 2), Domain.HEALTH);
		//AnalyzedReview r = ReviewAnalyzer.analyze(new Review("Dig Inn is consistently on-point for its healthy selection, affordable prices, and tasty choices. It is a favorite of mine in the \"fast casual\" segment. It is rare to be able to eat delicious and healthy food at this price range. The restaurant focuses on market plates, consisting of a protein (can order vegetarian with tofu or no protein) with two sides and rice (several choices). There is a large size that makes for a very filling meal. The protein options include grilled steak, salmon, chicken, and meatballs. The sides include delicious Mac n cheese, Brussels sprouts, and kale salad, among other choices. Additionally, there is a salad bar option. There are also a wide selection of fresh iced teas, all of which are very delicious. Overall, expect price range for a meal (plate + drink) between $ 12-15. Very reasonable given the quality of the options.", 4), Domain.RESTAURANT);
		l.add(ReviewAnalyzer.analyze(new Review("My friend will not stop raving about the corn on the cob she ate for lunch today at Dig Inn. I got a big market plate with steak. Portion size is good but man, what an awful piece of steak. It was tough, chewy, flavorless and underdone. I do like my steak rare but having to chew a slice of almost raw beef for 10 minutes is not pleasant. My receipt says the steak was grilled and chilled. Why chill it??? I got lentil and squash as sides and both were great choices.", 4), Domain.RESTAURANT));
		l.add(ReviewAnalyzer.analyze(new Review("Love and hate (mostly love) relationship. Dig Inn definitely sends the message that you can look beyond steamed tilapia and broccoli to eat clean. Every single dish is flavorful and definitely doesn't give you consumer remorse like some other clean eating restaurants, where you feel like you could have made everything at home by yourself. I particularly recommend the perfectly cooked salmon which is drizzled with pesto, the bulgur, and chilled quinoa. But I have never had a dish that I dislike here. (upstate mac and roasted beets can be my least favorite) However, I have heard multiple complaints from friends that portion size here is inconsistent. Today I got the kale+rhubarb salad and only received 5 leaves of kale (I counted). Granted, I still left satisfied thanks to the generous portions of bulgur and upstate mac. Another suggestion I have is, instead of having the main come automatically on top of a carb along with 2 sides, let customers choose 3 sides, which would be better for those eating paleo. FYI you can claim rewards if you use LevelUp. They also sel,l a few juices and cookies by the good batch (same ice cream sandwich vendor at smorgasburg)", 4), Domain.RESTAURANT));
		l.add(ReviewAnalyzer.analyze(new Review("Absolutely fantastic food. I love the concept and the execution was great. Moves like a Chipotle line (super fast service), tastes like fine dining. Has this great, open, fresh ambience. Think wood furniture and lots of daylight. I also loved their book selection - I was perusing their copy of Pollan before my friends arrived. I ordered the grilled salmon with sweet potatoes and their seasonal veggies. The salmon was delicious and grilled to perfection. It was topped with this lovely green sauce (peas, maybe?) and sat on a bed of some type of savory grains that was also amazingly flavored and soft in texture. The sweet potatoes were roasted so nicely - slightly crispy on the outside, melt-in-your-mouth smooth on the inside. And the seasonal veggies (spinach and some other veggies) were fantastic. Flavoring was so nice and clean. Nothing was bland or boring. I was so impressed. Also had their vanilla mint tea, which was delicious and refreshing. Cannot recommend this place enough. You literally feel so healthy and good about yourself after you eat here (successful marketing or just damn good food?). I'm definitely coming back.", 5), Domain.RESTAURANT));
		l.add(ReviewAnalyzer.analyze(new Review("Ugh! SOOOO RUDE!! Not only is the receptionist not friendly, the ugly fish mobile drives me crazy while i'm sitting in the chair.  I went here a few times and while nothing was great or nothing too bad i always thought-- 'why am i going here, it's old, boring and does not have state of the art equipment' but it was so close to work and free with my insurance.  now after someone calling once to remind me a about an appointment, i missed an appointment and i get 4 calls in 25 minutes to make sure I pay the $25 cancellation policy.  I asked since it was my first time to get it refunded and the receptionist was still . . . rude. I am NEVER going here again.", 2), Domain.HEALTH));
		l.add(ReviewAnalyzer.analyze(new Review("IGNORE ALL NEGATIVE REVIEWS. The dental office has changed a lot, I believe, in the past couple months. They have taken a different direction in which the office does not have anymore interns. I visited this office for the first time in June for a quick cleaning. From there, they discovered a HUGE cavity that was to be treated immediately. This is when I met Dr. Oh, an amazing dentist who led me through my whole procedure, step-by-step, and made sure I was aware of what was going on. He mentioned the prices and said I was welcome to consider other dental offices too. However, this office also provides a student discount, and is very ideal for those that are on a budget. My cavity removal/treatment required several appointments, but each time, with Dr. Oh and the dental assistant, I felt very comfortable and respected. Also the entire procedure was pain-free, which I was very thankful for. I would definitely go again if any dental issues arise and would easily recommend the office to anyone.", 5), Domain.HEALTH));
		l.add(ReviewAnalyzer.analyze(new Review("The hygienist did a thorough job... However, Dr. Davis-Bell 'saw' 5 cavities that didn't exist (which was confirmed later by another dental office)... She wanted me to get them filled by 'the boys'.  Apparently they need guinea pigs for their interns to practice on, so why not use Columbia grad students? I tried to take my X-rays with me, but they didn't let me.  Very suspicious.", 1), Domain.HEALTH));
		l.add(ReviewAnalyzer.analyze(new Review("I finally wandered in to Book Culture, after tickling through the 'seconds' book trucks displayed outside the store for a few months.  What a pleasant surprise:  books and sundries laid out in a spacious, clean, browser friendly setting.  Books are separated by genre, or by 'new books', and children's books, as well as many seconds to hunt through.  I found a 2016 Cosmos calendar in the seconds section for a great price.   The jewel in this store is the writing desk service.  An enormous desk is available for patrons to write letters.  The bookstore sends out these letters (they provide the stamps), once a week.  This is not only romantic (you should see this desk!), handwriting a letter forces you to be less edited, more pure, and to freely scratch out your mistakes and over loop your cursive.  I will definitely be using this service.   I found the employees very  helpful, knowledgeable, and the key ingredient to all retail: quick at the cash register.  No long lines with lost or bored employees. The location is easy to find, I noticed it was dog friendly, and near a Starbucks - there's nothing like coffee and books to bring out the brain in you.", 5), Domain.SHOPPING));
		l.add(ReviewAnalyzer.analyze(new Review("One measure of a good bookstore is that at some point your inner accountant demands that you get the hell out of there. If you're Yelping for bookstores, then you know what I'm talking about--that stack of books cradled in your arm keeps growing and growing as you continue to find your favorite authors, or you discover something you've never heard of but just reading the description lets you know it's gonna scratch you right where you itch. So that's Book Culture. This location is very inviting, with most of their new fiction downstairs and reduced/used stuff upstairs. Bargain books, of which there are many excellent choices, are outside on the carts and pretty much anywhere they can fit them (tops of shelves, staircases, etc.). The second floor also has a lot of the assigned reading for Columbia. I'm not sure if old feckers like me who already done got they edumacation are allowed to buy these course list books, but it's interesting to look. For instance, here's a class on Roberto Bolano that assigns 8 of his books. And they're ALL translated by the professor teaching the class! Nicely played, profesora. I'm looking forward to visiting their other store locations, but I know this one will be a regular stop for me.", 4), Domain.SHOPPING));
		l.add(ReviewAnalyzer.analyze(new Review("I had a terrible experience here. After a cleaning, they told me the dentist wasn't there and I'd have to come back for a second visit, and that the whole thing would cost over $500. When I refused the second visit, they tried to charge me more money to email my x-rays to another dentist. The hygienist was extremely thorough - it was the most painful cleaning I've ever had - but I have my doubts about how necessary that level of roughness is.", 1), Domain.HEALTH));
		l.add(ReviewAnalyzer.analyze(new Review("Good", 4), Domain.RESTAURANT));
		l.add(ReviewAnalyzer.analyze(new Review("", 4), Domain.RESTAURANT));
		for(AnalyzedReview r : l) {
			System.out.println(r.tagTextForRelevantNGrams());
		}
		
	}
	
	private Domain domain;
	private String tokenizedText;
	private List<AnalyzedWord> analyzedWords;
	private Map<String, List<NGram>> uniGrams;
	private Map<String, List<NGram>> biGrams;
	private Map<String, List<NGram>> triGrams;
	private Sentiment sentiment;
	private ArrayList<Highlight> highlights;
	
	private static class Highlight implements Comparable<Highlight> {
		public int startIndex;
		public int endIndex;
		
		public Highlight(int s, int e) {
			this.startIndex = s;
			this.endIndex = e;
		}
		
		public Highlight(NGram n) {
			this.startIndex = n.getStartIndex();
			this.endIndex = n.getEndIndex();
		}
		
		public int compareTo(Highlight h) {
			if(h.startIndex > this.startIndex) {
				return -1;
			} else if(h.startIndex < this.startIndex) {
				return 1;
			} else if(h.endIndex > this.endIndex) {
				return -1;
			} else if(h.endIndex < this.endIndex) { 
				return 1;
			} else {
				return 0;
			}
		}
		
		public static boolean mergeable(Highlight h1, Highlight h2) {
			return h1.startIndex <= h2.endIndex &&   h1.startIndex >= h2.startIndex || 
				   h2.startIndex <= h1.endIndex &&   h2.startIndex >= h1.startIndex ||
				   h1.endIndex >= h2.startIndex &&   h1.endIndex <= h2.endIndex ||
				   h2.endIndex >= h1.startIndex &&   h2.endIndex <= h1.endIndex;
		}
		
		public static Highlight merge(Highlight h1, Highlight h2) {
			if(!mergeable(h1,h2)) {
				return null;
			} else {
				return new Highlight(Math.min(h1.startIndex, h2.startIndex), Math.max(h1.endIndex, h2.endIndex));
			}
		}
	}
	
	public AnalyzedReview(Domain domain){
		super();
		this.domain = domain;
		this.highlights = new ArrayList<Highlight>();
	}
	
	public AnalyzedReview(String text, String tokenizedText, List<AnalyzedWord> analyzedWords, 
			Map<String, List<NGram>> uniGrams, Map<String, List<NGram>> biGrams, Map<String, List<NGram>> triGrams, Sentiment sentiment,
			int rating, Domain domain) {
		super(text, rating);
		this.domain = domain;
		this.highlights = new ArrayList<Highlight>();
		this.tokenizedText = tokenizedText;
		this.analyzedWords = analyzedWords;
		this.uniGrams = uniGrams;
		this.biGrams = biGrams;
		this.triGrams = triGrams;
		this.sentiment = sentiment;
	}
	
	//Get the count of a given NGram string
	public int getNGramCount(String str){
		List<NGram> ngrams = null;
		int length = str == null? 0 : str.split("\\s").length;
		if(length == 1)
			ngrams = uniGrams.get(str);
		else if(length == 2)
			ngrams = biGrams.get(str);
		else if(length == 3)
			ngrams = triGrams.get(str);
		return ngrams == null? 0 : ngrams.size(); 
	}
	
	public int getNGramBool(String str){
		List<NGram> ngrams = null;
		int length = str == null? 0 : str.split("\\s").length;
		if(length == 1)
			ngrams = uniGrams.get(str);
		else if(length == 2)
			ngrams = biGrams.get(str);
		else if(length == 3)
			ngrams = triGrams.get(str);
		return ngrams == null? 0 : 1; 
	}
	
	//Get the ML vector
	public Instance getInstanceVector(int ngramSize, int bool){
		ArrayList<Integer> counts = new ArrayList<Integer>();
		int ngramCount = ResourceFactory.getNgramCount(this.domain, ngramSize);
		LinkedHashSet<String> unigrams = ResourceFactory.getNgrams(this.domain, 1);
		LinkedHashSet<String> bigrams = ResourceFactory.getNgrams(this.domain, 2);
		LinkedHashSet<String> trigrams = ResourceFactory.getNgrams(this.domain, 3);
		int attributeCount = ngramCount + 4;
		
		if(ngramSize > 0) {
			for(String s: unigrams) {
				counts.add(bool == 1 ? this.getNGramBool(s) : this.getNGramCount(s));
			}
		}
		
		if(ngramSize > 1) {
			for(String s: bigrams) {
				counts.add(bool == 1 ? this.getNGramBool(s) : this.getNGramCount(s));
			}
		}
		
		if(ngramSize > 2) {
			for(String s: trigrams) {
				counts.add(bool == 1 ? this.getNGramBool(s) : this.getNGramCount(s));
			}
		}

		Instance inst = new Instance(attributeCount);
		for(int i=0; i < attributeCount-4; i++) {
			inst.setValue(i, counts.get(i));
			
		}
		
		inst.setValue(attributeCount-4, unigrams.size());
		inst.setValue(attributeCount-3, sentiment.getPositiveScore());
		inst.setValue(attributeCount-2, sentiment.getNegativeScore());
		
		return inst;
	}
	
	public ArrayList<PriorityQueue<NGram>> getBestNgrams() {
		ArrayList<PriorityQueue<NGram>> bestNgramList = new ArrayList<PriorityQueue<NGram>>();
		for(int i = 1; i < 4; i++) {
			PriorityQueue<NGram> bestNgrams = new PriorityQueue<NGram>(3, new Comparator<NGram>() {
				public int compare(NGram n1, NGram n2) { 
					if(getRating() > 2) {
						return n1.getSentiment().getPositiveScore() > n2.getSentiment().getPositiveScore() ? -1 : 1;
					} else {
						return n1.getSentiment().getNegativeScore() > n2.getSentiment().getNegativeScore() ? -1 : 1;
					}
				}
			});
			Map<String, List<NGram>> m = (i == 1 ? uniGrams : i ==2 ? biGrams : triGrams);
			for(List<NGram> l : m.values()) {
				for(NGram n: l) {
					if(i == 2 && (getRating() > 2 && n.getSentiment().getPositiveScore() > 0.2 || getRating() <= 2 && n.getSentiment().getNegativeScore() > 0.2)) {
						bestNgrams.add(n);
					} else if(i == 1 && (getRating() > 2 && n.getSentiment().getPositiveScore() > 0.3 || getRating() <= 2 && n.getSentiment().getNegativeScore() > 0.3)) {
						bestNgrams.add(n);
					}
					
				}
			}
			bestNgramList.add(bestNgrams);
		}
		
		return bestNgramList;
	}

	
	public String tagTextForRelevantNGrams() { 
		ArrayList<PriorityQueue<NGram>> bestNgramList = getBestNgrams();
		String[] splitText = tokenizedText.split("\\s");
		// ngram size
		for(int l = 1; l<3; l++) {
			// get best 3 ngrams for each size
			for(int i=0; i<4; i++) { 
				NGram n = bestNgramList.get(l-1).poll();
				if(n != null) {
					Highlight cur = new Highlight(n);
					int j = 0;
					while(j < highlights.size()) {
						if(Highlight.mergeable(highlights.get(j), cur)) {
							highlights.set(j, Highlight.merge(highlights.get(j), cur));
							break;
						}
						j++;
					}
					if(j == highlights.size()) {
						highlights.add(cur);
					}
				}
			}
		}
		StringBuilder b = new StringBuilder();
		Collections.sort(highlights);
		int c = 0;
		/*
		for(Highlight h : highlights) {
			System.out.println(h.startIndex);
			System.out.println(h.endIndex);
			System.out.println("--------");
		}
		*/
		int i = 0;
		while(i < splitText.length) {
			b.append(splitText[i]);
			if(c < highlights.size() && highlights.get(c).startIndex == i) {
				System.out.println(splitText[i+1]);
				if(!(splitText[i+1].matches("\\'\\w") || splitText[i+1].matches("n\\'t"))) {
					b.append(" ");
				}
				b.append("[[");
				for(int j = i+1; j < highlights.get(c).endIndex+2; j++) {
					b.append(splitText[j]);
					if(j < highlights.get(c).endIndex+1 && !(splitText[j+1].matches("[\\.,;\\':\\?!]") 
							|| splitText[j+1].matches("\\'\\w")
							|| splitText[j+1].matches("n\\'t"))){
						b.append(" ");
					}
					
				}
				b.append("]]");
				i = highlights.get(c).endIndex +2;
				c++;
			} else {
				i++;
			}
			if(i < splitText.length && !(splitText[i].matches("[\\.,;':\\?!]") 
					|| splitText[i].matches("\\'\\w")
					|| splitText[i].matches("n\\'t"))) {
				b.append(" ");
			}
		}
		String outputText = b.toString().trim();
		return outputText;
	}
	
	//Getters and Setters
	
	public String getTokenizedText() {
		return tokenizedText;
	}
	public void setTokenizedText(String tokenizedText) {
		this.tokenizedText = tokenizedText;
	}
	
	public List<AnalyzedWord> getAnalyzedWords() {
		return analyzedWords;
	}
	public void setAnalyzedWords(List<AnalyzedWord> analyzedWords) {
		this.analyzedWords = analyzedWords;
	}
	
	public Map<String, List<NGram>> getUniGrams() {
		return uniGrams;
	}
	
	public void setUniGrams(Map<String, List<NGram>> uniGrams) {
		this.uniGrams = uniGrams;
	}
	
	public Map<String, List<NGram>> getBiGrams() {
		return biGrams;
	}
	
	public void setBiGrams(Map<String, List<NGram>> biGrams) {
		this.biGrams = biGrams;
	}
	
	public Map<String, List<NGram>> getTriGrams() {
		return triGrams;
	}
	public void setTriGrams(Map<String, List<NGram>> triGrams) {
		this.triGrams = triGrams;
	}
	
	public Sentiment getSentiment() {
		return sentiment;
	}
	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

}
