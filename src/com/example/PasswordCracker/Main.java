package com.example.PasswordCracker;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import javafx.util.Pair;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {

    final static char[] supportedChar = new char[]{   //chars that the password can contain
            '!', '"', '#', '$', '%', '&', '(', ')', '*', '+', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', ']', '^', '_', '{', '|', '}', '~',
            'A', 'B', 'C' , 'D' ,  'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    static int maxSteps = 10000; //maximum rows in one array
    static String lookPassword = "A#AAA";      //password to check
    static long time;
    public static void main(String[] args) {
        time = System.currentTimeMillis();
        crackPassword(1);
    }

    public static void crackPassword(int length){
        System.out.println("trying with " + length);
        String rcv = getCombinations(length);
        if(rcv == null){
            crackPassword(length + 1);
        }else {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(time);

            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(System.currentTimeMillis());
            long hours = ChronoUnit.HOURS.between(c1.toInstant(), c2.toInstant());
            long minutes = ChronoUnit.MINUTES.between(c1.toInstant(), c2.toInstant());
            long seconds = ChronoUnit.SECONDS.between(c1.toInstant(), c2.toInstant());
            long milliseconds = ChronoUnit.MILLIS.between(c1.toInstant(), c2.toInstant());
            System.out.println(hours + ":" + minutes + ":" + seconds + ":" + (milliseconds - seconds * 1000) + " " + rcv);
        }
    }

    public static String getCombinations(int length){
        int chars = supportedChar.length;
        int steps = (int) Math.pow(chars, length);

        int[] nowChar = new int[length];
        for(int i= 0; i < nowChar.length; i++){
            nowChar[i] = -1;
        }

        int[] jTotal = new int[length];
        for(int jt = 0; jt < jTotal.length; jt++){
            jTotal[jt] = 0;
        }

        for(int s = 0; s < (int) Math.ceil((float) steps / (float) maxSteps); s++) {

            int localMaxSteps = maxSteps;
            if(localMaxSteps >= steps - s * maxSteps){
                localMaxSteps = steps - s * maxSteps;
            }
            char[][] charTable = new char[localMaxSteps][length];   //table with all possible combinations (rows = number of combinations, columns = length of password)
            for (int i = length - 1; i >= 0; i--) {   //runs through columns backwards
                for (int j = 0; j < localMaxSteps; j++) {     //runs through rows
                    int fillSame = (int) Math.pow(chars, i); //how many chars have to be filled in with same char
                    if (jTotal[i] % fillSame == 0 || jTotal[i] == 0) {    //selects next character when all fillSame characters are filled with same character
                        nowChar[i]++;
                        if (nowChar[i] >= chars) {   //if all characters are already used, start with the first one
                            nowChar[i] = 0;
                        }
                    }
                    try{
                        charTable[j][i] = supportedChar[nowChar[i]];   //set character in Table
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    jTotal[i]++;
                }
            }
            String[] combinations = new String[localMaxSteps];
            for (int i = 0; i < localMaxSteps; i++){
                combinations[i] = "";
                for(int j = length - 1; j >= 0; j--){
                    combinations[i] += charTable[i][j];
                }
            }
            for(String password: combinations){
                if(password.equals(lookPassword)){
                    return password;
                }
            }
        }
        return null;
    }
}

