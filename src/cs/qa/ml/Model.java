package cs.qa.ml;

import java.util.List;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import cs.qa.enums.ClassifierType;
import cs.qa.enums.DataType;
import cs.qa.enums.Domain;
import cs.qa.handler.DataReader;
import cs.qa.handler.ResourceFactory;
import cs.qa.model.AnalyzedReview;
import cs.qa.model.IReview;

public class Model {
	
	public static void main(String[] args) throws Exception {
		for(int s=400; s<=400; s+=100) {
			new ResourceFactory().init();
			for(Domain d : Domain.values()) {
				for(int ngc = 2; ngc <= 2; ngc++) {
					for(int b = 1; b <=1; b++) {
						Vector<Object> objects = buildData(d, DataType.TRAIN, 10000, ngc, b);
						for (ClassifierType c : ClassifierType.values()){
							if(c != ClassifierType.LIB_SVM)
								continue;
	//						System.out.println();
	//						System.out.println("===========================");
	//						System.out.println("Domain: " + d.toString());
	//						System.out.println("Classifier: " + c.toString());
	//						System.out.println("Ngram size: " + ngc);
	//						System.out.println("Bool/Count?: " + (b == 1 ? "Bool" : "Count"));
	//						System.out.println("===========================");
	//						System.out.println();
							classifyAndEvaluate((Instances)objects.get(0), (Instances)objects.get(1), c, d, ngc, b, s);
						}
						
					}
					
				}
			}
		}
		
//		ResourceFactory.initialize(400);
//		
//		Vector<Object> objects1 = buildData(Domain.HEALTH, DataType.TRAIN, 10000, 2, 1);
//		classifyAndEvaluate((Instances)objects1.get(0), (Instances)objects1.get(1), ClassifierType.LIB_SVM, Domain.HEALTH, 2, 0, 400);
//		PrintWriter p1 = new PrintWriter("/Users/ramivina/hhh.txt"); p1.write(Serializer.toString(objects1)); p1.close();
//		
//		Vector<Object> objects2 = buildData(Domain.RESTAURANT, DataType.TRAIN, 10000, 2, 1);
//		classifyAndEvaluate((Instances)objects2.get(0), (Instances)objects2.get(1), ClassifierType.LIB_SVM, Domain.RESTAURANT, 2, 0, 200);
//		PrintWriter p2 = new PrintWriter("/Users/ramivina/rrr.txt"); p2.write(Serializer.toString(objects2)); p2.close();
//		
//		Vector<Object> objects3 = buildData(Domain.SHOPPING, DataType.TRAIN, 10000, 2, 1);
//		classifyAndEvaluate((Instances)objects3.get(0), (Instances)objects3.get(1), ClassifierType.LIB_SVM, Domain.SHOPPING, 2, 1, 400);
//		PrintWriter p3 = new PrintWriter("/Users/ramivina/sss.txt"); p3.write(Serializer.toString(objects3)); p3.close();
		
		System.out.println("Done");
	}
	
	private static FastVector getVector(Domain domain, int ngramSize) {
		int extraAtts = 4;
		int attributeCount = ResourceFactory.getNgramCount(domain, ngramSize) + extraAtts;
		FastVector vector = new FastVector(attributeCount);
		for(int i=0; i < attributeCount-extraAtts; i++) {
			vector.addElement(new Attribute("RR" + i));
		}
		
		if(extraAtts == 4){
			vector.addElement(new Attribute("wordCount"));
			Attribute posScore = new Attribute("positiveScore");
			Attribute negScore = new Attribute("negativeScore");
			vector.addElement(posScore);
			vector.addElement(negScore);
		}
		
		FastVector fvClassVal = new FastVector(5);
	   	for(int i=1; i<=5; i++) {
	   		fvClassVal.addElement(""+ i);
	   	}
	   	Attribute ClassAttribute = new Attribute("Rating", fvClassVal);
	   	vector.addElement(ClassAttribute);
	   	return vector;
	}
	
	public static Vector<Object> buildData(Domain domain, DataType type, int setSize, int ngramSize, int bool) throws Exception {
		int extraAtts = 4;
		FastVector vector = getVector(domain, ngramSize);
		int attributeCount = ResourceFactory.getNgramCount(domain, ngramSize) + extraAtts;
		
	   	List<IReview> testReviews = DataReader.read(domain, type, true);
		Instances trainSet = new Instances("ReviewRating", vector, setSize);
		trainSet.setClassIndex(attributeCount-1);
		for(IReview r : testReviews) {
			Instance inst = ((AnalyzedReview) r).getInstanceVector(ngramSize, bool);
			inst.setDataset(trainSet);
			inst.setValue(attributeCount-1, ""+ r.getRating());
			trainSet.add(inst);
		}
		
		FastVector vector2 = getVector(domain, ngramSize);
	   	List<IReview> devReviews = DataReader.read(domain, DataType.TEST, true);
		Instances devSet = new Instances("ReviewRating", vector2, 1000);
		devSet.setClassIndex(attributeCount-1);
		for(IReview r : devReviews) {
			Instance inst = ((AnalyzedReview) r).getInstanceVector(ngramSize, bool);
			inst.setDataset(devSet);
			inst.setValue(attributeCount-1, ""+ r.getRating());
			devSet.add(inst);
		}
		
		Vector<Object> objects = new Vector<Object>();
		objects.add(trainSet);
		objects.add(devSet);
		return objects;
	}
	
	
	public static void classifyAndEvaluate(Instances trainSet, Instances devSet, ClassifierType c, Domain domain, int ngramSize, int bool, int s) throws Exception {
		Classifier classifier =ClassifierType.getClassifier(c);
		classifier.buildClassifier(trainSet);
		Evaluation eDev = new Evaluation(trainSet);
		StringBuffer forPredictionsPrinting = new StringBuffer();
		eDev.evaluateModel(classifier, devSet);//, forPredictionsPrinting, null, true);
//		String predictions = forPredictionsPrinting.toString();
//		String[] predictionSplit = predictions.split("\n");
//		for(int i=0; i<predictionSplit.length; i++){
//			if(predictionSplit.length>0){
//				String prediction = predictionSplit[i].replaceAll("^.*\\:(\\S+)\\s.*\\:(\\S+)\\s.*$", "$1\t$2");
//				System.out.println(prediction);
//			}
//		}
		double accuracy = calculateAccuracy(eDev.predictions());	
		System.out.println(domain+"\t"+c+"\t"+ngramSize+"\t"+(bool==0?"Count":"Boolean")+"\t"+s+"\t"+String.format("%.2f%%", accuracy));	
		
	}
	
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}
	
}
