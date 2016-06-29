import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.io.Files;

public class Classifier
{
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        Map<String, Polarity> sentences = new HashMap<String, Polarity>();
        List<Ngram> posNgrams = new ArrayList<Ngram>();
        List<Ngram> negNgrams = new ArrayList<Ngram>();
        Set<Sentence> posSentences = new HashSet<Sentence>();
        Set<Sentence> negSentences = new HashSet<Sentence>();
        /*sentences.put("This is a bad sentence ", Polarity.NEGATIVE);
        sentences.put("It was a pathetic movie", Polarity.NEGATIVE);
        sentences.put("This is a good sentence", Polarity.POSITIVE);
        sentences.put("This is not as bad as that", Polarity.POSITIVE);
        sentences.put("This is a bad thing that has happened to everyone", Polarity.NEGATIVE);*/
        List<String> linesNegSentences = Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_neg.txt"),
                        Charset.forName("utf-8"));
        for (String lines : linesNegSentences)
        {
            sentences.put(lines, Polarity.NEGATIVE);
        }
        System.out.println("Read Neg Data Set : " + linesNegSentences.size());
        List<String> linesPosSentences = Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_pos.txt"),
                        Charset.forName("utf-8"));
        for (String lines : linesPosSentences)
        {
            sentences.put(lines, Polarity.POSITIVE);
        }
        System.out.println("Read Pos Data Set : " + linesPosSentences.size());
        //sentences.put("This ia a ;  bad ,  Sentence ? ", Polarity.NEGATIVE);
        // ******Annotating data set and creating Ngrams from it*********//
        for (String sentenceString : sentences.keySet())
        {
            Sentence sentence = new Sentence(sentenceString,
                    sentences.get(sentenceString));
            //System.out.println(sentence.getWords().toString() + "-----" + sentence.getPolarity().toString());
            sentence.generateNgrams(1);
            if (Polarity.NEGATIVE.equals(sentence.getPolarity()))
            {
                negSentences.add(sentence);
                for (Ngram ngram : sentence.getNgrams())
                {
                    negNgrams.add(ngram);
                }
            }
            else
            {
                posSentences.add(sentence);
                for (Ngram ngram : sentence.getNgrams())
                {
                    posNgrams.add(ngram);
                }
            }
        }
        System.out.println("Generated Ngrams for all of them . ");
        // **************Calculating Probability***************//
        double posNCount = posNgrams.size();
        double negNCount = negNgrams.size();
        double total = posNCount + negNCount;
        double pOfPosNgrams = posNCount / total;
        double pOfNegNgrams = 1 - pOfPosNgrams;
        //System.out.println("posNCount : "+posNCount +" negNCount : "+negNCount+" total : "+total+" pOfPosNgrams : "+pOfPosNgrams+" pOfNegNgrams : "+pOfNegNgrams);
        // ************Test Sentence *******//
        double correct = 0;
        double wrong = 0;
        
        //String test = "It was an amazing movie";
        List<String> testSetPOS= Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_pos_validation.txt"),
                        Charset.forName("utf-8"));
        HashMap<String, Polarity> testPairs = new HashMap<String, Polarity>();
        for (String line : testSetPOS)
        {
            testPairs .put(line, Polarity.NEGATIVE);
        }
        List<String> testSetNEG= Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_neg_validation.txt"),
                        Charset.forName("utf-8"));
        for (String line : testSetNEG)
        {
            testPairs.put(line, Polarity.NEGATIVE);
        }
        
        int i = 0;
        for (String key : testPairs.keySet())
        {
            String test = key;
            Polarity pol = testPairs.get(key);
            
            //System.out.println("Processing test Sentences : " + i);
            i++;
            
            double pOfPos = 0.00;
            double pOfNeg = 0.00;
            Sentence testSentence = new Sentence(test);
            //Sentence testSentence = new Sentence("Once again Mr. Costner has dragged out a movie for far longer than necessary . ");
            for (Ngram ngram : testSentence.generateNgrams(1))
            {
                double countPosNgrams = Collections.frequency(posNgrams, ngram);
                double countNegNgrams = Collections.frequency(negNgrams, ngram);
                double countOfNgrams = countPosNgrams + countNegNgrams;
                double pOfNgram = (double) countOfNgrams / (total);
                double pOfNgGivenPos = countPosNgrams
                        / (double) (posNgrams.size());
                double pOfNgGivenNeg = countNegNgrams
                        / (double) (negNgrams.size());
                if (pOfNgram != 0)
                {
                    pOfPos += pOfNgGivenPos * pOfPosNgrams / pOfNgram;
                    pOfNeg += pOfNgGivenNeg * pOfNegNgrams / pOfNgram;
                }
                //         System.out.println(" Ngram : "+ngram.toString()+" countPosNgrams : "+countPosNgrams+" countNegNgrams : "+countNegNgrams);
            }
            //System.out.println("pOfPos : "+pOfPos+" pOfNeg : "+pOfNeg);
            if (pOfPos > pOfNeg && pol.equals(Polarity.POSITIVE))
            {
                correct++;
                System.out.println("POS : "+testSentence.getWords().toString());
                //System.out.println("Polarity : "+Polarity.POSITIVE.toString());
            }
            else if (pOfPos > pOfNeg)
            {
                wrong++;
                System.out.println("NEG : "+testSentence.getWords().toString());
                //System.out.println("Polarity : "+Polarity.POSITIVE.toString());
            }
            if (pOfPos < pOfNeg && pol.equals(Polarity.NEGATIVE))
            {
                correct++;
                System.out.println("NEG : "+testSentence.getWords().toString());
                //System.out.println("Polarity : "+Polarity.POSITIVE.toString());
            }
            else if (pOfPos < pOfNeg)
            {
                wrong++;
                System.out.println("POS : "+testSentence.getWords().toString());
                //System.out.println("Polarity : "+Polarity.POSITIVE.toString());
            }
            else
            {
                wrong++;
                //System.out.println("Equal Probability");
            }
        }
        System.out.println("Processed Test Sentences "+i);
        double totalCount = correct + wrong;
        System.out.println("Accuraacy : " + (correct / totalCount));
    }
}
