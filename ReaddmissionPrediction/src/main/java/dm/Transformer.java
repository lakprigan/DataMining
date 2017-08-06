package dm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * Transforms discrete non-numeric attributes to numeric
 * replaces the missing attributes with Double.Max Value
 *
 */
public class Transformer {
	
	public static final String MISSING = "?";
	public static final double MISSING_REPLACED = Double.MAX_VALUE;
	
	public static List<List<Double>> transformedFeatures = new ArrayList<List<Double>>();
		
	public static void transform(List<List<String>> features) throws FileNotFoundException{
		double hit = 1.0;
		HashMap<String, Double> lookup = new HashMap<String, Double>();
		for(int i = 0; i<features.size();i++){
			List<String> instance = features.get(i);
			List<Double> transformedInstance = new ArrayList<Double>();
			for(int j =0; j <instance.size(); j++){
				String current = instance.get(j);
				if(current.equalsIgnoreCase(MISSING)){
					transformedInstance.add(MISSING_REPLACED);
				}
				else{
					try{
						transformedInstance.add(Double.parseDouble(current));
					}
					catch(Exception e){
						if(lookup.containsKey(j+current)){
							transformedInstance.add(lookup.get(j+current));
						}
						else{
							transformedInstance.add(hit);
							lookup.put(j+current, hit++);
						}
					}
				}
			}
			transformedFeatures.add(transformedInstance);
		}
	}
}
