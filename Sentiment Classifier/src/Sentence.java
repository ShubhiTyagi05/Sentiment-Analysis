import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Splitter;

public class Sentence {
    Polarity polarity=null;
    List<Ngram> ngram;
    List<String> words;
     Set<String> set = new HashSet<String>();

        {
            set.add(".");
            set.add(",");
            set.add("--");
            set.add("?");
            set.add("\"");
            set.add("(");
            set.add(")");
            set.add("[");
            set.add("]");
            set.add(":");
            set.add("!");
            set.add(";");
        }
    
    Sentence(){
        ngram = new ArrayList<Ngram>();
        words = new ArrayList<String>();

    }
    Sentence(String sentence){
        ngram = new ArrayList<Ngram>();
        this.setWords(sentence);
    }
    Sentence(String sentence, Polarity polarity) {
        ngram = new ArrayList<Ngram>();
        this.setWords(sentence);
        this.polarity = polarity;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public void setPolarity(Polarity polarity) {
        this.polarity = polarity;
    }

    public List<Ngram> getNgrams() {
        return ngram;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(String sentence) {
        
        this.words = removeStopWords(sentence);
    }

    public List<String> removeStopWords(String sentence){
        
            List<String> allWords = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(sentence);
            List<String> words = new ArrayList<String>();
            for (String word : allWords) {
                if (set.contains(word)) {
                    
                    continue;
                } else {
                    words.add(word);
                }
            }
            return words;
    
    }
    
    public List<Ngram> generateNgrams(int n) {

        if(n==0){
            System.out.println("Value 0f N cannot be 0 for ngram generation");
            return null;
        }
        else{
        for (int i = 0; i < words.size() - n + 1; i++) {
            Ngram ngram = new Ngram();
            ngram.setWords(this.words.subList(i, i + n));
            this.ngram.add(ngram);
        
        }
        return ngram;
        }
    }
}

