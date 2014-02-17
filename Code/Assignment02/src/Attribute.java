import java.util.ArrayList;


public class Attribute {
	private String name;
	private ArrayList<String> values;
	private double entropy;
	private ArrayList<Attribute> childAttributes;
	
	
	
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
	

	
	public void setEntropy(double value) {
		this.entropy = value; 
	}	
	
	public double getEntropy(){
		return this.entropy;
	}
	
	public void createNextAttributes(int index, ArrayList<Attribute> array, ArrayList<ArrayList<String>> datas){
		
		if(datas.size() > 0){
			this.childAttributes = new ArrayList<Attribute>(array); //duplicate the attributes
			this.childAttributes.remove(index); //remove the root attribute from the new array
			
			for(int i = 0; i < values.size(); i++){
				ArrayList<ArrayList<String>> childDatas = new ArrayList<ArrayList<String>>(datas); 			
				for(int j = 0; j < childDatas.size(); j++){
					if(!this.values.get(i).equals(childDatas.get(j).get(index))){
						childDatas.remove(j);					
					}
				}
				for(int j = 0; j < childDatas.size(); j++){
					childDatas.get(j).remove(index); //similarly, we delete the column of this attribute on the new data array
				}			
				calculateEntropyForChildAttribute(this.childAttributes, childDatas);			
			}
		}
	}
	
	public void calculateEntropyForChildAttribute(ArrayList<Attribute> array, ArrayList<ArrayList<String>> datas){
		for(int i = 0; i < array.size(); i++) {
			double p = 0;
			double n = 0;
			for(int j = 0; j < datas.size(); j++) {
				if (datas.get(j).get(array.size()-1).equals("Yes")){
					p++;
				} else {
					n++;
				}
			}
			array.get(i).setEntropy(Reader.mathEntropy(p, n));
		}
		double entropyMax = -1;
		int indexMax = -1;
		for(int i= 0; i < array.size(); i++){
			if(array.get(i).getEntropy() > entropyMax){
				indexMax = i;
			}
		}
		if(indexMax != -1){
			array.get(indexMax).createNextAttributes(indexMax,array,datas);
		}
	}
	
	public String toString(){
		return this.name + this.values.toString() + this.entropy + "\n\t" + this.childAttributes.toString();
	}	
	
}
