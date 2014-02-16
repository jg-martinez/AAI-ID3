import java.util.ArrayList;


public class Attribute {
	private String name;
	private ArrayList<String> values;
	private double entropy;
	
	public Attribute(String name, String stringValues){
		String tempValue = null;
		int index = 0;
		this.entropy = 0;
		this.name = name;
		this.values = new ArrayList<String>();
		
		tempValue = (String) stringValues.subSequence(index, stringValues.indexOf(","));		
		index = stringValues.indexOf(",") + 1;
		this.values.add(tempValue);
		
		while(stringValues.indexOf(",", index) != -1){
			tempValue = (String) stringValues.subSequence(index, stringValues.indexOf(",", index));		
			index = stringValues.indexOf(",", index) + 1;
			this.values.add(tempValue);
		}
		
		tempValue = (String) stringValues.subSequence(index, stringValues.length());		
		this.values.add(tempValue);
		
		
		
	}
	
	public String toString(){
		return this.name + this.values.toString() + this.entropy;
	}
	
	public void setEntropy(double value) {
		this.entropy = value; 
	}
	
	public static double mathEntropy(double p, double n) {
		double total = p + n;
		double result = 0;
		if(p != 0 && n != 0)
			result = -(p/total)*(Math.log(p/total)/Math.log(2))-n/total*(Math.log(n/total)/Math.log(2));
		return result;
	}
	
}
