package net.kimptoc;

import org.boon.datarepo.Repo;
import org.boon.datarepo.Repos;

import java.util.*;

import static org.boon.criteria.ObjectFilter.and;
import static org.boon.criteria.ObjectFilter.eq;

public class MainBoon {

    static List<Map<String,Object>> database = new ArrayList<Map<String, Object>>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};
    static String[] sports = new String[] {"athletics", "soccer", "swim", "cycle"};

    static void log(String msg) {
        System.out.println(new Date()+":"+msg);
    }

    public static void main(String[] args) {
        log("begin");

        for (int i=0; i< Constants.MAX_N; i++){
            Map<String,Object> entry = new HashMap();
            entry.put("name","Mr "+i);
            entry.put("colour",colours[((int) (Math.random() * colours.length))]);
            entry.put("job",jobs[((int) (Math.random() * jobs.length))]);
            entry.put("sport",sports[((int) (Math.random() * sports.length))]);
            database.add(entry);
        }

        log("Loaded objects:"+database.size());

        Repo<Integer, Map> dbRepo = Repos.builder()
                .primaryKey("name")
                .searchIndex("colour")
                .searchIndex("job")
                .build(int.class, Map.class);

        dbRepo.addAll(database);
        log("Loaded repo:" + dbRepo.size());


        for (int i=0; i<5; i++) {
            findColour(dbRepo, "red");

            findJob(dbRepo, "artist");
            findJobColour(dbRepo, "blue", "clerk");
            findJobSport(dbRepo, "manager", "cycle");
            findJobColour2(dbRepo, "blue", "clerk");
        }


        log("end");

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

    private static void findJobColour(Repo<Integer, Map> dbRepo, String colour, String job) {
        long start = System.currentTimeMillis();
        List<Map> result = dbRepo.query(and( eq("colour", colour), eq("job", job)));
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find '"+colour+"' '"+job+"' people:"+elapsed+"ms - "+result.size()+" total.");
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
