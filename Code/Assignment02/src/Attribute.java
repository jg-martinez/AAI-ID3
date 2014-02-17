import java.util.ArrayList;

public class Attribute {
	private String name;
	private ArrayList<String> possibleValues;
	private ArrayList<String> values;
	//private ArrayList<Attribute> childAttribute;
	private double entropy;	
	private int[] positive;
	private int[] negative;
	//private double[] specificEntropy;
	private double gain;
	
	public Attribute(String name, String stringValues){
		String tempValue = null;
		int index = 0;
		this.entropy = 0;
		this.gain = 0;
		this.name = name;
		this.possibleValues = new ArrayList<String>();
		this.values = new ArrayList<String>();		
		
		tempValue = (String) stringValues.subSequence(index, stringValues.indexOf(","));		
		index = stringValues.indexOf(",") + 1;
		this.possibleValues.add(tempValue);
		
		while(stringValues.indexOf(",", index) != -1){
			tempValue = (String) stringValues.subSequence(index, stringValues.indexOf(",", index));		
			index = stringValues.indexOf(",", index) + 1;
			this.possibleValues.add(tempValue);
		}
		
		tempValue = (String) stringValues.subSequence(index, stringValues.length());		
		this.possibleValues.add(tempValue);	
		
		this.positive = new int[this.possibleValues.size()];
		this.negative = new int[this.possibleValues.size()];
		//this.specificEntropy = new double[this.possibleValues.size()];
	}
	
	public void calculateGain(ArrayList<Attribute> array, double total){		
		for(int i = 0; i < this.possibleValues.size(); i++){
			positive[i] = 0;
			negative[i] = 0;
			for(int j = 0; j < this.values.size(); j++){
				if(values.get(j).equals(this.possibleValues.get(i))){ 
					if(array.get(array.size()-1).getValues().get(j).equals("Yes") || array.get(array.size()-1).getValues().get(j).equals("P")){
						positive[i]++;
					} else {
						negative[i]++;
					}
				}
			}
			//specificEntropy[i] = Reader.mathEntropy(positive[i], negative[i]);
		}		
		this.gain = this.entropy;
		for(int i = 0; i < this.possibleValues.size(); i++){
			this.gain -= (double)(((double)((double)positive[i] + (double)negative[i]))/(double)total)*Reader.mathEntropy((double)positive[i]/(double)((double)positive[i]+(double)negative[i]), (double)negative[i]/(double)((double)positive[i]+(double)negative[i]));
		}
	}
	
	public double getGain(){
		return this.gain;
	}
	
	@SuppressWarnings("unchecked")
	public Attribute(Attribute a){
		this.name = a.name;
		this.possibleValues = (ArrayList<String>) a.possibleValues.clone();
		this.values = (ArrayList<String>) a.values.clone();
		//this.childAttribute = (ArrayList<Attribute>) a.childAttribute.clone();
		this.entropy = a.entropy;
		this.positive = new int[this.possibleValues.size()];
		this.negative = new int[this.possibleValues.size()];
		this.gain = a.gain;
		//this.specificEntropy = new double[this.possibleValues.size()];
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<String> getPossibleValues(){
		return this.possibleValues;
	}
	
	public ArrayList<String> getValues(){
		return this.values;
	}
	
	public void setEntropy(double value) {
		this.entropy = value; 
	}	
	
	public double getEntropy(){
		return this.entropy;
	}
	
	public int createNextAttributes(ArrayList<Attribute> array, int dataSize){		
		/*this.childAttribute = new ArrayList<Attribute>(); //we duplicate the array
		for(int i = 0; i < array.size(); i++){
			this.childAttribute.add(new Attribute(array.get(i)));
		}*/
		if(array.get(array.size()-1).getValues().size() <= 0){
			return -1; //we return -1 if there is no more examples in this branch
		}
		
		if(array.size() == 1){ //if the only attribute left is the class
			return -2;
		}
		int positive = 0;
		int negative = 0;
		//we check the number of positive and negative classes
		for(int i = 0; i < dataSize; i++){
			if(array.get(array.size()-1).getValues().get(i).equals("P") || array.get(array.size()-1).getValues().get(i).equals("Yes")){//if we get a positive
				positive++;
			} else if(array.get(array.size()-1).getValues().get(i).equals("N") || array.get(array.size()-1).getValues().get(i).equals("No")){//if we get a negative
				negative++;
			}
		}
		
		if(positive == 0 || negative == 0){
			return 0; //this is a leaf of the tree
		}
		
		//now we can go deeper
		//we parse each values of the current attribute
		for(int i = 0; i < array.size()-1; i++){  //i = the attribute
			if(array.get(i).getName().equals(this.name)){//if we get the right attribute
				for (int j = 0; j < array.get(i).getPossibleValues().size(); j++){ //for each possible value, we go deeper in the tree
					// j = the possible value
					ArrayList<Attribute> tempArray = new ArrayList<Attribute>();
					for(int k = 0; k < array.size(); k++){
						tempArray.add(new Attribute(array.get(k)));
					}
					for(int k = 0; k < tempArray.get(i).getValues().size(); k++){ //for each value corresponding of the current possible value
						//k = the data
						if(!tempArray.get(i).getValues().get(k).equals(tempArray.get(i).getPossibleValues().get(j))){ //if the data is different from the current value
							//we remove the value in the new array for each attribute
							for(int l = 0; l < tempArray.size(); l++){
								tempArray.get(l).getValues().remove(k);								
							}
							k--;
						}
					}
					//once we deleted the wrong data, we delete the current attribute
					tempArray.remove(i);
					for(int k = 0; k < tempArray.size(); k++){ //we calculate the entropy for each attribute
						double p = 0; //number of positive values
						double n = 0; //number of negative values
						for(int l = 0; l < dataSize; l++) { //we go through each line of data
							if (tempArray.get(tempArray.size()-1).getValues().get(j).equals("P") || tempArray.get(tempArray.size()-1).getValues().get(j).equals("Yes")){
								p++; //if positive
							} else if(tempArray.get(tempArray.size()-1).getValues().get(j).equals("N") || tempArray.get(tempArray.size()-1).getValues().get(j).equals("No")){
								n++; //if negative
							}
						}
						//we set the entropy of each attributes
						tempArray.get(k).setEntropy(Reader.mathEntropy(p, n));
					}
					//now we can check, with this new set of data, which entropy is maximum					
				}
			}
		}
		return 1;
	}
	
	public void calculateEntropyForChildAttribute(ArrayList<Attribute> array){


	}
	
	public String toString(){
		String string = new String();
		string = this.name + this.entropy + "   " + this.gain + "\n";
		return string;
	}	
	
}
