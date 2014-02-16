import java.util.ArrayList;


public class Attribute {
	private String name;
	private ArrayList<String> values;
	private double entropy;
	
	private ArrayList<ArrayList<Attribute>> nextAttributes;
	
	
	
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
	
	public double getEntropy(){
		return this.entropy;
	}
	
	public void createNextAttributes(){
		this.nextAttributes = new ArrayList<ArrayList<Attribute>>();
		
	}
}
