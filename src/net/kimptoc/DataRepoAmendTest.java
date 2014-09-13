package net.kimptoc;

//Bug report from https://github.com/kimptoc/boon-tester

import org.boon.datarepo.Repo;
import org.boon.datarepo.Repos;

import java.util.*;

import static org.boon.Boon.puts;
import static org.boon.criteria.ObjectFilter.eq;

/**
 * Created by Richard on 9/8/14.
 */
public class DataRepoAmendTest {

    static List<Map<String, String>> database = new ArrayList<>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};
    static String[] sports = new String[] {"athletics", "soccer", "swim", "cycle", "couch potato"};


    public static void main(String[] args) {

        createEntry(database, 1, 0, 0, 0);
        createEntry(database, 2, 1, 1, 1);
        createEntry(database, 3, 0, 1, 2);
        createEntry(database, 4, 2, 0, 3);

        Repo<String, Map<String, String>> dbRepo = createDataRepo();

        List<Map<String, String>> managers = findBoon(dbRepo, "manager", "job");
        puts("Expect to be 2 managers:",managers.size());
        puts("Expect to be 2 clerk:", findBoon(dbRepo, "clerk", "job").size());
        puts("Expect to be 2 red:", findBoon(dbRepo, "red", "colour").size());

        puts("just amend object");
        managers.get(0).put("job","clerk");
        puts("Expect to be 1 manager:", findBoon(dbRepo, "manager", "job").size());
        puts("Expect to be 3 clerks:", findBoon(dbRepo, "clerk", "job").size());

        puts("try update call");
        dbRepo.update(managers.get(0));
        puts("Expect to be 1 manager:", findBoon(dbRepo, "manager", "job").size());
        puts("Expect to be 3 clerks:", findBoon(dbRepo, "clerk", "job").size());
        puts("db size:",dbRepo.size());

        puts("try modify call");
        dbRepo.modify(managers.get(0));
        puts("Expect to be 1 manager:", findBoon(dbRepo, "manager", "job").size());
        puts("Expect to be 3 clerks:", findBoon(dbRepo, "clerk", "job").size());
        puts("Expect to be 2 red:", findBoon(dbRepo, "red", "colour").size());
        puts("db size:", dbRepo.size());

        puts("recreate db");
        dbRepo = createDataRepo();
        puts("Expect to be 1 manager:", findBoon(dbRepo, "manager", "job").size());
        puts("Expect to be 3 clerks:", findBoon(dbRepo, "clerk", "job").size());
        puts("db size:", dbRepo.size());

    }

    private static Repo<String, Map<String, String>> createDataRepo() {
        Repo<String, Map<String, String>> dbRepo =  (Repo<String, Map<String, String>>)(Object) Repos.builder()
                .primaryKey("name")
                .lookupIndex("sport")
                .lookupIndex("job")
                .lookupIndex("colour")
                .build(String.class, Map.class);

        dbRepo.addAll(database);
        puts("db size:",dbRepo.size());
        return dbRepo;
    }

    private static void createEntry(List<Map<String, String>> database, int id, int colour, int job, int sport) {
        Map<String, String> entry = new HashMap<>();
        entry.put("name","Mr "+id);
        entry.put("colour",colours[colour]);
        entry.put("job",jobs[job]);
        entry.put("sport",sports[sport]);
        database.add(entry);
    }



    private static List<Map<String, String>> findBoon(Repo<String, Map<String, String>> dbRepo, String value, String property) {
        return dbRepo.query(eq(property, value));
    }
}
