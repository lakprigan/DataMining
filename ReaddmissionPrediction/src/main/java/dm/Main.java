package dm;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class Main 
{
	public static void main(String[] args) throws Exception {
		Helper.load("res/diabetic_data.csv");

		List<List<Double>> train_data = new ArrayList<List<Double>>();
		List<Double> train_labels = new ArrayList<Double>();

		List<List<Double>> test_data = new ArrayList<List<Double>>();
		List<Double> test_labels = new ArrayList<Double>();
        
		Classifier KNN_classify = null;
		Classifier RandomForest_classify = null;
		Classifier DecisionTree_classify = null;
		Classifier LibSVM_classify = null;
		Classifier NaiveBaiyes_classify = null;

		//This method transforms from nominal/string values to numerical values
		Transformer.transform(Helper.features);
		
		/*
		 * This method imputes a missing value with-
		 *    1. Mode of the records  belonging to the same class if the missing value is
		 *       discrete
		 *    2. Mean of the records belonging to the same class if the missing value is
		 *       continuous
		 */
			
		PredictMissingValues.predictMissingValues();
		PredictMissingValues.replaceMissingValues();

		/*
		 * Feature Selection use Filter technique (Pearson Correlation) to filter the relevant
		 * feature set.
		 */
		FeatureSelection.computeR();
		int count_positive = 0;
		int count_negative = 0;
		String testCSV = "res/diabetic_data_test.csv";
		String testARFFOutput = "res/testOutput.arff";

		/*
		 * Split the input data into Training and Testing data, by randomly allocating
		 * 80% of input data for training and 20% for testing
		 * From the training data ,create five random bags(containing balanced proportion of both majority
		 * class  and minority class) of size 30K
		 */
		
		for (int i = 0; i < Transformer.transformedFeatures.size(); i++) 
		{
			double random = Math.random();
			if (random < 0.8) {
				train_data.add(Transformer.transformedFeatures.get(i));
				train_labels.add(Helper.labels.get(i));
				if (Helper.labels.get(i) == 0.0) {
					count_negative++;
				} else {
					count_positive++;
				}
			} else
			{
				test_data.add(Transformer.transformedFeatures.get(i));
				test_labels.add(Helper.labels.get(i));
			}
		}
		for (int numModels = 0; numModels < 5; numModels++) {
			List<List<Double>> features_mod = new ArrayList<List<Double>>();
			List<Double> labels_mod = new ArrayList<Double>();
			for (int i = 0; i < train_data.size(); i++) {
				double random = Math.random();
				if (train_labels.get(i) == 1.0) {
					features_mod.add(Transformer.transformedFeatures.get(i));
					labels_mod.add(Helper.labels.get(i));
					if (random < (6000.0 / count_positive)) {
						features_mod.add(Transformer.transformedFeatures.get(i));
						labels_mod.add(Helper.labels.get(i));
					}
				} else {
					if (random <= (15000.0 / count_negative)) {
						features_mod.add(Transformer.transformedFeatures.get(i));
						labels_mod.add(Helper.labels.get(i));
					}
				}
			}
 /*
  * Create Classifiers for each each classification algorithm-
  *       1. KNN
  *       2.Random Forest
  *       3.Decision Tree
  *       4.Naive Bayes
  *       5.SVM
  */			
			if (numModels == 0) 
			{
				KNN_classify = KNNClassifier.classify(features_mod, labels_mod);
			} 
			else if (numModels == 1) 
			{
				RandomForest_classify = RandomForestClassifier.classify(features_mod, labels_mod);
			} 
			else if(numModels == 2) 
			{
				DecisionTree_classify = DecisionTreeClassifier.classify(features_mod, labels_mod);
			}
			else if(numModels == 3) 
			{
				NaiveBaiyes_classify = NaiveBayesClassifier.classify(features_mod, labels_mod);
			}
			else if(numModels == 4)
			{
				LibSVM_classify = LibSvmClassifier.classify(features_mod, labels_mod);
			}
		}
       /*
        * Create a test CSV to load the test CSV to Weka library
        * Each test instance is then sent to each classifier to get the label. The final
        * label is computed using a Majority vote model 
        */
		Helper.createCSV(testCSV, test_data, test_labels);
		Instances data_test = Helper.createARFF(testCSV, testARFFOutput);
		int correct_pred = 0;
		int one = 0;
		int zero = 0;
		for (int j = 0; j < data_test.numInstances(); j++) {
			double d_clsLabel = RandomForest_classify.classifyInstance(data_test.instance(j));
     		double d_clsLabel_J48 = DecisionTree_classify.classifyInstance(data_test.instance(j));			
    		double d_clsLabel_KNN = KNN_classify.classifyInstance(data_test.instance(j));
     		double d_clsLabel_SVM = LibSVM_classify.classifyInstance(data_test.instance(j));
    		double d_clsLabel_LibLinear = NaiveBaiyes_classify.classifyInstance(data_test.instance(j));			
			double maj_classLabel = d_clsLabel + d_clsLabel_J48  + d_clsLabel_SVM + d_clsLabel_LibLinear + d_clsLabel_KNN;
			
			if (maj_classLabel >= 3.0) {
				maj_classLabel = 1.0;
			} else {
				maj_classLabel = 0.0;
			}

			if(maj_classLabel == 1.0){
				one++;
			}
			else{
				zero++;
			}
			if (maj_classLabel == data_test.instance(j).value(data_test.classIndex())) {
				correct_pred++;
			}
		}
		System.out.println("Final...");
		System.out.println("the total test instances"+data_test.numInstances());
		System.out.println("Total accuracy is " + (double) correct_pred / data_test.numInstances());		
		System.out.println("ones "+one);
		System.out.println("zeros "+zero);
	}
}
