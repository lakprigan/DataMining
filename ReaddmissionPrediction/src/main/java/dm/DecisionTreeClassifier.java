package dm;

import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * 
 * Builds the Decision Tree Model for the given Test data
 * Uses Weka's J48 implementation
 * No parameter tuning, and hence no cross validation
 *
 */
public class DecisionTreeClassifier {
	public static Classifier classify(List<List<Double>> features,List<Double> labels) throws Exception
	   {
		   String DT_CSV = "res/diabetic_data_dt.csv";
		   String DT_Arff = "res/dt_output.arff";
		   System.out.println("Decision Tree...");
		   Helper.createCSV(DT_CSV,features,labels);
		   Instances data_dt = Helper.createARFF(DT_CSV, DT_Arff);
		   Classifier modelJ48 = (Classifier)new J48();
		    modelJ48.buildClassifier(data_dt);
		    
		    Evaluation eval = new Evaluation(data_dt);
		    Random rand = new Random(1);  // using seed = 1
		    int folds = 10;
		    eval.crossValidateModel(modelJ48, data_dt, folds, rand);
		    System.out.println(eval.toSummaryString());
		 
		    return modelJ48;
	   }

}
