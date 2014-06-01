package com.smsbooker.pack;

import com.smsbooker.pack.models.MessagePart;
import com.smsbooker.pack.models.ValuePattern;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuriy on 12.05.2014.
 */

public class MessageParser {

    final String GET_NUMBERS_REGEX = "[+-]?\\d+([,.]\\d+)?";
    final String SPLIT_INTO_WORDS_REGEX = "[\\S\\s][\\w']*[\\S\\s]";

    private ArrayList<MessagePart> messageParts;

    public MessageParser() {
        messageParts = new ArrayList<MessagePart>();
    }

    public MessageParser(String message) {
        this();

        parse(message);
    }

    public ArrayList<MessagePart> getMessageParts(){
        return messageParts;
    }

    public void parse(String message){
        messageParts.clear();

        Pattern pattern = Pattern.compile(GET_NUMBERS_REGEX);

        Matcher mt = pattern.matcher(message);
        int lastParsedIndex = -1;
        while (mt.find()){
            if (messageParts.size() == 0 && mt.start() != 0){
                splitAndCreateParts(message.substring(0, mt.start()), messageParts);
            }

            if (lastParsedIndex != -1 && lastParsedIndex < mt.start()){
                splitAndCreateParts(message.substring(lastParsedIndex, mt.start()), messageParts);
            }

            messageParts.add(new MessagePart(true, message.substring(mt.start(), mt.end())));

            lastParsedIndex = mt.end();
        }
    }

    private void splitAndCreateParts(String text, ArrayList<MessagePart> messageParts){
        Pattern pattern = Pattern.compile(SPLIT_INTO_WORDS_REGEX);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()){
            messageParts.add(new MessagePart(false, text.substring(matcher.start(), matcher.end())));
        }
    }

    private String getPreviousPartText(int partIndex){
        if (partIndex > 0){
            int currentPartIndex = partIndex - 1;
            do{
                MessagePart part = messageParts.get(currentPartIndex);
                if (!part.isNumber){
                    return part.value;
                }
                currentPartIndex--;
            } while (currentPartIndex >= 0);
        }
        return null;
    }

    private String getNextPartText(int partIndex){
        int messagePartsCount = messageParts.size();
        if (partIndex < messagePartsCount - 1){
            int currentPartIndex = partIndex + 1;
            do{
                MessagePart part = messageParts.get(currentPartIndex);
                if (!part.isNumber){
                    return part.value;
                }
                currentPartIndex++;
            } while (currentPartIndex < messagePartsCount);
        }
        return null;
    }

    public ValuePattern getPattern(int partIndex){
        return new ValuePattern(getPreviousPartText(partIndex), getNextPartText(partIndex));
    }

    public String getPatternValue(ValuePattern valuePattern){
        if (valuePattern == null){
            return null;
        }

        int messagePartsCount = messageParts.size();
        for (int index = 1; index < messagePartsCount; index++){
            MessagePart
                previousPart = messageParts.get(index - 1),
                currentPart = messageParts.get(index);

            if (!previousPart.value.equalsIgnoreCase(valuePattern.previousText)){
                continue;
            }

            if (index + 1 == messagePartsCount && valuePattern.nextText == null){
                return currentPart.value;
            }

            MessagePart nextPart = messageParts.get(index + 1);
            if (currentPart.isNumber && nextPart.value.equalsIgnoreCase(valuePattern.nextText)){
                return currentPart.value;
            }
        }
        return null;
    }
}
