package com.smsbooker.pack;

import com.smsbooker.pack.models.MessagePart;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuriy on 12.05.2014.
 */

public class MessagesParser {

    final String GET_NUMBERS_REGEX = "[+-]?\\d+([,.]\\d+)?";

    public ArrayList<MessagePart> parseMessage(String message){
        Pattern pattern = Pattern.compile(GET_NUMBERS_REGEX);

        ArrayList<MessagePart> messageParts = new ArrayList<MessagePart>();

        Matcher mt = pattern.matcher(message);
        int lastParsedIndex = -1;
        while (mt.find()){
            if (messageParts.size() == 0 && mt.start() != 0){
                messageParts.add(new MessagePart(false, message.substring(0, mt.start())));
            }

            if (lastParsedIndex != -1 && lastParsedIndex < mt.start()){
                messageParts.add(new MessagePart(false, message.substring(lastParsedIndex, mt.start())));
            }

            messageParts.add(new MessagePart(true, message.substring(mt.start(), mt.end())));

            lastParsedIndex = mt.end();
        }

        return messageParts;
    }

    public String getPreviousPartText(ArrayList<MessagePart> messageParts, int partIndex){
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

    public String getNextPartText(ArrayList<MessagePart> messageParts, int partIndex){
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

    public String getPatternValue(String message, String previousText, String nextText){
        ArrayList<MessagePart> messageParts = parseMessage(message);
        int messagePartsCount = messageParts.size();
        for (int index = 1; index < messagePartsCount; index++){
            MessagePart
                previousPart = messageParts.get(index - 1),
                currentPart = messageParts.get(index);

            if (!previousPart.value.equalsIgnoreCase(previousText)){
                continue;
            }

            if (index + 1 == messagePartsCount && nextText == null){
                return currentPart.value;
            }

            MessagePart nextPart = messageParts.get(index + 1);
            if (index + 1 > messagePartsCount && nextText != null && currentPart.isNumber && nextPart.value == nextText){
                return currentPart.value;
            }
        }
        return null;
    }
}
