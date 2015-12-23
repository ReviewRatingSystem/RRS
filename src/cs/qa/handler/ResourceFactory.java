package cs.qa.handler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import cs.qa.enums.Domain;
import cs.qa.enums.PosType;
import cs.qa.model.Sentiment;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@SuppressWarnings("serial")
public class ResourceFactory extends HttpServlet {
	
	public final static int NGRAM_SIZE = 400;
	
	//Stanford NLP pipeline necessary to do Stanford NLP annotations
	private static StanfordCoreNLP stanfordCoreNLP = null;

	//SentiWordNet Map
	private static Map<String, Map<PosType, Sentiment>> sentiWordNet = null;
	
	//Ngrams
	private static Map<Domain, ArrayList<LinkedHashSet<String>>> ngrams;
	
	//Training data and models
	private static Map<Domain, Vector<Object>> modelMap;
	
	private static String RESOURCE_DIR = "/resources";
	
	//Initializing the resources
	//This method must be called before doing any processing
	 public synchronized void init() throws ServletException{
		try{
			
			//Adjust the resources String
			try{
				InputStream is = new ResourceFactory().getClass().getResourceAsStream(RESOURCE_DIR+"/SentiWordNet_3.0.txt");
				if(is == null){
					RESOURCE_DIR = RESOURCE_DIR.substring(1);
				}
			}catch(Exception e){
				RESOURCE_DIR = RESOURCE_DIR.substring(1);
			}
			
			
			//Initialize Stanford NLP pipeline
			if(stanfordCoreNLP == null)
				initializeStanfordCoreNLPPipeline();

			//Initialize SentiWordNet
			if(sentiWordNet == null)
				loadSentiWordNet();
			
			//Load N-Grams
			loadNgrams(NGRAM_SIZE);
			
			//Load Training data and models
			readModels();
			
			int k=0;
			k++;
			
		}catch(Exception e){
			System.out.println("Error Initializing the resources!");
		}
	}
	
	public static void loadNgrams(int size) {
		ngrams = new HashMap<Domain, ArrayList<LinkedHashSet<String>>>();
		for(Domain d : Domain.values()) {
			LinkedHashSet<String> unigrams = readNgrams(d, 1, size);
			LinkedHashSet<String> bigrams = readNgrams(d, 2, size);
			LinkedHashSet<String> trigrams = readNgrams(d, 3, size);
			ArrayList<LinkedHashSet<String>> list = new ArrayList<LinkedHashSet<String>>();
			list.add(unigrams);
			list.add(bigrams);
			list.add(trigrams);
			ngrams.put(d, list);
		}
		
	}
	
	//Initialize Stanford NLP pipeline
	private static void initializeStanfordCoreNLPPipeline(){
		try{
			Properties props = new Properties();
			props.put("annotators", "tokenize, ssplit, pos, lemma");
			props.put("tokenize.options", "untokenizable=noneKeep");
			stanfordCoreNLP = new StanfordCoreNLP(props);
		}catch(Exception e){
			System.out.println("Error initializng Stanford Core NLP!");
		}
	}
	
	//Initialize SentiWordNet
	private static void loadSentiWordNet(){
		try{
			sentiWordNet  = new HashMap<String, Map<PosType, Sentiment>>();
			//Open SentiWordNet for read
			InputStream is = new ResourceFactory().getClass().getResourceAsStream(RESOURCE_DIR+"/SentiWordNet_3.0.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null){
				if(!line.matches("^[anrv]\\t.*$"))
					continue;
				//Parse the lines
				String[] lineParts = line.split("\\t");
				PosType posType = PosType.getByValue(lineParts[0].toUpperCase());
				float positiveScore = Float.parseFloat(lineParts[2]);
				float negativeScore = Float.parseFloat(lineParts[3]);
				String[] synsetTerms = lineParts[4].split("\\s");
				//Store the synsets and their sentiment.
				for(int i=0; i<synsetTerms.length; i++){
					String synsetTerm = synsetTerms[i].toLowerCase();
					synsetTerm = synsetTerm.replace(".", "").replace("'s", "").replace("'", "");
					synsetTerm = synsetTerm.replaceAll("\\#\\d+$", "");
					if(!sentiWordNet.containsKey(synsetTerm))
						sentiWordNet.put(synsetTerm, new HashMap<PosType, Sentiment>());
					//For a synset that appears more than once under the same POS, assign the sentiment scores to be the avaeage ones.
					if(sentiWordNet.get(synsetTerm).containsKey(posType)){
						Sentiment currentSentiment = sentiWordNet.get(synsetTerm).get(posType);
						currentSentiment.setPositiveScore(((currentSentiment.getPositiveScore()*currentSentiment.getCount())+positiveScore)/(currentSentiment.getCount()+1));
						currentSentiment.setNegativeScore(((currentSentiment.getNegativeScore()*currentSentiment.getCount())+negativeScore)/(currentSentiment.getCount()+1));
						currentSentiment.setCount(currentSentiment.getCount()+1);
					}else{
						sentiWordNet.get(synsetTerm).put(posType, new Sentiment(positiveScore, negativeScore));
					}
				}
			}

			//Add backup sentiment (POS=ANY)
			for(Entry<String, Map<PosType, Sentiment>> synsetEntry : sentiWordNet.entrySet()){
				float accPositiveScore = 0;
				float accNegativeScore = 0;
				int count=0;
				for(Entry<PosType, Sentiment> sentimentEntry : synsetEntry.getValue().entrySet()){
					count++;
					accPositiveScore += sentimentEntry.getValue().getPositiveScore();
					accNegativeScore += sentimentEntry.getValue().getNegativeScore();
				}
				synsetEntry.getValue().put(PosType.ANY, new Sentiment((float)accPositiveScore/(float)count, (float)accNegativeScore/(float)count));
			}
			
			br.close();
			is.close();
		}catch(Exception e){
			System.out.println("Error loading SentiWordNet!");
		}
	}
	
	private static LinkedHashSet<String> readNgrams(Domain domain, int n, int size) {
		String degree = n == 1 ? "unigrams" : n == 2 ? "bigrams" : n == 3 ? "trigrams" : null;
		LinkedHashSet<String> s = new LinkedHashSet<String>();
		String ngramFile = ResourceFactory.class.getResource(RESOURCE_DIR+"/data/" + domain.getValue() + "/" + degree + ".txt").getPath();
		BufferedReader ngramReader;
		String line;
		try {
			int index=0;
			ngramReader = new BufferedReader(new FileReader(ngramFile));
			while((line = ngramReader.readLine()) != null && index++<size) {
				if(!line.equals("")) {
					s.add(line.trim().split("\\s+")[1]);
				}
			}
			ngramReader.close();
		} catch (Exception e){
			System.out.println("Error reading the Ngrams!");
		}
		return s;
	}
	
	@SuppressWarnings("unchecked")
	public static void readModels() throws Exception {
		try{
			modelMap = new HashMap<Domain, Vector<Object>>();
			for(Domain domain : Domain.values()){
				InputStream is = new ResourceFactory().getClass().getResourceAsStream(RESOURCE_DIR+"/data/"+domain.getValue().toLowerCase()+"/model.txt");
				String model = convertStreamToString(is);
				modelMap.put(domain, (Vector<Object>)Serializer.fromString(model));
			}
		} catch (Exception e){
			System.out.println("Error loading the models!");
		}
	}
	
	public static LinkedHashSet<String> getNgrams(Domain domain, int n) {
		return ngrams.get(domain).get(n-1);
	}
	
	public static int getNgramCount(Domain domain, int ngramSize) {
		int i = 1;
		int count = 0;
		for(LinkedHashSet<String> l : ngrams.get(domain)) {
			if(i <= ngramSize) {
				count += l.size();
			}
			i++;
		}
		return count;
	}

	//Get Stanford NLP pipeline
	public static StanfordCoreNLP getStanfordCoreNLP(){
		return stanfordCoreNLP;
	}
	
	//Get SentiWordNet
	public static Map<String, Map<PosType, Sentiment>> getSentiWordNet(){
		return sentiWordNet;
	}

	public static Map<Domain, Vector<Object>> getModelMap() {
		return modelMap;
	}
	
	static String convertStreamToString(InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

}
