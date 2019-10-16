//Ihfaz Tajwar
//Project 4 (External Sorting)
//CS 3345.007
//Greg Ozbirn

import java.io.*;
import java.util.*;
import java.nio.file.Path;

public class ExternalSort {

    //This method merges two runs from two files and writes that to a file
    private static boolean mergeAndWrite(Scanner s1, Scanner s2, FileWriter f, int run) throws IOException {
        int x = 0, y = 0, min = 0, xrun = 0, yrun = 0;
        boolean singleRunX = false, singleRunY = false;

        //Enter the first element from input file 1
        if (s1.hasNextInt()) {
            x = s1.nextInt();
        }
        else //If no new element, take all the remaining elements from second file
            singleRunY = true;

        //Enter the first element from input file 2
        if (s2.hasNextInt()) {
            y = s2.nextInt();
        }
        else //If no new element, take all the remaining elements from first file
            singleRunX = true;

        //Returns false if input files have been read
        if (singleRunX && singleRunY)
            return false;

        //If the run on the second file have no more values
        while (singleRunX){
            f.write(x + " ");
            if (!s1.hasNextInt())
                return false;
            x = s1.nextInt();
        }

        //If the run on the first file have no more values
        while (singleRunY){
            f.write(y + " ");
            if (!s2.hasNextInt())
                return false;
            y = s2.nextInt();
        }

        while(true){
            //Returns if runs have been completed on both the files
            if (xrun == run && yrun == run){
                if(!s1.hasNextInt() && !s2.hasNextInt()) //Returns false if both files have been read
                    return false;
                else
                    return true;
            }

            //If the run on the second file have no more values
            while (singleRunX){
                f.write(x + " ");
                if (!s1.hasNextInt())
                    return false;
                x = s1.nextInt();
            }

            //If the run on the first file have no more values
            while (singleRunY){
                f.write(y + " ");
                if (!s2.hasNextInt())
                    return false;
                y = s2.nextInt();
            }

            //If the run on the first file has been completed
            if (xrun == run){
                f.write(y + " ");
                yrun++;
                //Checks whether the file has any more values
                if (yrun < run && s2.hasNextInt())
                    y = s2.nextInt();
                else if (s2.hasNextInt() || s1.hasNextInt())
                    return true;
                else
                    return false;

                continue;
            }
            //If the run on the second file has been completed
            if (yrun == run){
                f.write(x + " ");
                xrun++;
                //Checks whether the file has any more values
                if (xrun < run && s1.hasNextInt())
                    x = s1.nextInt();
                else if (s1.hasNextInt() || s2.hasNextInt())
                    return true;
                else
                    return false;

                continue;
            }

            //Takes values from both the files and compares them
            if (x < y){
                min = x;
                xrun++;
                if (xrun < run){
                    if(!s1.hasNextInt()) {
                        singleRunY = true;
                    }else
                        x = s1.nextInt(); //Next value from first file
                }
            }else {
                min = y;
                yrun++;
                if (yrun < run) {
                    if(!s2.hasNextInt()){
                        singleRunX = true;
                    }else
                        y = s2.nextInt(); //Next value from second file

                }
            }
            f.write(min + " "); //Writes the value which is smaller
        }
    }

    //This method performs the external sort
    public static Path extsort(Path t1, int runsize) throws IOException {

        List<Integer> list = new ArrayList<>(); //AL to hold set of values temporarily
        //Declaring all the file objects
        File    f1 = new File(t1.toString()),
                f2 = new File("E:\\Spring 19\\Data Struct & Alg\\Project4\\src\\T2.txt"),
                f3 = new File("E:\\Spring 19\\Data Struct & Alg\\Project4\\src\\T3.txt"),
                f4 = new File("E:\\Spring 19\\Data Struct & Alg\\Project4\\src\\T4.txt");
        //Declaring the scanner objects to read from files
        Scanner sc1 = new Scanner(f1), sc2 = new Scanner(f2);
        //Declaring FileWriter objects to write to files
        FileWriter fw1 = new FileWriter(f3, false), fw2 = new FileWriter(f4, false);

        //Loops through the input file
        while (sc1.hasNextInt()){

            //Reads in the first set of values
            for(int i = 0; i < runsize && sc1.hasNextInt(); i++){
                list.add(sc1.nextInt());
            }
            //Sort and write to file
            Collections.sort(list);
            for(int i = 0; i < list.size(); i++){
                fw1.write(list.get(i) + " ");
            }
            list.clear();
            if (!sc1.hasNextInt())
                break;

            fw1.write("\n");

            //Reads in the next set of values
            for(int i = 0; i < runsize && sc1.hasNextInt(); i++){
                list.add(sc1.nextInt());
            }
            //Sort and write to file
            Collections.sort(list);
            for(int i = 0; i < list.size(); i++){
                fw2.write(list.get(i) + " ");
            }
            list.clear();
            if (!sc1.hasNextInt())
                break;

            fw2.write("\n");
        }

        //Close all the objects
        sc1.close();
        sc2.close();
        fw1.close();
        fw2.close();

        //This boolean determines which pair of files are output/input
        boolean firstSetOutput = true;

        while(true){
            //Assigns pairs of files for input and output alternatively until the list is sorted
            if(firstSetOutput){
                sc1 = new Scanner(f3);
                sc2 = new Scanner(f4);
                fw1 = new FileWriter(f1, false);
                fw2 = new FileWriter(f2, false);
            }else{
                sc1 = new Scanner(f1);
                sc2 = new Scanner(f2);
                fw1 = new FileWriter(f3, false);
                fw2 = new FileWriter(f4, false);
            }

            //Breaks out of the loop if the second file read is empty
            if (!sc2.hasNextInt()){
                if (firstSetOutput){
                    return f3.toPath();
                }else {
                    return f1.toPath();
                }
            }

            //Merge and write to file for each run
            while (true){
                if (!mergeAndWrite(sc1, sc2, fw1, runsize)){
                    fw1.write("\n");
                    break;
                }
                fw1.write("\n");

                if (!mergeAndWrite(sc1, sc2, fw2, runsize)){
                    fw2.write("\n");
                    break;
                }
                fw2.write("\n");
            }

            runsize *=2; //Double the runsize after every merge
            firstSetOutput = !firstSetOutput; //Reset the boolean

            //Close all the objects
            sc1.close();
            sc2.close();
            fw1.close();
            fw2.close();
        }

    }

    //Main method to demonstrate External Sort
    public static void main(String[] args) throws IOException {

        int runSize = Integer.valueOf(args[0]); //Takes the run size from user as CL arg

        //Checks to see if runsize is positive
        if (runSize <= 0){
            System.out.println("Invalid runsize, please run the program again.");
            System.exit(1);
        }

        //Creates the input file object which contains the list
        File inFile = new File("E:\\Spring 19\\Data Struct & Alg\\Project4\\src\\T1.txt");

        //Checks to see if file exists
        if(!inFile.exists()){
            System.out.println("File " + inFile.getName() + " does not exist.");
            System.exit(1);
        }

        //Calls the extsort method which sorts the list and returns the path of the output file
        System.out.print("Path for output file: ");
        System.out.println(ExternalSort.extsort(inFile.toPath(), runSize));

    }

}