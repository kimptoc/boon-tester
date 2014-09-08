package net.kimptoc;

//import com.googlecode.cqengine.CQEngine;
//import com.googlecode.cqengine.IndexedCollection;
//import com.googlecode.cqengine.index.hash.HashIndex;

import java.util.*;

public class MainCQEngine {

    static Collection<Map> database = new ArrayList<Map>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};

    static void log(String msg) {
        System.out.println(new Date()+":"+msg);
    }

    public static void main(String[] args) {
        log("begin");

        for (int i=0; i<Constants.MAX_N; i++){
            Map entry = new HashMap();
            entry.put("name","Mr "+i);
            entry.put("colour",colours[((int) (Math.random() * colours.length))]);
            entry.put("job",jobs[((int) (Math.random() * jobs.length))]);
            database.add(entry);
        }

        log("Loaded objects:"+database.size());

//        indexedUsers = CQEngine.newInstance();
//        indexedUsers.addIndex(HashIndex.onAttribute("job"));
//        indexedUsers.addIndex(HashIndex.onAttribute("colour"));

        log("Loaded CQEngine:"+database.size());


        for (int i=0; i<5; i++) {
            findColour();
            findJob();
            findJobColour("clerk", "blue");
        }


        log("end");

    }

    private static void findJob() {
        long start;
        long count;
        long elapsed;

        start = System.currentTimeMillis();
        count = 0;
        for (Map entry : database) {
            if (entry.get("job").equals("artist")) {
                count++;
            }
        }
        elapsed = System.currentTimeMillis() - start;
        log("Time to find 'artist' people:"+elapsed+"ms - "+count+" total.");
    }

    private static void findColour() {
        long start = System.currentTimeMillis();
        long count = 0;
        for (Map entry : database) {
            if (entry.get("colour").equals("red")) {
                count++;
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find 'red' people:"+elapsed+"ms - "+count+" total.");
    }

    private static void findJobColour(String job, String colour) {
        long start = System.currentTimeMillis();
        long count = 0;
        for (Map entry : database) {
            if (entry.get("colour").equals(colour) && entry.get("job").equals(job)) {
                count++;
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find '"+colour+"' '"+job+"' people:"+elapsed+"ms - "+count+" total.");
    }
}
