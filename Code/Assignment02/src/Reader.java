import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {	
	static ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	static boolean canReadData = false;
	
	public static boolean checkArffExtension(String string) {
		return string.contains(".arff");
	}

	public static String readPathOfFile () {
		System.out.println("Write path to ARFF format file: \n");
		Scanner sc = new Scanner(System.in);
		String path = sc.next();
		sc.close();
		return path;
	}

	public static void readFile(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				testLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testLine(String line){
		if(!line.startsWith("%") && !line.startsWith("@RELATION") && !line.isEmpty()){
			line = line.replaceAll("\\s+", ""); //we remove spaces and \t
			String name = null;
			String values = null;
			if(line.startsWith("@ATTRIBUTE")){	
				line = line.replaceAll("@ATTRIBUTE", "");
				name = (String) line.subSequence(0, line.indexOf("{"));
				values = (String) line.subSequence(line.indexOf("{")+1, line.indexOf("}"));
				attributes.add(new Attribute(name,values));
			}			
			if(canReadData){	
				int i = 0;
				for(String subString:line.split(",")){
					if(attributes.get(i).getPossibleValues().contains(subString)){ //if the value exist
						attributes.get(i).getValues().add(subString);
					} else {
						attributes.get(i).getValues().add("Unknown");
					}
					i++;
				}
			}
			if(line.startsWith("@DATA")){
				canReadData = true;
			}
		} 
	}
	
	public static void calculateEntropyForRoot() {
		for(int currentAttribute = 0; currentAttribute < attributes.size()-1; currentAttribute++) {			
			//we calculate for the currentAttribute the number of each class value
			double[]classCounter = new double[attributes.get(attributes.size()-1).getPossibleValues().size()];	
			//size = number of different value of class
			
			for(int i = 0; i <  attributes.get(attributes.size()-1).getPossibleValues().size(); i++){
				classCounter[i] = 0; //first we initialize
				for(int l = 0; l < attributes.get(attributes.size()-1).getValues().size(); l++) { //we go through each line of data
					if (attributes.get(attributes.size()-1).getValues().get(l).equals(attributes.get(attributes.size()-1).getPossibleValues().get(i))){
						classCounter[i]++;
						//we check all the data line on each class data 
					}
				}
			}
			
			double total = 0;
			for(int i = 0; i < attributes.get(attributes.size()-1).getPossibleValues().size(); i++){
				total += classCounter[i];
			}
			//we calculate the entropy and the gain			
			attributes.get(currentAttribute).setEntropy(mathEntropy(classCounter, total));
			attributes.get(currentAttribute).calculateGain(attributes, currentAttribute, total);			
		}
		
		double gainMax = -1;
		int indexMax = -1;
		for(int i= 0; i < attributes.size()-1; i++){ //looking for the max entropy
			if(attributes.get(i).getGain() > gainMax){
				indexMax = i;
				gainMax = attributes.get(i).getGain();
			}
		}
		
		if(indexMax != -1) {
			//for each value of the attribute with the max Gain
			for(int attribute = 0; attribute < attributes.get(indexMax).getPossibleValues().size(); attribute++){
				String string = new String();
				string += attributes.get(indexMax).getName() + " = ";
				string += attributes.get(indexMax).getPossibleValues().get(attribute).toString() + " : ";

				double[]maxAttributeClassCounter = new double[attributes.get(attributes.size()-1).getPossibleValues().size()];	
				for(int classValue = 0; classValue < attributes.get(attributes.size()-1).getPossibleValues().size(); classValue++){
					maxAttributeClassCounter[classValue] = 0;
				}
				

				for(int attributeMaxValue = 0; attributeMaxValue < attributes.get(indexMax).getValues().size(); attributeMaxValue++) { //we go through each line of data						
					if(attributes.get(indexMax).getValues().get(attributeMaxValue).equals(attributes.get(indexMax).getPossibleValues().get(attribute))){//if we get a positive
						for(int classValue = 0; classValue < attributes.get(attributes.size()-1).getPossibleValues().size(); classValue++){						
							if(attributes.get(attributes.size()-1).getValues().get(attributeMaxValue).equals(attributes.get(attributes.size()-1).getPossibleValues().get(classValue))){//if we get a positive
								maxAttributeClassCounter[classValue]++;
							}
						}
					}
				}
				
				boolean atLeastOneResult = false;
				if(Attribute.noNeedToGoDeeper(maxAttributeClassCounter)){ 
					for(int k = 0; k < attributes.get(attributes.size()-1).getPossibleValues().size(); k++){
						string += attributes.get(attributes.size()-1).getPossibleValues().get(k).toString() + " = " + maxAttributeClassCounter[k] + " | ";
						atLeastOneResult = true;
					}
					if(atLeastOneResult){
						string = string.substring(0, string.length()-2);
						System.out.println(string); //we print the current attribute	
					}
				} else { //if we have to go deeper in the tree
					System.out.println(string); //we print the current attribute	
					attributes.get(indexMax).createNextAttributes(attributes, 1,attribute);	//we created the next node					
				}
			}
		}
		
	}
	
	public static double mathEntropy(double[] c, double total) { //calculate the entropy from the class
		double result = 0;
		for(int i = 0; i < c.length; i++){
			if(c[i] != 0){
				result -= (c[i]/total)*(Math.log(c[i]/total)/Math.log(2));
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		String path = "C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/soybean.arff";		
		if (checkArffExtension(path)){
			readFile(path); //"C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/soybean.arff"
			calculateEntropyForRoot();		
		} else System.out.println("File doesn't have a .arff extension");
		}
}
