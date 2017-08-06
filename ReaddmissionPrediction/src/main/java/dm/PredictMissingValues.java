package dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 * This method imputes a missing value with-
 *    1. Mode of the records  belonging to the same class if the missing value is
 *       discrete
 *    2. Mean of the records belonging to the same class if the missing value is
 *       continuous
 */
public class PredictMissingValues {
	//<30
	static List<Double> Col_MissingValue_1 = new ArrayList<Double>();
	//NO >30
	static List<Double> Col_MissingValue_0 = new ArrayList<Double>();
	public static void predictMissingValues()
	{
		double accum_0=0.0;
		double accum_1=0.0;
		int num_0=0;
		int num_1=0;
		for(int j = 0; j < Transformer.transformedFeatures.get(0).size(); j++)
		{
	
			HashMap<Double, Integer> Counts_label0 = new HashMap<Double, Integer>();
			HashMap<Double, Integer> Counts_label1 = new HashMap<Double, Integer>();
			
			
			for(int i =0;i<Transformer.transformedFeatures.size(); i++){
			
		double value = Transformer.transformedFeatures.get(i).get(j);
		if(value != Double.MAX_VALUE)
		{
			if(Helper.labels.get(i) == 0.0)
			{
				if(j==9)//time in hospital
				{
					accum_0+=value;
					num_0++;
				}
				else if(Counts_label0.containsKey(value)){
					Counts_label0.put(value, Counts_label0.get(value)+1);
				}
				else{
					Counts_label0.put(value, 1);
				}
			}
			else
			{
				if(j==9)//time in hospital
				{
					accum_1+=value;
					num_1++;
				}
				else if(Counts_label1.containsKey(value)){
					Counts_label1.put(value, Counts_label1.get(value)+1);
				}
				else{
					Counts_label1.put(value, 1);
				}				
			}			
		}		
		}
		if(j==9)
		{
			if(num_0!=0)
			 Col_MissingValue_0.add(accum_0/num_0);
			else
				Col_MissingValue_0.add(0.0);
			if(num_1!=0)
				 Col_MissingValue_1.add(accum_1/num_1);
				else
					Col_MissingValue_1.add(0.0);							
		}
		else
		{
			int max = -1;
		double replaceWith = 0.0;
		for(Double d: Counts_label0.keySet()){
			if(Counts_label0.get(d) > max){
				max = Counts_label0.get(d);
				replaceWith = d;
			}
		}
		Col_MissingValue_0.add(replaceWith);
		
		max = -1;
		replaceWith = 0.0;
		for(Double d: Counts_label1.keySet()){
			if(Counts_label1.get(d) > max){
				max = Counts_label1.get(d);
				replaceWith = d;
			}
		}
		Col_MissingValue_1.add(replaceWith);		
		}
		}
		//System.out.println("the missing value 9th col"+Col_MissingValue_0+" "+Col_MissingValue_1);
	}
	public static void replaceMissingValues() {
		for(int i =0;i<Transformer.transformedFeatures.size(); i++){	
		for(int j = 0; j < Transformer.transformedFeatures.get(0).size(); j++){
		  if(Transformer.transformedFeatures.get(i).get(j) == Double.MAX_VALUE){
			  if(Helper.labels.get(i) == 0){
				  Transformer.transformedFeatures.get(i).set(j, Col_MissingValue_0.get(j));
			  }
			  else
				  Transformer.transformedFeatures.get(i).set(j, Col_MissingValue_1.get(j));
		  }
		}
		}				
	}
}

