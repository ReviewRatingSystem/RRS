package cs.qa.main;

import cs.qa.enums.DataType;
import cs.qa.enums.Domain;
import cs.qa.handler.DataReader;
import cs.qa.handler.ResourceFactory;
import cs.qa.handler.ReviewAnalyzer;
import cs.qa.model.AnalyzedReview;
import cs.qa.model.Review;

public class Main {
	
	public static void main(String[] args){
		try{
			new ResourceFactory().init();
			AnalyzedReview r = ReviewAnalyzer.analyze(new Review("hello Michael, hello everybody. This is a review about a shop that I do not like.", 3), Domain.HEALTH);
			//r.getInstanceVector();
			//System.out.println(new Gson().toJson(ReviewAnalyzer.analyze(new Review("hello Michael, hello everybody. This is a review about a shop that I do not like.", 3))));
			for (Domain domain : Domain.values()) {
				DataReader.read(domain, DataType.TRAIN, true);
			}
		}catch(Exception e){}
	}

}
