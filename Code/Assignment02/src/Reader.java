import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {

	
	static ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
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
		} 
	}
	
	public static void main(String[] args) {
		String path = readPathOfFile();		
		if (checkArffExtension(path)){
			readFile(path);
			System.out.println(attributes.toString());
		} else System.out.println("File doesn't have a .arff extension");
		}
}
