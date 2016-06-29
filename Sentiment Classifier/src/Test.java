import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.io.Files;


public class Test
{
    public static void main(String[] args) throws IOException
    {
        List<String> linesNegSentences = Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_neg.txt"),Charset.forName("utf-8"));
        for(String sent : linesNegSentences){
            sent=sent.replace(".", "").replace(",", "");
            List<String> tokens =Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(sent);
            System.out.println("N,"+tokens.toString().replace("[", "").replace("]", "").replace(" ", ""));
        }
        List<String> linesPosSentences = Files
                .readLines(
                        new File(
                                "C:\\Users\\Shubhi_Tyagi\\Downloads\\rt-polaritydata\\rt-polarity_pos.txt"),Charset.forName("utf-8"));
        for(String sent : linesPosSentences){
            sent=sent.replace(".", "").replace(",", "");
            List<String> tokens =Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(sent);
            System.out.println("P,"+tokens.toString().replace("[", "").replace("]", "").replace(" ", ""));
        }
    }
}
