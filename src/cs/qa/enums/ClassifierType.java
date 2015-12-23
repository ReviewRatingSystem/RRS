package cs.qa.enums;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

public enum ClassifierType {
	
	//Classifiers
	NAIVE_BAYES("NAIVE_BAYES"),
	LIB_SVM("LIB_SVM"),
	J48("J48"),
	RANDOM_FOREST("RANDOM_FOREST"),
	IBK("IBK");

	private String value;
	
	ClassifierType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static ClassifierType getByValue(String value){
	    for(ClassifierType type : values()){
	        if(type.getValue().equals(value)){
	            return type;
	        }
	    }
	    return null;
	}
	
	public static Classifier getClassifier(ClassifierType classifierType){
		Classifier classifier = null;
		switch(classifierType){
		case NAIVE_BAYES:
			classifier = new NaiveBayes();
			break;
		case LIB_SVM:
			classifier = new LibSVM();
			break;
		case J48:
			classifier = new J48();
			break;
		case RANDOM_FOREST:
			classifier = new RandomForest();
			break;
		case IBK:
			classifier = new IBk();
			break;
		default:
			classifier = new NaiveBayes();
		}
		return classifier;
	}

}
