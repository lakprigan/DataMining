package dm;

import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class RandomForestClassifier {
	/*
	 * Use Weka implementation of Random Forest to create a model. 
	 * Parameter tuning (setMaxDepth,setNumTrees) is done using cross validation with the 
	 * number of folds set to 4.
	 */
	public static Classifier classify(List<List<Double>> features,List<Double> labels) throws Exception
	   {
		
		   String RF_CSV = "res/diabetic_data_rf.csv";
		   String RF_Arff = "res/rf_output.arff";
		   Helper.createCSV(RF_CSV,features,labels);
		   Instances data_rf = Helper.createARFF(RF_CSV, RF_Arff);
		   RandomForest rf = new RandomForest();
		   System.out.println("Random Forest...");
		   rf.setNumTrees(100);
		   rf.setMaxDepth(8);
		   rf.buildClassifier(data_rf);
		   
		   Evaluation eval = new Evaluation(data_rf);
		   Random rand = new Random(1);  // using seed = 1
		   int folds = 4;
		   eval.crossValidateModel(rf, data_rf, folds, rand);
		   System.out.println(eval.toSummaryString());
		    
		   return rf;
	   }
}
