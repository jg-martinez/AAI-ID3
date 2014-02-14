import java.util.ArrayList;


public class Attribute {
	private String name;
	private ArrayList<String> values;
	private int entropy;
	
	public Attribute(String name, String stringValues){
		String tempValue = null;
		int index = 0;
		int entropy = 0;
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
		return this.name + this.values.toString();
	}
	
}
