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

}
