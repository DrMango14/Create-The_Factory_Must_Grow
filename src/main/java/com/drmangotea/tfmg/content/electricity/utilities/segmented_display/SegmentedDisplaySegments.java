package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import java.util.*;

public class SegmentedDisplaySegments {
    public static Map<String, List<Integer>> SYMBOLS_TO_SEGMENTS= new HashMap<>();
    static {


        //
        put(0,1,2,3,5,6,7);
        put(1,1,2);
        put(2,3,6,4,2,5);
        put(3,3,4,5,6,7);
        put(4,1,4,6,7);
        put(5,1,3,4,5,7);
        put(6,1,2,3,4,5,7);
        put(7,7,6,3);
        put(8,1,2,3,4,5,6,7);
        put(9,1,3,4,6,7);

        put(":",0);

        put("a",1,2,3,4,6,7);
        put("b",1,2,4,5,7);
        put("c",1,2,3,5);
        put("d",2,4,5,6,7);
        put("e",1,2,3,4,5);
        put("f",1,2,3,4);
        put("g",1,2,3,5,7);
        put("h",1,2,4,6,7);
        put("i",1,2);
        put("j",6,7,5,2);
        put("k",1,2,9,10);
        put("l",1,2,5);
        put("m",1,2,6,7,8);
        put("n",2,7,10);
        put("o",1,2,6,7,3,5);
        put("p",1,2,3,4,6);
        put("q",1,3,4,6,7);
        put("r",1,2,3,4,6,10);
        put("s",1,3,4,5,7);
        put("t",1,2,4,5);
        put("u",2,5,7);
        put("v",1,2,5,6,7);
        put("w",1,2,4,5,6,7);
        put("x",1,2,4,6,7);
        put("y",1,4,6,7,5);
        put("z",3,9,4);


    }
    public static void put(int number, Integer... segments){
        put(String.valueOf(number),segments);
    }
    private static void put(String string, Integer... segments){

        SYMBOLS_TO_SEGMENTS.put(string, Arrays.asList(segments));

        List<Integer> segments2 = new ArrayList<>();

        for (Integer segment : segments){

            if(segment == 0)
                break;

            segments2.add(10+segment);
        }


        SYMBOLS_TO_SEGMENTS.put(string, segments2);

    }
}
