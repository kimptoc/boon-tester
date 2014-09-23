package net.kimptoc;

import org.boon.datarepo.Repo;
import org.boon.datarepo.Repos;

import java.util.*;

import static org.boon.criteria.ObjectFilter.and;
import static org.boon.criteria.ObjectFilter.eq;
import static org.boon.criteria.ObjectFilter.in;

public class BoonInTest {

    static List<Map<String,Object>> database = new ArrayList<Map<String, Object>>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};
    static String[] sports = new String[] {"athletics", "soccer", "swim", "cycle"};

    static void log(String msg) {
        System.out.println(new Date()+":"+msg);
    }

    public static void main(String[] args) {
        log("begin");

        for (int i=0; i< 10; i++){
            Map<String,Object> entry = new HashMap();
            entry.put("name","Mr "+i);
            entry.put("colour",colours[i%colours.length]);
            entry.put("job",jobs[((int) (Math.random() * jobs.length))]);
            entry.put("sport",sports[((int) (Math.random() * sports.length))]);
            database.add(entry);
        }

        log("Loaded objects:"+database.size());

        Repo<Integer, Map> dbRepo = Repos.builder()
                .primaryKey("name")
                .lookupIndex("colour")
                .lookupIndex("job")
                .build(int.class, Map.class);

        dbRepo.addAll(database);
        log("Loaded repo:" + dbRepo.size());

        long start = System.currentTimeMillis();
        long count = 0;

//        for (int i=0; i<5; i++) {
//            findColour(dbRepo, "red");
//            findJob(dbRepo, "artist");
            count += findColourColour(dbRepo, "blue", "red").size();
//            findJobSport(dbRepo, "manager", "cycle");
//            findJobColour2(dbRepo, "blue", "clerk");
//        }


        log("end:"+(System.currentTimeMillis() - start)+":"+count+"-should be 4!");

    }

    private static void findJob(Repo<Integer, Map> dbRepo, String job) {
        long start;
        List<Map> result;
        long elapsed;

        start = System.currentTimeMillis();
        result = dbRepo.query(eq("job", job));
        elapsed = System.currentTimeMillis() - start;
        log("Time to find 'artist' people:"+elapsed+"ms - "+result.size()+" total.");
    }

    private static void findColour(Repo<Integer, Map> dbRepo, String colour) {
        long start = System.currentTimeMillis();
        List<Map> result = dbRepo.query(eq("colour", colour));
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find 'red' people:"+elapsed+"ms - "+result.size()+" total.");
    }

    private static List<Map> findJobColour(Repo<Integer, Map> dbRepo, String colour, String job) {
//        long start = System.currentTimeMillis();
        List<Map> result = dbRepo.query(and( eq("colour", colour), eq("job", job)));
//        long elapsed = System.currentTimeMillis() - start;
//        log("Time to find '"+colour+"' '"+job+"' people:"+elapsed+"ms - "+result.size()+" total.");
        return result;
    }
    private static List<Map> findColourColour(Repo<Integer, Map> dbRepo, String colour1, String colour2) {
//        long start = System.currentTimeMillis();
        List<Map> result = dbRepo.query(in("colour", new String[]{colour1,colour2}));
//        long elapsed = System.currentTimeMillis() - start;
//        log("Time to find '"+colour+"' '"+job+"' people:"+elapsed+"ms - "+result.size()+" total.");
        return result;
    }

    private static void findJobSport(Repo<Integer, Map> dbRepo, String job, String sport) {
        long start = System.currentTimeMillis();
        List<Map> result = dbRepo.query(and( eq("sport", sport), eq("job", job)));
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find '"+sport+"' '"+job+"' people:"+elapsed+"ms - "+result.size()+" total.");
    }
    private static void findJobColour2(Repo<Integer, Map> dbRepo, String colour, String job) {
        long start = System.currentTimeMillis();
//        List<Map> result = dbRepo.query(and( eq("colour", colour), eq("job", job)));
        List<Map> result = dbRepo.query(eq("colour", colour));
        long count = 0;
        for (Map map : result) {
            if (map.get("job").equals(job)) {
                count++;
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find '"+colour+"' '"+job+"' people2:"+elapsed+"ms - "+count+" total.");
    }
}
