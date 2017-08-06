package dm;

import java.util.List;

public class Pearson {
	/*
	 * Computes the Pearson Correlation for each attribute with the class label
	 */
	public static double pearson(List<Double> feature,List<Double> label){
	    double sum_sq_x = 0.0;
	    double sum_sq_y = 0.0;
	    double sum_coproduct = 0;
	    double mean_x = 0.0;
	    double mean_y = 0.0;
	    int N = label.size();
	    for (int i = 0; i<N; i++) {
	        sum_sq_x += feature.get(i) * feature.get(i);
	        sum_sq_y += label.get(i) * label.get(i);
	        sum_coproduct += feature.get(i) * label.get(i);
	        mean_x += feature.get(i);
	        mean_y += label.get(i);
	    }
	    mean_x = mean_x / N;
	    mean_y = mean_y / N;
	    double pop_sd_x = Math.sqrt((sum_sq_x/N) - (mean_x * mean_x));
	    double pop_sd_y = Math.sqrt((sum_sq_y / N) - (mean_y * mean_y));
	    double cov_x_y = (sum_coproduct / N) - (mean_x * mean_y);
	    double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
	    return correlation;
	}


}
