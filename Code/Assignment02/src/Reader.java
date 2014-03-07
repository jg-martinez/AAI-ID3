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
		System.out.println("Write path to ARFF format file:");
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
		if(!line.startsWith("%") && !line.startsWith("@relation") && !line.isEmpty()){
			line = line.replaceAll("\\s+", ""); //we remove spaces and \t
			String name = null;
			String values = null;
			line = line.toLowerCase();
			if(line.startsWith("@attribute")){	//reading an attribute
				line = line.replaceAll("@attribute", "");
				name = (String) line.subSequence(0, line.indexOf("{"));
				values = (String) line.subSequence(line.indexOf("{")+1, line.indexOf("}"));
				attributes.add(new Attribute(name,values));
			}			
			if(canReadData){ //if we are reading data	
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
			if(line.startsWith("@data")){
				canReadData = true;  //now we can read data
			}
		} 
	}
	
	public static void buildTree() {		
		analyseClass(attributes);			
		int indexMax = getIndexAttributeMaxGain(attributes);		
		if(indexMax != -1) {
			goDeeperFromRoot(attributes,indexMax);	
		}
		
	}
	
	public static void goDeeperFromRoot(ArrayList<Attribute> arrayOfAttributes, int indexMax){
		int nbAttribute = arrayOfAttributes.size()-1;
		int nbPossibleValue = arrayOfAttributes.get(indexMax).getPossibleValues().size();
		int nbClassValues = arrayOfAttributes.get(nbAttribute).getPossibleValues().size(); //number of possible class values
		int nbDataLine = arrayOfAttributes.get(nbAttribute).getValues().size(); //we take the number of data available			
		
		for(int value = 0; value < nbPossibleValue; value++){ //for each value of this attribute
			//we print the name of the attribute and its current value
			String string = new String();
			string += arrayOfAttributes.get(indexMax).getName() + " = ";
			string += arrayOfAttributes.get(indexMax).getPossibleValues().get(value).toString() + " : ";

			double[]classCounter = new double[nbClassValues];	
			for(int classValue = 0; classValue < nbClassValues; classValue++){
				classCounter[classValue] = 0;
			}
			
			for(int data = 0; data < nbDataLine; data++) { //we go through each line of data						
				if(arrayOfAttributes.get(indexMax).getValues().get(data).equals(arrayOfAttributes.get(indexMax).getPossibleValues().get(value))){
					//if this data is equal to the current possible value
					for(int classValue = 0; classValue < nbClassValues; classValue++){	 //we check at which class this data belongs					
						if(arrayOfAttributes.get(nbAttribute).getValues().get(data).equals(arrayOfAttributes.get(nbAttribute).getPossibleValues().get(classValue))){
							classCounter[classValue]++;
						}
					}
				}
			}			
			
			if(noNeedToGoDeeper(classCounter)){ //do we print plurality value/class value or not (are we at the end of the tree...)
				for(int k = 0; k < nbClassValues; k++){
					if(classCounter[k] != 0){
						string += arrayOfAttributes.get(nbAttribute).getPossibleValues().get(k).toString() + " = " + classCounter[k] + " | ";
					}
				}
				string = string.substring(0, string.length()-2);
				System.out.println(string); //we print the current attribute	
			} else { //if we have to go deeper in the tree
				System.out.println(string); //we print the current attribute	
				arrayOfAttributes.get(indexMax).createNextAttributes(arrayOfAttributes, 1,value); //we created the next node					
			}
		}
	}
	
	public static boolean noNeedToGoDeeper(double[] classe){
		boolean tempResult = false;
		//we check if there are more than one class value
		for(int i = 0; i < classe.length; i++){
			if(classe[i] != 0 && tempResult == false){
				tempResult = true;
			} else if (classe[i] != 0 && tempResult == true){ //if there are two values or more, we go deeper in the tree
				return false;
			}
		}
		return true;
	}
	
	public static int getIndexAttributeMaxGain(ArrayList<Attribute> arrayOfAttributes){
		double gainMax = -1;
		int indexMax = -1;
		for(int i= 0; i < arrayOfAttributes.size()-1; i++){ //looking for the max gain
			if(arrayOfAttributes.get(i).getGain() > gainMax){
				indexMax = i;
				gainMax = arrayOfAttributes.get(i).getGain();
			}
		}
		return indexMax;
	}
	
	public static void analyseClass(ArrayList<Attribute> arrayOfAttributes){
		int nbAttribute = arrayOfAttributes.size()-1; //index of the class
		int nbClassValues = arrayOfAttributes.get(nbAttribute).getPossibleValues().size(); //number of possible class values
		int nbDataLine = arrayOfAttributes.get(nbAttribute).getValues().size(); //we take the number of data available
		
		for(int currentAttribute = 0; currentAttribute < nbAttribute; currentAttribute++) {				
			double[] classCounter = new double[nbClassValues];	
			
			for(int i = 0; i < nbClassValues; i++){ //for each class value
				classCounter[i] = 0; //first we initialize the counter of this class value				
				for(int j = 0; j < nbDataLine; j++) { //we go through each line of data
					if (arrayOfAttributes.get(nbAttribute).getValues().get(j).equals(arrayOfAttributes.get(nbAttribute).getPossibleValues().get(i))){
						//if the class value of the current data is equal to this possible class value, we increment the counter
						classCounter[i]++;
					}
				}
			}
			calculateEntropyAndGain(arrayOfAttributes, nbClassValues, classCounter, currentAttribute);					
		}
	}
	
	public static void calculateEntropyAndGain(ArrayList<Attribute> arrayOfAttributes, int nbClassValues, double[] classCounter, int currentAttribute){
		double total = 0;
		for(int i = 0; i < nbClassValues; i++){ //sum the number of values
			total += classCounter[i];
		}
		//we calculate the entropy and the gain	for the current Attribute		
		arrayOfAttributes.get(currentAttribute).setEntropy(mathEntropy(classCounter, total));
		arrayOfAttributes.get(currentAttribute).calculateGain(arrayOfAttributes, currentAttribute, total);	
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
		String path = readPathOfFile();				
		if (checkArffExtension(path)){
			readFile(path);
			buildTree();		
		} else System.out.println("File doesn't have a .arff extension");
		}
}
