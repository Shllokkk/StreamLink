/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Owner
 */
public class XMLParser {
    public static String toXML(String type, String to, String from, String message) {
        // return "<message>\n" +                            this format causes problem with readline()
        //        "     <type>" + type + "</type>\n"
        //        "     <to>" + to + "</to>\n" +
        //        "     <from> + from + "</from>\n"
        //        "     <body>" + message + "</body>\n" +
        //        "</message>";
        return "<message><type>" + type + "</type><to>" + to + "</to><from>" + from + "</from><body>" + message + "</body></message>";
    }

    public static String[] parseXML(String XML) {
        String[] values = new String[4];

        Pattern typePattern = Pattern.compile("<type>(.+?)</type>");
        Pattern receiverPattern = Pattern.compile("<to>(.+?)</to>");
        Pattern senderPattern = Pattern.compile("<from>(.+?)</from>");
        Pattern messagePattern = Pattern.compile("<body>(.+?)</body>");
        
        Matcher typeMatcher = typePattern.matcher(XML);
        if(typeMatcher.find()) {
            values[0] = typeMatcher.group(1);
        }
        
        Matcher receiverMatcher = receiverPattern.matcher(XML);
        if(receiverMatcher.find()) {
            values[1] = receiverMatcher.group(1);
        }
        
        Matcher senderMatcher = senderPattern.matcher(XML);
        if(senderMatcher.find()) {
            values[2] = senderMatcher.group(1);
        }
        
        Matcher messageMatcher = messagePattern.matcher(XML);
        if(messageMatcher.find()) {
            values[3] = messageMatcher.group(1);
        }
        return values;
    }
}
