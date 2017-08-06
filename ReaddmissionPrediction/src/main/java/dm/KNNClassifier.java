package dm;

import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

/**
 * 
 * Builds the KNN for the given Test data
 * Uses Weka's KNN implementation
 * Does 3 fold cross validation to select the optimal K
 *
 */
public class KNNClassifier 
{
   public static Classifier classify(List<List<Double>> features,List<Double> labels) throws Exception
   {
	   String KNN_CSV = "res/diabetic_data_knn.csv";
	   String KNN_Arff = "res/knn_output.arff";
	   System.out.println("KNN...");
	   Helper.createCSV(KNN_CSV,features,labels);
	   Instances data_knn = Helper.createARFF(KNN_CSV, KNN_Arff);
	   IBk ibk = new IBk();	
	   
	   double accuracy = -1;
	   int k = 1;
	   int bestK = 1;
	   boolean flag = true;
	   while (flag){
		   ibk.setKNN(k);
		   ibk.buildClassifier(data_knn);
		   Evaluation eval = new Evaluation(data_knn);
		   Random rand = new Random(1);
		   int folds = 3;
		   eval.crossValidateModel(ibk, data_knn, folds, rand);
		   System.out.println("K value: "+k);
		   System.out.println(eval.toSummaryString());
		   double n = eval.correct();
		   if(n > accuracy){
			   bestK = k;
			   accuracy = n;
			   k++;
		   }
		   else{
			   flag = false;
			   ibk.setKNN(bestK);
			   //ibk.setKNN(5);
			   ibk.buildClassifier(data_knn);
		   }
	   }
	   return (Classifier)ibk;
   }
}
