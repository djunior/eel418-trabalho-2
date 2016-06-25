package com.djunior.eel418trabalho2.utils;

import java.text.Normalizer;

public class Utils {
//------------------------------------------------------------------------------    
    public static String removeDiacriticals(String input) {
        if (input == null || input.equals("")) return input;
        input = input.toUpperCase();
        final String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("[,.:;'\"\n\t?!{}()/ ]+", " ");
    }
//------------------------------------------------------------------------------    
}
