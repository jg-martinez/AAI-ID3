import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {	
	static ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	static ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
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
			String name = null;
			String values = null;
			if(line.startsWith("@ATTRIBUTE")){						
				name = (String) line.subSequence(line.indexOf(" ") + 1, line.indexOf(" ", line.indexOf(" ") + 1));
				values = (String) line.subSequence(line.indexOf("{")+1, line.indexOf("}"));
				attributes.add(new Attribute(name,values));
			}			
			if(canReadData){	
				ArrayList<String> strList = new ArrayList<String>(); // for each line we create a new ArrayList of String
				for(String subString:line.split(",")){
					strList.add(subString);			    
				}
				datas.add(strList);
			}
			if(line.startsWith("@DATA")){
				canReadData = true;
			}
		} 
	}
	
	public static void calculateEntropyForRoot() {
		for(int i = 0; i < attributes.size()-1; i++) { //we exclude the last attribute (the class)
			double p = 0; //number of positive values
			double n = 0; //number of negatve values
			for(int j = 0; j < datas.size(); j++) { //we go through each line of data
				if (datas.get(j).get(attributes.size()-1).equals("P") || datas.get(j).get(attributes.size()-1).equals("Yes")){ //for each line, we get the last attribute (the class)
					p++; //if positive
				} else {
					n++; //if negative
				}
			}
			//we set the entropy of each attributes
			attributes.get(i).setEntropy(mathEntropy(p, n));
		}
		double entropyMax = -1;
		int indexMax = -1;
		for(int i= 0; i < attributes.size()-1; i++){ //looking for the max entropy
			if(attributes.get(i).getEntropy() > entropyMax){
				indexMax = i;
				entropyMax = attributes.get(i).getEntropy();
			}
		}
		attributes.get(indexMax).createNextAttributes(indexMax,attributes,datas);
		System.out.println(attributes.get(indexMax).toString()); //we print the tree
	}
	
	public static double mathEntropy(double p, double n) {
		double total = p + n;
		double result = 0;
		if(p != 0 && n != 0)
			result = -(p/total)*(Math.log(p/total)/Math.log(2))-n/total*(Math.log(n/total)/Math.log(2));
		return result;
	}
	
	
	public static void main(String[] args) {
		String path = readPathOfFile();		
		if (checkArffExtension(path)){
			readFile(path); //C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/Quilan.arff
			calculateEntropyForRoot();		
		} else System.out.println("File doesn't have a .arff extension");
		}
}
