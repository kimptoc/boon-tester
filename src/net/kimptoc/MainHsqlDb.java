package net.kimptoc;

import java.sql.*;
import java.util.*;
import java.util.Date;


public class MainHsqlDb {

    static List<Map<String,Object>> database = new ArrayList<Map<String, Object>>();

    static String[] jobs = new String[] {"manager", "clerk", "footballer", "artist", "teacher"};
    static String[] colours = new String[] {"red", "blue", "green", "yellow", "orange"};
    static String[] sports = new String[] {"athletics", "soccer", "swim", "cycle"};

    static void log(String msg) {
        System.out.println(new Date()+":"+msg);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        log("begin");

        for (int i=0; i< Constants.MAX_N; i++){
            Map entry = new HashMap();
            entry.put("name","Mr "+i);
            entry.put("colour",colours[((int) (Math.random() * colours.length))]);
            entry.put("job",jobs[((int) (Math.random() * jobs.length))]);
            entry.put("sport",sports[((int) (Math.random() * sports.length))]);
            database.add(entry);
        }

        log("Loaded objects:"+database.size());

        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        Connection conn =
                DriverManager.getConnection("jdbc:hsqldb:testfiles", "SA",
                        "");
        saveToDatabase(conn,"MyCache");

        log("Loaded repo:?");


        for (int i=0; i<9; i++) {
            findColour(conn, "MyCache", "red");
//
//            findJob(dbRepo, "artist");
            findJobColour(conn, "MyCache", "blue", "clerk");
//            findJobSport(dbRepo, "manager", "cycle");
//            findJobColour2(dbRepo, "blue", "clerk");
        }


        log("end");

    }

    static void saveToDatabase(Connection conn, String tableName) throws SQLException {

        System.out.println("Re-creating the database...");

        // Create a statement object
        Statement stat = conn.createStatement();

        // Try to drop the table
        try {
            stat.executeUpdate("DROP TABLE "+tableName);
        } catch (SQLException e) {    // Ignore Exception, because the table may not yet exist
        }

        // For compatibility to other database, use varchar(255)
        // In HSQL Database Engine, length is unlimited, like Java Strings
        stat.execute("CREATE TABLE "+tableName
                + " (name varchar(255),job varchar(255),colour varchar(255),sport varchar(255))");

        // Close the Statement object, it is no longer used
        stat.close();

        // Use a PreparedStatement because Path and Name could contain '
        PreparedStatement prep =
                conn.prepareCall("INSERT INTO "+tableName+" (name,job,colour,sport) VALUES (?,?,?,?)");

        // Start with the 'root' directory and recurse all subdirectories
        insertRows(prep);

        // Close the PreparedStatement
        prep.close();

        // Index table
        Statement statement = conn.createStatement();
        statement.execute("CREATE INDEX colourIndex ON "+tableName+"(colour)");
        statement.execute("CREATE INDEX jobIndex ON "+tableName+"(job)");
        statement.close();

        System.out.println("Finished");
    }

    static void insertRows(PreparedStatement prep) throws SQLException {

        for (Map<String, Object> entry : database) {
            prep.clearParameters();
            prep.setObject(1, entry.get("name"));
            prep.setObject(2, entry.get("job"));
            prep.setObject(3, entry.get("colour"));
            prep.setObject(4, entry.get("sport"));
            prep.execute();

        }
    }


//    private static void findJob(Repo<Integer, Map> dbRepo, String job) {
//        long start;
//        List<Map> result;
//        long elapsed;
//
//        start = System.currentTimeMillis();
//        result = dbRepo.query(eq("job", job));
//        elapsed = System.currentTimeMillis() - start;
//        log("Time to find 'artist' people:"+elapsed+"ms - "+result.size()+" total.");
//    }

    private static void findColour(Connection conn, String tableName, String colour) throws SQLException {
        long start = System.currentTimeMillis();

        String sql = "SELECT * FROM " + tableName + " WHERE "
                + "colour =  '" + colour
                + "'";
        List<Map> filteredResult = queryDb(conn, sql);


        long elapsed = System.currentTimeMillis() - start;
        log("Time to find 'red' people:"+elapsed+"ms - "+filteredResult.size()+" total.");
    }

    private static List<Map> queryDb(Connection conn, String sql) throws SQLException {
        Statement stat = conn.createStatement();
        ResultSet result = stat.executeQuery(sql);

        List<Map> filteredResult = new ArrayList<Map>();
        // Moves to the next record until no more records
        ResultSetMetaData metaData = result.getMetaData();
        while (result.next()) {
            Map entry = new HashMap();
            for (int i=1; i<= metaData.getColumnCount(); i++) {
                entry.put(metaData.getColumnName(i),result.getObject(i));
            }
            filteredResult.add(entry);
        }

        // Close the ResultSet - not really necessary, but recommended
        result.close();
        stat.close();
        return filteredResult;
    }

    private static void findJobColour(Connection conn, String tableName, String colour, String job) throws SQLException {
        long start = System.currentTimeMillis();
        String sql = "SELECT * FROM " + tableName + " WHERE colour =  '" + colour + "' and job='"+job+"'";
        List<Map> filteredResult = queryDb(conn, sql);
        long elapsed = System.currentTimeMillis() - start;
        log("Time to find '"+colour+"' '"+job+"' people:"+elapsed+"ms - "+filteredResult.size()+" total.");
    }

//    private static void findJobSport(Repo<Integer, Map> dbRepo, String job, String sport) {
//        long start = System.currentTimeMillis();
//        List<Map> result = dbRepo.query(and( eq("sport", sport), eq("job", job)));
//        long elapsed = System.currentTimeMillis() - start;
//        log("Time to find '"+sport+"' '"+job+"' people:"+elapsed+"ms - "+result.size()+" total.");
//    }
//    private static void findJobColour2(Repo<Integer, Map> dbRepo, String colour, String job) {
//        long start = System.currentTimeMillis();
////        List<Map> result = dbRepo.query(and( eq("colour", colour), eq("job", job)));
//        List<Map> result = dbRepo.query(eq("colour", colour));
//        long count = 0;
//        for (Map map : result) {
//            if (map.get("job").equals(job)) {
//                count++;
//            }
//        }
//        long elapsed = System.currentTimeMillis() - start;
//        log("Time to find '"+colour+"' '"+job+"' people2:"+elapsed+"ms - "+count+" total.");
//    }
}
