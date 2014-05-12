package com.smsbooker.pack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuriy on 12.05.2014.
 */
public class MessagesParser {

    final String GET_NUMBERS_REGEX = "[+-]?\\d+([,.]\\d+)?";

    public void parseMessage(String message){
        Pattern pattern = Pattern.compile(GET_NUMBERS_REGEX);

        Matcher mt = pattern.matcher(message);
        while (mt.find()){
            String match = message.substring(mt.start(), mt.end());
            int temp = mt.start();
        }
    }

}
