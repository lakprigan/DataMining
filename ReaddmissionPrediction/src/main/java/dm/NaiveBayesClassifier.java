package dm;

import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

/**
 * Use Weka implementation of Naive Bayes to develop a Naive Bayes model.Used Cross-validation
 * to select the parameters of model, with number of folds set to 10.
 * 
 */
public class NaiveBayesClassifier {
	public static Classifier classify(List<List<Double>> features,List<Double> labels) throws Exception
	   {
		   String SVM_CSV = "res/diabetic_data_nb.csv";
		   String SVM_Arff = "res/nb_output.arff";
		   System.out.println("Naive Baiyes...");
		   Helper.createCSV(SVM_CSV,features,labels);
		   Instances data_dt = Helper.createARFF(SVM_CSV, SVM_Arff);
		   Classifier nb = (Classifier)new NaiveBayes();
		   nb.buildClassifier(data_dt);
		   
		   Evaluation eval = new Evaluation(data_dt);
		    Random rand = new Random(1);  // using seed = 1
		    int folds = 10;
		    eval.crossValidateModel(nb, data_dt, folds, rand);
		    System.out.println(eval.toSummaryString());
		   
		   return nb;
	   }
}
