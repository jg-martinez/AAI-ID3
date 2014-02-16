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
			System.out.println(line);
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
		for(int i = 0; i < attributes.size(); i++) {
			double p = 0;
			double n = 0;
			for(int j = 0; j < datas.size(); j++) {
				if (datas.get(attributes.size()).equals("Yes"))
					p++;
				else n++;
			}
			attributes.get(i).setEntropy(Attribute.mathEntropy(p, n));
		}
	}
	
	public static void main(String[] args) {
		String path = readPathOfFile();		
		if (checkArffExtension(path)){
			readFile(path); //C:/Users/Tywuz/Documents/GitHub/AAI-ID3/WEKA_Format_Files/restaurant.arff
			System.out.println(attributes.toString());
			System.out.println(datas.toString());
		} else System.out.println("File doesn't have a .arff extension");
		}
}
