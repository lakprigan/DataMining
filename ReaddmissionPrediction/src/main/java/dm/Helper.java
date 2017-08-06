package dm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * 
 * Initial Loading of features,labels and attribute headers. It also contains
 * generic methods like - Create a CSV,prepare data in the format required by Weka 
 * library
 *
 */
public class Helper {
	static List<List<String>> features = new ArrayList<List<String>>();
	static List<Double> labels = new ArrayList<Double>();
	static ArrayList<String> attrbIndices = new ArrayList<String>();
    static List<Integer> removeCols = Arrays.asList(0,1,5);
	public static void load(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			boolean labelFlag = false;
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0)
					continue;
				if (labelFlag) {
					String values[] = line.split(",");
					List<String> currentFeature = new ArrayList<String>();
					for (int i = 0; i < values.length - 1; i++) {
						if(!removeCols.contains(i))
						   currentFeature.add(values[i]);
					}
					if (values[values.length - 1].equals("NO") || (values[values.length - 1].equals(">30"))) {
						labels.add(0.0);
					} else {
						labels.add(1.0);
					}
					features.add(currentFeature);
				}
				if (!labelFlag) {
					String values[] = line.split(",");
					for (int i = 0; i < values.length; i++) {
						if(!removeCols.contains(i))
						   attrbIndices.add(values[i]);
					}
				}
				labelFlag = true;
			}
			//System.out.println(features.get(0).size()+" "+attrbIndices.size());
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (reader != null) {

				try {

					reader.close();

				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		}

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
/*
 * Create a CSV file for the input features and label data provided
 */
	public static void createCSV(String filename, List<List<Double>> features, List<Double> labels) throws IOException {
		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		String attrb = "";
		boolean flag = true;
		for (int i = 0; i < Helper.attrbIndices.size(); i++) {
			if (flag) {
				attrb += Helper.attrbIndices.get(i);
				flag = false;
			} else {
				attrb += "," + Helper.attrbIndices.get(i);

			}
		}
		bw.write(attrb);
		for (int i = 0; i < features.size(); i++) {
			bw.newLine();
			boolean first = true;
			String attrb_val = "";
			for (int j = 0; j < features.get(0).size(); j++) {
				if (first) {
					attrb_val += features.get(i).get(j);
					first = false;
				} else {
					attrb_val += "," + features.get(i).get(j);
				}
			}

			if (filename.contains("_test"))
				attrb_val += "," + labels.get(i);
			else
				attrb_val += "," + labels.get(i);

			bw.write(attrb_val);
		}
		bw.close();
	}
	/*
	 * Loads the CSV file, and set parameters-provide the class index. Also create a filter
	 * that converts the class label from numeric to nominal as required by Weka.
	 */
	public static Instances createARFF(String CSVFile, String ARFFoutput) throws Exception {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(CSVFile));
		Instances data = loader.getDataSet();
		NumericToNominal convert = new NumericToNominal();
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "last";
		convert.setOptions(options);
		convert.setInputFormat(data);
		data = Filter.useFilter(data, convert);
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(data);
//		saver.setFile(new File(ARFFoutput));
//		saver.writeBatch();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		return data;
	}
}
