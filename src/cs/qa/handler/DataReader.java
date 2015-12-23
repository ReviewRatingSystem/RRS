package cs.qa.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs.qa.enums.DataType;
import cs.qa.enums.Domain;
import cs.qa.model.AnalyzedReview;
import cs.qa.model.IReview;
import cs.qa.model.Review;

public class DataReader {
	
	/* 
	 * This class reads a data file (train, dev or test of any domain) and outputs a list of review. 
	 * The inputs are: 	1) the domain (health, restaurant or shopping)
	 * 					2) the data type (train, dev or test) 
	 * 					3) a boolean of whether to analyze the reviews or not
	 */
	
	public static List<IReview> read(Domain domain, DataType dataType, boolean analyze){
		List<IReview> reviews = new ArrayList<IReview>();
		try{
			//Open the data file for read
			InputStream is = new DataReader().getClass().getResourceAsStream("/resources/data/"+domain.getValue()+"/"+dataType.getValue()+".json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line;
	        //Loop over the lines and read the reviews
	        while ((line = br.readLine()) != null){
	        	if(line.equals("")) //empty line
	        		continue;
	        	HashMap<String,Object> map = new Gson().fromJson(line, new TypeToken<HashMap<String, Object>>(){}.getType());
	        	Review review = new Review(map.get("text").toString(), (int)Float.parseFloat(map.get("stars").toString()));
	        	//Analyze the review if the "analyze" parameter is set to "true".
	        	if(analyze){
	        		AnalyzedReview analyzedReview = ReviewAnalyzer.analyze(review, domain);
	        		reviews.add(analyzedReview);
	        	}else{
	        		reviews.add(review);
	        	}
	        }
            is.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error reading the data!");
		}
		return reviews;
	}

}
