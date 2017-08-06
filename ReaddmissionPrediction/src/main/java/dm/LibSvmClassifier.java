package dm;

import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;

/**
 * 
 * Use Weka implementation of SVM(LibSVM)to develop a SVM classifier.Used Cross-validation
 * to select the parameters of model(degree) with number of folds set to 3.
 *
 */
public class LibSvmClassifier {
	public static Classifier classify(List<List<Double>> features,List<Double> labels) throws Exception
	   {
		   String SVM_CSV = "res/diabetic_data_svm.csv";
		   String SVM_Arff = "res/svm_output.arff";
		   System.out.println("SVM...");
		   Helper.createCSV(SVM_CSV,features,labels);
		   Instances data_svm = Helper.createARFF(SVM_CSV, SVM_Arff);
		   LibSVM svm = new LibSVM();
		   svm.setCacheSize(512);
		   svm.setNormalize(true);
		   svm.setShrinking(true);
		   svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_POLYNOMIAL, LibSVM.TAGS_KERNELTYPE));		   
		   svm.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));		  
		   svm.setCost(0.1);
		   double accuracy = -1;
		   int degree = 1;
		   int bestDegree = 1;
		   boolean flag = true;
		   while (flag){
			   svm.setDegree(bestDegree);
			   svm.buildClassifier(data_svm);
       
			   Evaluation eval = new Evaluation(data_svm);
			   Random rand = new Random(1);
			   int folds = 3;
			   eval.crossValidateModel(svm, data_svm, folds, rand);
			   System.out.println("Degree value: "+degree);
			   System.out.println(eval.toSummaryString());
			   double n = eval.correct();
			   if(n > accuracy){
				   bestDegree = degree;
				   accuracy = n;
				   degree++;
			   }
			   else{
				   flag = false;
				   svm.setDegree(bestDegree);
				   svm.buildClassifier(data_svm);
			   }
		   }		   			   
		   return (Classifier)svm;		   		   
	   }
}
