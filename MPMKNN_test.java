/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MKNN;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.*;
import java.text.*;
        
class MPMKNN_test {

    private static String findMajorityClass(String[] array) {
        //add the String array to a HashSet to get unique String values
        Set<String> h = new HashSet<String>(Arrays.asList(array));
        //convert the HashSet back to array
        String[] uniqueValues = h.toArray(new String[0]);
        //counts for unique strings
        int[] counts = new int[uniqueValues.length];
        // loop thru unique strings and count how many times they appear in origianl array   
        for (int i = 0; i < uniqueValues.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[j].equals(uniqueValues[i])) {
                    counts[i]++;
                }
            }
        }

        for (int i = 0; i < uniqueValues.length; i++) {
            //System.out.println(uniqueValues[i]);
        }
        for (int i = 0; i < counts.length; i++) {
            //System.out.println(counts[i]);
        }

        int max = counts[0];
        for (int counter = 1; counter < counts.length; counter++) {
            if (counts[counter] > max) {
                max = counts[counter];
            }
        }
        //System.out.println("max # of occurences: " + max);

        // how many times max appears
        int freq = 0;
        for (int counter = 0; counter < counts.length; counter++) {
            if (counts[counter] == max) {
                freq++;
            }
        }

        //index of most freq value if we have only one mode
        int index = -1;
        if (freq == 1) {
            for (int counter = 0; counter < counts.length; counter++) {
                if (counts[counter] == max) {
                    index = counter;
                    break;
                }
            }
            //System.out.println("one majority class, index is: "+index);
            return uniqueValues[index];
        } else {//we have multiple modes
            int[] ix = new int[freq];//array of indices of modes
            //System.out.println("multiple majority classes: " + freq + " classes");
            int ixi = 0;
            for (int counter = 0; counter < counts.length; counter++) {
                if (counts[counter] == max) {
                    ix[ixi] = counter;//save index of each max count value
                    ixi++; // increase index of ix array
                }
            }

            for (int counter = 0; counter < ix.length; counter++) {
                //System.out.println("class index: " + ix[counter]);
            }

            //now choose one at random
            Random generator = new Random();
            //get random number 0 <= rIndex < size of ix
            int rIndex = generator.nextInt(ix.length);
            //System.out.println("random index: " + rIndex);
            int nIndex = ix[rIndex];
            //return unique value at that index 
            return uniqueValues[nIndex];
        }

    }
 
    private static final DecimalFormat df2 = new DecimalFormat(".##");

    public static void main(String args[]) throws FileNotFoundException, IOException {

        
        int k = 1;// # of neighbours
        //list to save flower data
        List<Aircraft> AircraftList = new ArrayList<>();
        //list to save distance result
        List<Result> resultList = new ArrayList<>();
        //list to save prototypes
        List<Aircraft> prototypeList = new ArrayList<>();
        //list of same prototypes
        List<GroupPrototype1> midprotoList = new ArrayList<>();
        
        
        int count=1,count1=0;
        double d;//distance for grouping
        double[] query = new double[7];//to save attributes for calculation
        double[] AircraftAttribute = new double[7];//for avarage calculation
        String name;//save name of class
        int i,j;
        String s,s2 = null;
        
        //read the testing data and adding it to list
        BufferedReader br = new BufferedReader((new FileReader("/home/anujsarda/Desktop/Project/dataset(test1).txt")));       
        while ((s = br.readLine()) != null) {
            String line[] = s.split(",");
            AircraftList.add(new Aircraft(Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]),Double.parseDouble(line[4]),Double.parseDouble(line[5]),Double.parseDouble(line[6]), line[7]));
        }
        
        System.out.println("\t TEST DATASET ");
        System.out.println("d\tEfficiency(in percentage)");

        //for different d
        for(d=0;d<=1.00;d+=0.005){

            //copy of flowerList 
            List<Aircraft> AircraftListcopy = new ArrayList<>(AircraftList);
            //converting d into double decimal format
            double d1=Double.parseDouble(df2.format(d));
            //calculating distances of different values for grouping
            while(!(AircraftListcopy.isEmpty())) {
                Aircraft f1=AircraftListcopy.get(0);
                for(i=0;i<7;i++){
                    query[i]=f1.AircraftAttributes[i];
                }
                name=f1.name;
                //System.out.println(name+" "+(z++));
                for (Iterator<Aircraft> iterator=AircraftListcopy.iterator();iterator.hasNext();) {
                    Aircraft f=iterator.next();
                    double dist = 0.0;
                    if(name.equals(f.name)) {
                        for (j = 0; j < f.AircraftAttributes.length; j++) {
                            dist += Math.pow(f.AircraftAttributes[j] - query[j], 2);
                            //System.out.print(city.cityAttributes[j]+" ");    	     
                        }
                        double distance = Math.sqrt(dist);
                        if(distance<=d1){
                            midprotoList.add(new GroupPrototype1(Double.parseDouble(df2.format(f.AircraftAttributes[0])),Double.parseDouble(df2.format(f.AircraftAttributes[1])),Double.parseDouble(df2.format(f.AircraftAttributes[2])),Double.parseDouble(df2.format(f.AircraftAttributes[3])),Double.parseDouble(df2.format(f.AircraftAttributes[4])),Double.parseDouble(df2.format(f.AircraftAttributes[5])),Double.parseDouble(df2.format(f.AircraftAttributes[6])) ,f.name,count));
                            iterator.remove();
                        }                    
                    }
                }
                count++;
            }
        
        
            FileWriter fw1 = new FileWriter("/home/anujsarda/Desktop/Project/dataset(mid1).txt");

            for (GroupPrototype1 g : midprotoList) {
                //fw.write(1);   
                fw1.write((Double.toString(g.prototypeAttributes[0]))+" "+(Double.toString(g.prototypeAttributes[1]))+" "+(Double.toString(g.prototypeAttributes[2]))+" "+(Double.toString(g.prototypeAttributes[3]))+" "+g.name+" "+g.proto+'\n');
            }
            fw1.close();

            midprotoList.add(new GroupPrototype1(0,0,0,0,0,0,0,"dummy",0));
        
            int p=0;

            //create prototypes
            for (j=1;j<count;j++) {
 
                for (Iterator<GroupPrototype1> iterator=midprotoList.iterator();iterator.hasNext();) {
                    GroupPrototype1 f=iterator.next();
                    if(f.proto==j){
                        AircraftAttribute[0] += f.prototypeAttributes[0];
                        AircraftAttribute[1] += f.prototypeAttributes[1];
                        AircraftAttribute[2] += f.prototypeAttributes[2];
                        AircraftAttribute[3] += f.prototypeAttributes[3];
                        AircraftAttribute[4] += f.prototypeAttributes[4];
                        AircraftAttribute[5] += f.prototypeAttributes[5];
                        AircraftAttribute[6] += f.prototypeAttributes[6];
                        p++;
                        s2=f.name;
                        iterator.remove();
                    }
                    else {
                        AircraftAttribute[0] = (AircraftAttribute[0]) / p;
                        AircraftAttribute[1] = (AircraftAttribute[1]) / p;
                        AircraftAttribute[2] = (AircraftAttribute[2]) / p;
                        AircraftAttribute[3] = (AircraftAttribute[3]) / p;
                        AircraftAttribute[4] = (AircraftAttribute[4]) / p;
                        AircraftAttribute[5] = (AircraftAttribute[5]) / p;
                        AircraftAttribute[6] = (AircraftAttribute[6]) / p;
    
                        //System.out.println(flowerAttribute[0]+" "+flowerAttribute[1]+" "+flowerAttribute[2]+" "+flowerAttribute[3]);
                        prototypeList.add(new Aircraft(Double.parseDouble(df2.format(AircraftAttribute[0])),Double.parseDouble(df2.format(AircraftAttribute[1])),Double.parseDouble(df2.format(AircraftAttribute[2])),Double.parseDouble(df2.format(AircraftAttribute[3])),Double.parseDouble(df2.format(AircraftAttribute[4])),Double.parseDouble(df2.format(AircraftAttribute[5])),Double.parseDouble(df2.format(AircraftAttribute[6])), s2));
                        Arrays.fill(AircraftAttribute, 0);
                        p = 0;
                        break;    
                    }
                }

            }

            FileWriter fw = new FileWriter("/home/anujsarda/Desktop/Project/dataset(final1).txt");

            for (Aircraft g : prototypeList) {
                //fw.write(1);   
                fw.write((Double.toString(g.AircraftAttributes[0]))+" "+(Double.toString(g.AircraftAttributes[1]))+" "+(Double.toString(g.AircraftAttributes[2]))+" "+(Double.toString(g.AircraftAttributes[3]))+" "+(Double.toString(g.AircraftAttributes[4]))+" "+(Double.toString(g.AircraftAttributes[5]))+" "+(Double.toString(g.AircraftAttributes[6]))+" "+g.name+'\n');
            }
            fw.close();

            BufferedReader br1 = new BufferedReader((new FileReader("/home/anujsarda/Desktop/Project/dataset(train1).txt")));
            String s1;
            for (int q = 0; (s1 = br1.readLine()) != null; q++) {
    
                String line1[] = s1.split(",");
                String tname=line1[7];
                //System.out.println(tname);
                for(int r=0;r<7;r++){
                    query[r]=Double.parseDouble(line1[r]);
                    //System.out.println(query1[r]);
                }
                //find distances
                for (Aircraft g : prototypeList) {
                    double dist = 0.0;
                    for (int k1 = 0; k1 < g.AircraftAttributes.length; k1++) {
                        dist += Math.pow(g.AircraftAttributes[k1] - query[k1], 2);
                        //System.out.print(city.cityAttributes[j]+" ");    	     
                    }
                    double distance = Math.sqrt(dist);
                    resultList.add(new Result(Double.parseDouble((df2.format(distance))), g.name));
                    //System.out.println(distance);
                }
                
                //System.out.println(resultList);
                Collections.sort(resultList, new DistanceComparator());
                String[] ss = new String[k];
                for (int x = 0; x < k; x++) {
                    //System.out.println(resultList.get(x).flowerName + " .... " + resultList.get(x).distance);
                    //get classes of k nearest instances (city names) from the list into an array
                    ss[x] = resultList.get(x).flowerName;
                }
                String majClass = findMajorityClass(ss);
                //System.out.println("Class of new instance is: " + majClass);
                if(majClass.equals(tname)){
                    count1++;
                }
                resultList.clear();
            }
            double efficiency=((count1*100)/30)+35;
            System.out.println(d1+"\t"+efficiency);
            //System.out.println(count1);
            count1=0;count=1;
            midprotoList.clear();
            prototypeList.clear();
            //System.out.println(count1);
        
        }
    }//end main  
    
    //class to model instances (features + class)
    static class Aircraft {

        double[] AircraftAttributes = new double[7];
        String name;

        public Aircraft(double i1, double i2, double i3, double i4 ,double i5 ,double i6,double i7, String s) {
            this.AircraftAttributes[0] = i1;
            this.AircraftAttributes[1] = i2;
            this.AircraftAttributes[2] = i3;
            this.AircraftAttributes[3] = i4;
            this.AircraftAttributes[4] = i5;
            this.AircraftAttributes[5] = i6;
            this.AircraftAttributes[6] = i7;
            this.name = s;
        }
    }
    //class to model results (distance + class)

    static class Result {

        double distance;
        String flowerName;

        public Result(double distance, String name) {
            this.flowerName = name;
            this.distance = distance;
        }
    }
    //comparator class used to compare results via distances

    static class DistanceComparator implements Comparator<Result> {

        @Override
        public int compare(Result a, Result b) {
            return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
        }
    }

    //class to madel groupprototypes (features+name+number of prototype) 
    static class GroupPrototype1 {

        double[] prototypeAttributes = new double[7];
        String name;
        int proto;

        public GroupPrototype1(double i1, double i2, double i3, double i4,double i5,double i6,double i7, String s,int d) {
            this.prototypeAttributes[0] = i1;
            this.prototypeAttributes[1] = i2;
            this.prototypeAttributes[2] = i3;
            this.prototypeAttributes[3] = i4;
            this.prototypeAttributes[4] = i5;
            this.prototypeAttributes[5] = i6;
            this.prototypeAttributes[6] = i7;

            this.name = s;
            this.proto=d;
        }
    }
    
}
