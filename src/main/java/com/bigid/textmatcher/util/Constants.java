package com.bigid.textmatcher.util;

import java.util.List;

public class Constants {
    public static final long INITIAL_OFFSET_START = 1;
    public static final long INITIAL_OFFSET_END = 0;
    public static int CONCURRENT_MATCHERS_NUMBER = 5;
    public static long BATCH_SIZE = 1000;
    public static String SOURCE_PATH = "https://norvig.com/big.txt";
    public static List<String> WORDS = List.of(
            "James", "John", "Robert", "Michael", "William", "David", "Richard",
            "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark",
            "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald",
            "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry",
            "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond",
            "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter",
            "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );

}
