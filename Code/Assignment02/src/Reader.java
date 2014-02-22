import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {	
	static ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	static boolean canReadData = false;
	static int dataSize;
	
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
		dataSize = 0;
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
			String name = null;
			String values = null;
			if(line.startsWith("@ATTRIBUTE")){						
				name = (String) line.subSequence(line.indexOf(" ") + 1, line.indexOf(" ", line.indexOf(" ") + 1));
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
				dataSize++;
			}
			if(line.startsWith("@DATA")){
				canReadData = true;
			}
		} 
	}
	
	public static void calculateEntropyForRoot() {
		for(int currentAttribute = 0; currentAttribute < attributes.size()-1; currentAttribute++) { //we exclude the last attribute (the class)
			double[]classeCounter = new double[attributes.get(attributes.size()-1).getPossibleValues().size()];	
			for(int i = 0; i < attributes.get(attributes.size()-1).getPossibleValues().size(); i++){
				classeCounter[i] = 0;
			}
			for(int i = 0; i <  attributes.get(attributes.size()-1).getPossibleValues().size(); i++){
				for(int l = 0; l < attributes.get(attributes.size()-1).getValues().size(); l++) { //we go through each line of data
					if (attributes.get(attributes.size()-1).getValues().get(l).equals(attributes.get(attributes.size()-1).getPossibleValues().get(i))){
						classeCounter[i]++; //if positive
					}
				}
			}
			double total = 0;
			for(int i = 0; i < attributes.get(attributes.size()-1).getPossibleValues().size(); i++){
				total += classeCounter[i];
			}
			//we set the entropy of each attributes			
			attributes.get(currentAttribute).setEntropy(mathEntropy(classeCounter, total));
			attributes.get(currentAttribute).calculateGain(attributes, total);			
		}
		
		double gainMax = -1;
		int indexMax = -1;
		for(int i= 0; i < attributes.size()-1; i++){ //looking for the max entropy
			if(attributes.get(i).getGain() > gainMax){
				indexMax = i;
				gainMax = attributes.get(i).getGain();
			}
		}
		//j= number of possible value for this attribute
		for (int j = 0; j < attributes.get(indexMax).getPossibleValues().size(); j++){
			String string = new String();
			string += attributes.get(indexMax).getName() + " = ";
			string += attributes.get(indexMax).getPossibleValues().get(j).toString() + " : ";

			int positive = 0;
			int negative = 0;
			//we check the number of positive and negative classes
			for(int k = 0; k < attributes.get(attributes.size()-1).getValues().size(); k++){
				if(attributes.get(indexMax).getValues().get(k).equals(attributes.get(indexMax).getPossibleValues().get(j))){
					if(attributes.get(attributes.size()-1).getValues().get(k).equals("P")){//if we get a positive
						positive++;
					} else if(attributes.get(attributes.size()-1).getValues().get(k).equals("N")){//if we get a negative
						negative++;
					}
				}
			}
			if(positive == 0 || negative == 0){
				string += "P = " + positive + " |  N = " + negative; //we print the tree
				System.out.println(string); //we print the tree							
			} else {
				string += "P = " + positive + " |  N = " + negative; //we print the tree
				System.out.println(string); //we print the tree
				attributes.get(indexMax).createNextAttributes(attributes,1,j);						
			}	
		}
		
	}
	
	public static double mathEntropy(double[] classe, double total) {
		double result = 0;
		for(int i = 0; i < classe.length; i++){
			if(classe[i] != 0){
				result -= (classe[i]/total)*(Math.log(classe[i]/total)/Math.log(2));
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		String path = "C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/Quilan.arff";//readPathOfFile();		
		if (checkArffExtension(path)){
			readFile(path); //C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/Quilan.arff
			calculateEntropyForRoot();		
		} else System.out.println("File doesn't have a .arff extension");
		}
}
