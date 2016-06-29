import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.io.Files;


public class ClassifierIII
{
    public static void main(String [] args) throws IOException{
        
        Map<String, Polarity> sentences = new HashMap<String, Polarity>();
        Hashtable<Ngram, Integer> posNgrams = new Hashtable<Ngram, Integer>();
        Hashtable<Ngram, Integer> negNgrams = new Hashtable<Ngram, Integer>();
        
        Set<Sentence> posSentences = new HashSet<Sentence>();
        Set<Sentence> negSentences = new HashSet<Sentence>();
        // *****************************READING NEGATIVE DATA***************************//
        File folderNeg = new File("C:\\Users\\Shubhi_Tyagi\\Downloads\\large-movie-reviews-dataset-master\\acl-imdb-v1\\train\\neg");
        File[] listOfFilesNeg = folderNeg.listFiles();
        for (File file : listOfFilesNeg) {
            if (file.isFile() && file.getName().endsWith("txt")) {
                sentences.put(Files.readLines(file, Charset.forName("utf-8")).get(0), Polarity.NEGATIVE);
            }
        }
        System.out.println("Read Negative Data Set - " + listOfFilesNeg.length);
        // *********************************READING POSITIVE DATA**********************//
        File folderPos = new File("C:\\Users\\Shubhi_Tyagi\\Downloads\\large-movie-reviews-dataset-master\\acl-imdb-v1\\train\\pos");
        File[] listOfFilesPos = folderPos.listFiles();
        for (File file : listOfFilesPos) {
            if (file.isFile() && file.getName().endsWith("txt")) {
                sentences.put(Files.readLines(file, Charset.forName("utf-8")).get(0), Polarity.POSITIVE);
            }
        }
        System.out.println("Read Positive Data Set - " + listOfFilesPos.length);
        
     // ******Annotating data set and creating Ngrams from it*********//
        System.out.println("Total Sentences - " + sentences.size());
        int j = 1;
        for (String sentenceString : sentences.keySet()) {
           System.out.println("Genrating Ngrams : "+j);
            Sentence sentence = new Sentence(sentenceString, sentences.get(sentenceString));
            // System.out.println(sentence.getWords().toString() + "-----" +
            // sentence.getPolarity().toString());
            sentence.generateNgrams(3);
            if (Polarity.NEGATIVE.equals(sentence.getPolarity())) {
                negSentences.add(sentence);
                for (Ngram ngram : sentence.getNgrams()) {
                    if(negNgrams.contains(ngram)){
                        negNgrams.put(ngram, negNgrams.get(ngram)+1);                
                    }
                    else{
                        negNgrams.put(ngram,1);
                    }
                }
            } else {
                posSentences.add(sentence);
                for (Ngram ngram : sentence.getNgrams()) {
                    if(posNgrams.contains(ngram)){
                        posNgrams.put(ngram,posNgrams.get(ngram)+1);
                    }
                    else{
                        posNgrams.put(ngram,1); 
                    }
                }
            }
            // System.out.println("Generated ngrams for "+j);
            j++;
        }
        System.out.println("Generated Ngrams for all of them . ");
        

        // **************Calculating Probability***************//
        double posNCount = posNgrams.size();
        double negNCount = negNgrams.size();
        double total = posNCount + negNCount;
        double pOfPosNgrams = posNCount / total;
        double pOfNegNgrams = 1 - pOfPosNgrams;
        // System.out.println("posNCount : "+posNCount +" negNCount :
        // "+negNCount+" total : "+total+" pOfPosNgrams : "+pOfPosNgrams+"
        // pOfNegNgrams : "+pOfNegNgrams);
        // ************Test Sentence *******//
        double countPos = 0;
        double countNeg = 0;
        double countNeut = 0;
        double pOfPos = 0.00;
        double pOfNeg = 0.00;
        int i = 0;
        List<String> testSentences = new ArrayList<String>();
        File testFolderNeg = new File("C:\\Users\\Shubhi_Tyagi\\Downloads\\large-movie-reviews-dataset-master\\acl-imdb-v1\\test\\neg");
        File[] testListOfFilesNeg = testFolderNeg.listFiles();
        for (File file : testListOfFilesNeg) {
            if (file.isFile() && file.getName().endsWith("txt")) {
               
                i++;
                // testSentences.add(Files.readLines(file,
                // Charset.forName("utf-8")).get(0));
                String test = Files.readLines(file, Charset.forName("utf-8")).get(0);

                // System.out.println("Read Negative Test Data Set - " +
                // testListOfFilesNeg.length);

                // for(String test : testSentences){

                Sentence testSentence = new Sentence(test);
                // Sentence testSentence = new Sentence("Once again Mr. Costner
                // has dragged out a movie for far longer than necessary . ");
                for (Ngram ngram : testSentence.generateNgrams(3)) {
                    double countPosNgrams = posNgrams.get(ngram);  //null pointer exception here 
                    double countNegNgrams = negNgrams.get(ngram);
                    double countOfNgrams = countPosNgrams + countNegNgrams;
                    double pOfNgram = (double) countOfNgrams / (total);
                    double pOfNgGivenPos = countPosNgrams / (double) (posNgrams.size());
                    double pOfNgGivenNeg = countNegNgrams / (double) (negNgrams.size());
                    if (pOfNgram != 0) {
                        pOfPos += pOfNgGivenPos * pOfPosNgrams / pOfNgram;
                        pOfNeg += pOfNgGivenNeg * pOfNegNgrams / pOfNgram;
                    }
                    // System.out.println(" Ngram : "+ngram.toString()+"
                    // countPosNgrams : "+countPosNgrams+" countNegNgrams :
                    // "+countNegNgrams);
                }
                // System.out.println("pOfPos : "+pOfPos+" pOfNeg : "+pOfNeg);
                if (pOfPos > pOfNeg) {
                    countPos++;
                    // System.out.println("Polarity :
                    // "+Polarity.POSITIVE.toString());
                } else if (pOfNeg > pOfPos) {
                    countNeg++;
                    // System.out.println("Polarity :
                    // "+Polarity.NEGATIVE.toString());
                } else {
                    countNeut++;
                    // System.out.println("Equal Probability");
                }
                // }
                System.out.println("Processing Test Sentences : " + i+" Pos : "+countPos+" Neg : "+countNeg+" Neut : "+countNeut+" Sentence : "+test); 
            }
            
        }
        System.out.println("Processed Testing Sentences "+i);
        double totalCount = countPos + countNeg + countNeut;
        System.out.println(" Pos : " + countPos + " Neg : " + countNeg + " Neut : " + countNeut);
        System.out.println(" Pos Accuraacy : " + (countPos / totalCount));
        System.out.println(" Neg Accuracy : " + (countNeg / totalCount));
    }
}
