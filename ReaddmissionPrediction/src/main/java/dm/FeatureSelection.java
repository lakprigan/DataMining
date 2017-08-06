package dm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * @author PriyaArun
 *
 */
public class FeatureSelection 
{
	/* 
	 * For each attribute compute Pearson correlation of the attribute with the class label. 
	 * Using a threshold of .001, all features with correlation  less than this threshold are
	 * filtered, retaining only the features with threshold greater than this value
	 */
	public static HashMap<Integer,Double>individualFeatures = new HashMap<Integer,Double>();
	public static ArrayList<Integer> featureSubset = new ArrayList<Integer>();
	
	public static void computeR() 
	{
		int numberOfFeatures = Transformer.transformedFeatures.get(0).size();
		for(int i = 0; i< numberOfFeatures; i++){
			List<Double> features = new ArrayList<Double>();
			for(int l =0; l<Transformer.transformedFeatures.size(); l++)
			{				
					features.add(Transformer.transformedFeatures.get(l).get(i));			
			}
			individualFeatures.put(i,Math.abs(Pearson.pearson(features,Helper.labels)));					
		}
		LinkedHashMap<Integer,Double> sortedFeaures = sortHashMap(individualFeatures);
		
		for(Entry<Integer, Double> entry : sortedFeaures.entrySet())
		{
			if(Helper.round(entry.getValue(),3)>0.0)
			{
				featureSubset.add(entry.getKey());
				//System.out.println("the feature is"+entry.getKey());
			}
		}
	}
		//Sort the hashMap in descending order of values
		public static LinkedHashMap<Integer,Double> sortHashMap(HashMap<Integer,Double> individualFeatures)
		{
			List<Double> vals = new ArrayList<Double>(individualFeatures.values());
			List<Integer> keys = new ArrayList<Integer>(individualFeatures.keySet());
			Collections.sort(vals,Collections.reverseOrder());
			Collections.sort(keys,Collections.reverseOrder());
			LinkedHashMap<Integer,Double> sortedHash = new LinkedHashMap<Integer,Double>();
			Iterator<Double> iterator_val = vals.iterator();
			int count=0;
			while(iterator_val.hasNext()&&(count<100))
			{
				Double val = iterator_val.next();
				Iterator<Integer> iterator_key = keys.iterator();
				while(iterator_key.hasNext())
				{
					Integer k_val = iterator_key.next();
					Double getVal = individualFeatures.get(k_val);
					if(val.equals(getVal))
					{
						iterator_key.remove();
						sortedHash.put(k_val, getVal);
						break;
					}
				}
				count++;
			}
			return sortedHash;			 
		}		
}
	

