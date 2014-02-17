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
	

	public String getName(){
		return this.name;
	}
	
	public void setEntropy(double value) {
		this.entropy = value; 
	}	
	
	public double getEntropy(){
		return this.entropy;
	}
	
	public void createNextAttributes(int index, ArrayList<Attribute> array, ArrayList<ArrayList<String>> datas){
		/*this.childAttributes = (ArrayList<Attribute>) array.clone();
		this.childAttributes.remove(index);
		this.childDatas = (ArrayList<ArrayList<String>>) datas.clone();
		
		for(int i = 0; i < this.values.size(); i++){
			ArrayList<ArrayList<String>> tempDatas = new ArrayList<ArrayList<String>>(this.childDatas); 	
			for(int j = 0; j < tempDatas.size(); j++){		
				if(tempDatas.get(j).size() == 0) return;
				if(!this.values.get(i).equals(tempDatas.get(j).get(index))){
					tempDatas.remove(j);
					j--;					
				}
			}
			for(int j = 0; j < tempDatas.size(); j++){
				tempDatas.get(j).remove(index); //similarly, we delete the column of this attribute on the new data array
			}			
			calculateEntropyForChildAttribute(this.childAttributes, tempDatas);			
		}*/
	}
	
	public void calculateEntropyForChildAttribute(ArrayList<Attribute> array, ArrayList<ArrayList<String>> datas){
		/*for(int i = 0; i < array.size() - 1; i++) {
			double p = 0;
			double n = 0;
			for(int j = 0; j < datas.size(); j++) {
				if (datas.get(j).get(array.size()-1).equals("P")){
					p++;
				} else {
					n++;
				}
			}
			array.get(i).setEntropy(Reader.mathEntropy(p, n));
		}
		double entropyMax = -1;
		int indexMax = -1;
		for(int i= 0; i < array.size() - 1; i++){
			if(array.get(i).getEntropy() > entropyMax){
				indexMax = i;
				entropyMax = array.get(i).getEntropy();
			}
		}
		if(indexMax != -1){
			array.get(indexMax).createNextAttributes(indexMax,array,datas);
		}*/
	}
	
	public String toString(){
		String string = new String();
		string = this.name + this.values.toString() + this.entropy;
		return string;
	}	
	
}
