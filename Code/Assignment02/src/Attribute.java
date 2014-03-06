import java.util.ArrayList;

public class Attribute {
	private String name;
	private ArrayList<String> possibleValues;
	private ArrayList<String> values;
	private double entropy;	
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
	}
	
	@SuppressWarnings("unchecked")
	public Attribute(Attribute a){
		this.name = a.name;
		this.possibleValues = (ArrayList<String>) a.possibleValues.clone();
		this.values = (ArrayList<String>) a.values.clone();
		this.entropy = a.entropy;		
		this.gain = a.gain;
	}	

	public int createNextAttributes(ArrayList<Attribute> array, int depth, int upperAttributeValue){		
		int nbAttribute = array.size()-1; //index of the class
		int nbClassValues = array.get(nbAttribute).getPossibleValues().size(); //number of possible class values
		int nbDataLine = array.get(nbAttribute).getValues().size(); //we take the number of data available
		
		
		//we parse each values of the current attribute
		for(int attribute = 0; attribute < nbAttribute; attribute++){  //i = the attribute
			if(array.get(attribute).getName().equals(this.name)){//if we get the right attribute, we parse its values
				for (int value = 0; value < array.get(attribute).getPossibleValues().size(); value++){ 
					
					//for each possible value, we go deeper in the tree
					ArrayList<Attribute> tempArray = new ArrayList<Attribute>();
					for(int k = 0; k < array.size(); k++){ //we duplicate the attributes array in a new array
						tempArray.add(new Attribute(array.get(k)));
					}
					
					//delete the data which have nothing to do with the value of the current attribute
					removeData(tempArray, attribute, upperAttributeValue); 
					
					parseTempArray(tempArray);
					
					//now we can check, with this new set of data, which entropy is maximum	
					int indexMax = Reader.getIndexAttributeMaxGain(tempArray);
					
					if(indexMax != -1) {
						//for each value of the attribute with the max Gain
						for(int j = 0; j < tempArray.get(indexMax).getPossibleValues().size(); j++){
							String string = new String();
							for(int k = 0; k < depth; k++){
								string += " ";
							}
							string += tempArray.get(indexMax).getName() + " = ";
							string += tempArray.get(indexMax).getPossibleValues().get(j).toString() + " : ";	
	
							double[]classeCounter = new double[tempArray.get(tempArray.size()-1).getPossibleValues().size()];	
							for(int k = 0; k < tempArray.get(tempArray.size()-1).getPossibleValues().size(); k++){
								classeCounter[k] = 0;
							}
							
							//we check the class value
							for(int dataLine = 0; dataLine < tempArray.get(tempArray.size()-1).getValues().size(); dataLine++) { //we go through each line of data
								if(tempArray.get(indexMax).getValues().get(dataLine).equals(tempArray.get(indexMax).getPossibleValues().get(j))){
									for(int classData = 0; classData < tempArray.get(tempArray.size()-1).getPossibleValues().size(); classData++){						
										if(tempArray.get(tempArray.size()-1).getValues().get(dataLine).equals(tempArray.get(tempArray.size()-1).getPossibleValues().get(classData))){//if we get a positive
											classeCounter[classData]++;
										}
									}
								}
							}
							boolean atLeastOneResult = false;
							if(Reader.noNeedToGoDeeper(classeCounter)){
								for(int k = 0; k < tempArray.get(tempArray.size()-1).getPossibleValues().size(); k++){
									if(classeCounter[k] != 0){
										atLeastOneResult = true;
										string += tempArray.get(tempArray.size()-1).getPossibleValues().get(k).toString() + " = " + classeCounter[k] + " | ";
									}
								}
								if(atLeastOneResult){ //if this attribute value has no class result we don't print it
									string = string.substring(0, string.length()-2);
									System.out.println(string); //we print the current attribute	
								}
							} else {  //if we have to go deeper in the tree							
								if(tempArray.get(tempArray.size()-1).getValues().size() <= 0){ //no more data
									return -1; //we leave the function
								}
								if(tempArray.size() == 1){ //if the only attribute left is the class
									return -1; //we leave the function
								}	
								System.out.println(string); //we print the the current attribute	
								tempArray.get(indexMax).createNextAttributes(tempArray, depth +1,j);						
							}
						}
						return 0;
					}					
				}
				
			}
		}
		return 0;
	}

	public void removeData(ArrayList<Attribute> tempArray, int attribute, int value){
		for(int k = 0; k < tempArray.get(attribute).getValues().size(); k++){ //for each value corresponding of the current possible value
			if(!tempArray.get(attribute).getValues().get(k).equals(tempArray.get(attribute).getPossibleValues().get(value))){ //if the data is different from the current value
				//we remove the value in the new array for each attribute
				for(int l = 0; l < tempArray.size(); l++){
					tempArray.get(l).getValues().remove(k);								
				}
				k--;
			}
		}
		//once we deleted the wrong data, we delete the current attribute
		tempArray.remove(attribute);
	}

	public void parseTempArray(ArrayList<Attribute> tempArray){
		int nbAttribute = tempArray.size()-1; //index of the class
		int nbClassValues = tempArray.get(nbAttribute).getPossibleValues().size(); //number of possible class values
		int nbDataLine = tempArray.get(nbAttribute).getValues().size(); //we take the number of data available
		
		for(int nextAttribute = 0; nextAttribute < nbAttribute; nextAttribute++){
			//we calculate for the currentAttribute the number of each class value
			double[]classeCounter = new double[nbClassValues];	
			//size = number of different value of class
			
			for(int i = 0; i < nbClassValues; i++){
				classeCounter[i] = 0;
				for(int l = 0; l < nbDataLine; l++) { //we go through each line of data
					if (tempArray.get(tempArray.size()-1).getValues().get(l).equals(tempArray.get(tempArray.size()-1).getPossibleValues().get(i))){
						classeCounter[i]++;
					}
				}
			}
			
			double total = 0;
			for(int i = 0; i < nbClassValues; i++){
				total += classeCounter[i];
			}
			//we set the entropy of each attributes			
			tempArray.get(nextAttribute).setEntropy(Reader.mathEntropy(classeCounter, total));
			tempArray.get(nextAttribute).calculateGain(tempArray, nextAttribute, total);	
		}
	}

	public void calculateGain(ArrayList<Attribute> tempArray, int currentAttribute, double total){
		this.gain = this.entropy;
		int nbAttribute = tempArray.size()-1; //index of the class
		int nbClassValues = tempArray.get(nbAttribute).getPossibleValues().size(); //number of possible class values
		int nbDataLine = tempArray.get(nbAttribute).getValues().size(); //we take the number of data available
		
		//[number of possible attribute value][number of possible class value]
		double[][] classCounter = new double[this.possibleValues.size()][nbClassValues];		
		
		for(int j = 0; j < this.possibleValues.size(); j++){ //possible attribute value
			for(int possibleClassValue = 0; possibleClassValue <  nbClassValues; possibleClassValue++){ //possible class value
				classCounter[j][possibleClassValue] = 0; //initialization				
				for(int dataLine = 0; dataLine < nbDataLine; dataLine++) { //we go through each line of data
					if(tempArray.get(currentAttribute).getValues().get(dataLine).equals(this.possibleValues.get(j))) {
						if (tempArray.get(tempArray.size()-1).getValues().get(dataLine).equals(tempArray.get(tempArray.size()-1).getPossibleValues().get(possibleClassValue))){
							classCounter[j][possibleClassValue]++;
						}
					}
				}				
			}
		}	
		//each possible attribute value has a class counter for each possible class value
		for(int i = 0; i < this.possibleValues.size(); i++){ //we calculate the gain for each value of the current attribute
			double tempValue = 0;
			for(int j = 0; j < nbClassValues; j++){				
				tempValue+= classCounter[i][j];				
			}
			if(tempValue != 0){
				this.gain -= (tempValue/total)*Reader.mathEntropy(classCounter[i],tempValue);
			}				
		}
	
	}

	public double getGain(){
		return this.gain;
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
	
	public double getEntropy(){
		return this.entropy;
	}

	public void setEntropy(double value) {
		this.entropy = value; 
	}	
	
	public String toString(){
		String string = new String();
		string = this.name + this.values.toString() + "   " + this.gain + "\n";
		return string;
	}	
	
}
