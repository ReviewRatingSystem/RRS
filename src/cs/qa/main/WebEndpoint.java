package cs.qa.main;

import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.google.gson.Gson;

import cs.qa.enums.Domain;
import cs.qa.handler.ResourceFactory;
import cs.qa.handler.ReviewAnalyzer;
import cs.qa.model.AnalyzedReview;
import cs.qa.model.Review;

@Path("/")
public class WebEndpoint {
	
	@GET
	@Path("/{analyze}")
	@QueryParam("/{analyze}/{review}/{domain}")
	@Produces("application/json")
	public String getMsg(	@PathParam("analyze") String analyzeStr,		
							@QueryParam("review") String reviewStr, 
							@QueryParam("domain") String domainStr){
		try{

			//Empty Request
			if(analyzeStr == null || !analyzeStr.equals("analyze") || reviewStr == null || reviewStr.equals("") || domainStr == null || domainStr.equals("")){
				System.out.println("Invalid Request!");
				return new Gson().toJson("Invalid Request");
			}
			
			//Read the domain
			Domain domain = Domain.getByValue(domainStr.toLowerCase());
			
			//Analyze the review
			Review review = new Review(reviewStr.replace("_", " ").replace("QUEST_MARK", "?"), 0);
			AnalyzedReview analyzedReview = ReviewAnalyzer.analyze(review, domain);
			
			int rating = 5;
			if(analyzedReview.getSentiment().getPositiveScore() > 0.1){
				rating = 5;
			}if(analyzedReview.getSentiment().getNegativeScore() > 0.1){
				rating = 1;
			}else{
				//Built the evaluation set
				Instances querySet = new Instances("ReviewRating", getVector(domain, 2), 1);
				int attributeCount = ResourceFactory.getNgramCount(domain, 2) + 4;
				querySet.setClassIndex(attributeCount-1);
				Instance inst = (analyzedReview).getInstanceVector(2, 1);
				inst.setDataset(querySet);
				inst.setValue(attributeCount-1, "5");
				querySet.add(inst);
				
				//Run the evaluation
				Vector<Object> models = ResourceFactory.getModelMap().get(domain);
				Instances trainingSet = (Instances)models.get(0);
				Classifier classifier = (Classifier)models.get(1);
				Evaluation eDev = new Evaluation(trainingSet);
				StringBuffer forPredictionsPrinting = new StringBuffer();
				eDev.evaluateModel(classifier, querySet, forPredictionsPrinting, null, true);
				String predictions = forPredictionsPrinting.toString();
				String prediction = predictions.split("\\n")[0].replaceAll("^.*\\:(\\S+)\\s.*\\:(\\S+)\\s.*$", "$2");
				rating = Integer.parseInt(prediction);
			}

			review.setText(analyzedReview.tagTextForRelevantNGrams());
			review.setRating(rating);
			return new Gson().toJson(review);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			return new Gson().toJson("Unexpected Error!");
		}
	}
	
	private static FastVector getVector(Domain domain, int ngramSize) {
		int attributeCount = ResourceFactory.getNgramCount(domain, ngramSize) + 4;
		FastVector vector = new FastVector(attributeCount);
		for(int i=0; i < attributeCount-4; i++) {
			vector.addElement(new Attribute("RR" + i));
		}
		
		vector.addElement(new Attribute("wordCount"));
		Attribute posScore = new Attribute("positiveScore");
		Attribute negScore = new Attribute("negativeScore");
		vector.addElement(posScore);
		vector.addElement(negScore);

		FastVector fvClassVal = new FastVector(5);
	   	for(int i=1; i<=5; i++) {
	   		fvClassVal.addElement(""+ i);
	   	}
	   	Attribute ClassAttribute = new Attribute("Rating", fvClassVal);
	   	vector.addElement(ClassAttribute);
	   	return vector;
	}
	
}