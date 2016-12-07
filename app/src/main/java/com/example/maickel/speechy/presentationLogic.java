package com.example.maickel.speechy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Maick on 12/7/2016.
 */

public class PresentationLogic {

    public StringBuilder countKeyWords(Set<String> keyWords, Integer wordAmount, ArrayList<String> result){
        Map<String, Integer> keyWordCount = new HashMap<>();
        StringBuilder temp = new StringBuilder();
        List<String> list = new ArrayList<>(keyWords);

        for(int y = 0; y < keyWords.size(); y++){
            int inputAmount = 0;
            for(int x = 0; x < wordAmount; x++) {
                if (list.get(y).equals(result.get(x))) {
                    inputAmount++;
                }
            }
            keyWordCount.put(list.get(y), inputAmount);
        }

        for(Map.Entry<String, Integer> entry : keyWordCount.entrySet()){
            temp.append("-"+entry.getKey() + "(" + entry.getValue() + ")" + System.lineSeparator());
        }
        return temp;
    }

    public Map countKeyWordsMap(Set<String> keyWords, Integer wordAmount, ArrayList<String> result){
        Map<String, Integer> keyWordCount = new HashMap<>();
        List<String> list = new ArrayList<>(keyWords);

        for(int y = 0; y < keyWords.size(); y++){
            int inputAmount = 0;
            for(int x = 0; x < wordAmount; x++) {
                if (list.get(y).equals(result.get(x))) {
                    inputAmount++;
                }
            }
            keyWordCount.put(list.get(y), inputAmount);
        }

        return keyWordCount;
    }

    public Map.Entry<String, Integer> countMostRepeated(ArrayList<String> list){
        Map<String, Integer> stringsCount = new HashMap<>();

        for(String s : list){
            if(s != "de".replaceAll("\\s+","") && s!= "het".replaceAll("\\s+","")
                    && s != "een".replaceAll("\\s+","") && s != "De".replaceAll("\\s+","")
                    && s != "Het".replaceAll("\\s+","") && s != "Een".replaceAll("\\s+","") ) {
                Integer c = stringsCount.get(s);
                if (c == null) c = new Integer(0);
                c++;
                stringsCount.put(s, c);
            }
        }

        Map.Entry<String, Integer> mostRepeated = null;

        for(Map.Entry<String, Integer> e: stringsCount.entrySet()){
            if(mostRepeated == null || mostRepeated.getValue() < e.getValue()){
                mostRepeated = e;
            }
        }

        return mostRepeated;
    }

    public Integer countWords (ArrayList<String> result){
        ArrayList<String> tempArray = new ArrayList<>();
        for(int i = 0; i < result.size(); i++){
            String[] temp = result.get(i).split(" ");
            for(int j = 0; j < temp.length; j++){
                tempArray.add(temp[j]);
            }
        }
        Integer wordAmount = tempArray.size();
        return wordAmount;
    }

    public String convertSecondsToMmSs(long millis){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);
        String ms = String.format("%02d:%02d",
                minutes, seconds);
        return ms;
    }
}
