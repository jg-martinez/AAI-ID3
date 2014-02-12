Induction of Decision Trees
=======
###Objectives

The objectives of this second assignment (2a) are to:

Write a program to induce a decision tree from examples using the ID3 algorithm.
Improve your program with optional procedures or features.
Present your code and results in a short dissertation.

####Overview
The implementation of an elementary induction procedure is obligatory to pass the assignment. You have also to implement one optional procedure. A successful implementation of the optional procedure as well as improvements to the basic program will result in higher marks.

####Obligatory Part
Implement the decision tree learning algorithm according to Figure 18.5 in Russell-Norvig, Section 18.3. See the details in Sect. Basic Induction below.

####Optional Part
Modify your solution by adding improvements to the ID3 algorithm. You have to implement at least one optional procedure to pass the assignment.

Pruning is the most interesting one and a description is available in Sect. 18.3.5 of the textbook. Some other possible improvements are listed below in Sect. Improvements.

###Basic Induction (obligatory)
Your task is to implement a program that computes a decision tree according to the algorithm in Fig. 18.5, including an appropriate implementation of the IMPORTANCE function using entropy as explained in Russell-Norvig, Section 18.3.4.

You will encode the examples shown in Fig. 18.3 using the [Weka ARFF](http://weka.wikispaces.com/ARFF) format and you will write a reader program. You only need to write a subset of the ARFF format that corresponds to the examples in the figure.

The input to the program are the feature list and values, and the the examples. The output will be a decision tree. You can use this format:
```
Attribute1 = value1
   Attribute2 = value1: No
   Attribute2 = value2: Yes
Attribute1 = value2: No
Attribute1 = value3
   Attribute3 = value1
     Attribute4 = value1: No
     Attribute4 = value2: Yes
   Attribute3 = value2: No
```

In the first version of the algorithm, you can use a simple IMPORTANCE function, e.g. select the first of the remaining attributes or select it randomly. This is to check that the decision tree is built correctly. When you are certain of this, implement the IMPORTANCE function to choose the best attribute according to the information gain principle sketched in Sect. 18.3.4. of the textbook.

Once you have a working implementation for the restaurant examples, you can try more complex data sets from the Weka distribution.

###Improvements (optional)
Improve your ID3 implementation according to one or more of the following suggestions:

Apply a pruning procedure to your tree.
The information theory outlined in Section 18.3.4 is based on positive and negative examples only. Implement a generalized version of IMPORTANCE that is able to handle any number of values (c1, c2, ..., cn) for the classification attribute.
```
Attribute1 = value1
   Attribute2 = value1: c1
   Attribute2 = value2: c2
   Attribute2 = value2: c3
Attribute1 = value2: c2
Attribute1 = value3
   Attribute3 = value1
     Attribute4 = value1: c3
     Attribute4 = value2: c4
   Attribute3 = value2: c1
```
Example sets are available in the in the folder /usr/local/cs/EDA132/Learning/weka-3-7-3/data/.
Extend your implementation to allow attributes to have integer values. Input data to test this version are available in the folder /usr/local/cs/EDA132/Learning/weka-3-7-3/data/. The test condition for the integer attributes should be a numerical comparison with a split point such as value <= split_value or value > split_value in the decision tree. It is acceptable to convert the real numbers in an example set to the corresponding integer values, i.e. 1.234 may be converted to 1234.
If you find the data set too simple, use any of the data sets in the directory /usr/local/cs/EDA132/Learning/weka-3-7-3/data/ or data sets of your own choice.
