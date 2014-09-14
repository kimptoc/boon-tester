package net.kimptoc;

//Bug report from https://github.com/kimptoc/boon-tester

import org.boon.datarepo.Repo;
import org.boon.datarepo.Repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.boon.Boon.puts;
import static org.boon.criteria.ObjectFilter.eq;

/**
 * Created by Richard on 9/8/14.
 */
public class DataRepoAddTest {

    static List<Map<String, String>> database = new ArrayList<>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};
    static String[] sports = new String[] {"athletics", "soccer", "swim", "cycle", "couch potato"};


    public static void main(String[] args) {

        Repo<String, Map<String, String>> dbRepo = setup();

        List<Map<String, String>> managers1 = findBoon(dbRepo, "manager", "job");

        puts("Expect to be 2 managers:", findBoon(dbRepo, "manager", "job").size());

        Map<String, String> item = createItem(99, 0, 0, 0);
        dbRepo.add(item);
        puts("Expect to be 3 managers:", findBoon(dbRepo, "manager", "job").size());

        dbRepo.delete(managers1.get(0));

        puts("Expect to be 2 managers:", findBoon(dbRepo, "manager", "job").size());

    }

    private static Repo<String, Map<String, String>> setup() {
        database = new ArrayList<>();
        createEntry(database, 1, 0, 0, 0);
        createEntry(database, 2, 1, 1, 1);
        createEntry(database, 3, 0, 1, 2);
        createEntry(database, 4, 2, 0, 3);

        return createDataRepo();
    }

    private static Repo<String, Map<String, String>> createDataRepo() {
        Repo<String, Map<String, String>> dbRepo =  (Repo<String, Map<String, String>>)(Object) Repos.builder()
                .primaryKey("name")
                .lookupIndex("sport")
                .lookupIndex("job")
                .lookupIndex("colour")
                .build(String.class, Map.class);

        dbRepo.addAll(database);
//        puts("db size:",dbRepo.size());
        return dbRepo;
    }

    private static void createEntry(List<Map<String, String>> database, int id, int colour, int job, int sport) {
        Map<String, String> entry = createItem(id, colour, job, sport);
        database.add(entry);
    }

    private static Map<String, String> createItem(int id, int colour, int job, int sport) {
        Map<String, String> entry = new HashMap<>();
        entry.put("name","Mr "+id);
        entry.put("colour",colours[colour]);
        entry.put("job",jobs[job]);
        entry.put("sport",sports[sport]);
        return entry;
    }


    private static List<Map<String, String>> findBoon(Repo<String, Map<String, String>> dbRepo, String value, String property) {
        return dbRepo.query(eq(property, value));
    }
}
