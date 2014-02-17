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
		for(int i = 0; i < attributes.size()-1; i++) { //we exclude the last attribute (the class)
			double p = 0; //number of positive values
			double n = 0; //number of negative values
			for(int j = 0; j < dataSize; j++) { //we go through each line of data
				if (attributes.get(attributes.size()-1).getValues().get(j).equals("P")){
					p++; //if positive
				} else {
					n++; //if negative
				}
			}
			//we set the entropy of each attributes			
			attributes.get(i).setEntropy(mathEntropy(p, n));
			attributes.get(i).calculateGain(attributes, p+n);			
		}
		
		double gainMax = -1;
		int indexMax = -1;
		for(int i= 0; i < attributes.size()-1; i++){ //looking for the max entropy
			if(attributes.get(i).getEntropy() > gainMax){
				indexMax = i;
				gainMax = attributes.get(i).getGain();
			}
		}
		attributes.get(indexMax).createNextAttributes(attributes, dataSize);
		System.out.println(attributes.toString()); //we print the tree
	}
	
	public static double mathEntropy(double p, double n) {
		double total = p + n;
		double result = 0;
		if(p != 0 && n != 0)
			result = -(p/total)*(Math.log(p/total)/Math.log(2))-n/total*(Math.log(n/total)/Math.log(2));
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
