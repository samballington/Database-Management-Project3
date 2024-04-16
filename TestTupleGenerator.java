
/*****************************************************************************************
 * @file  TestTupleGenerator.java
 *
 * @author   Sadiq Charaniya, John Miller
 */

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

/*****************************************************************************************
 * This class tests the TupleGenerator on the Student Registration Database defined in the
 * Kifer, Bernstein and Lewis 2006 database textbook (see figure 3.6).  The primary keys
 * (see figure 3.6) and foreign keys (see example 3.2.2) are as given in the textbook.
 */
public class TestTupleGenerator
{
    /*************************************************************************************
     * The main method is the driver for TestGenerator.
     * @param args  the command-line arguments
     */
    public static void main (String [] args) {
        var test = new TupleGeneratorImpl();

        test.addRelSchema("Student",
                "id name address status",
                "Integer String String String",
                "id",
                null);

        test.addRelSchema("Professor",
                "id name deptId",
                "Integer String String",
                "id",
                null);

        test.addRelSchema("Course",
                "crsCode deptId crsName descr",
                "String String String String",
                "crsCode",
                null);

        test.addRelSchema("Teaching",
                "crsCode semester profId",
                "String String Integer",
                "crcCode semester",
                new String[][]{{"profId", "Professor", "id"},
                        {"crsCode", "Course", "crsCode"}});

        test.addRelSchema("Transcript",
                "studId crsCode semester grade",
                "Integer String String String",
                "studId crsCode semester",
                new String[][]{{"studId", "Student", "id"},
                        {"crsCode", "Course", "crsCode"},
                        {"crsCode semester", "Teaching", "crsCode semester"}});

        String[] tableNames = new String[]{"Student", "Professor", "Course", "Teaching", "Transcript"};
        int[] tups = new int[]{10000, 1000, 2000, 50000, 5000};

        var resultTest = test.generate(tups);


        Table[] tables = new Table[tableNames.length];
        for (int i = 0; i < tableNames.length; i++) {
            String attributes = test.getSchema(tableNames[i]);
            String domains = convertAttributesToDomains(attributes);
            String primaryKey = test.getPrimaryKey(tableNames[i]);
            tables[i] = new Table(tableNames[i], attributes, domains, primaryKey);
        }

        for (int i = 0; i < tables.length; i++) {
            for (Comparable[] tuple : resultTest[i]) {
                tables[i].insert(tuple);
            }
        }

        for (Table table : tables) {
            System.out.println("Table: " + table.getName());
            //table.print();
        }

        // Select Operation: Find student with id = v
        KeyType searchKey = new KeyType(new Comparable[]{"v"}); // Replace 'id' with the actual ID value
        Table selectedStudents = tables[0].select(searchKey);  // Assuming tables[0] is "Student"

        System.out.println("Selected Students:");
        selectedStudents.print(); // select test (failed), need new select method with Keytype parameter

        // Join Operation: Join Student with Transcript on id = studId
        Table joinedTable = tables[0].join(tables[0]);  // join test, need new join method with attributes1, attributes2, table2 parameters

        System.out.println("Joined Table:");
        joinedTable.print();


    } // main

    /**
     * Convert attribute names to domain names. This is a simplified method and should be
     * tailored to map attribute types to specific domain types used in the system.
     * @param attributes Comma-separated string of attributes.
     * @return Comma-separated string of domain types.
     */
    private static String convertAttributesToDomains(String attributes) {
        return attributes.replaceAll("id|crsCode|studId", "Integer")
                .replaceAll("name|address|status|deptId|crsName|descr|semester|grade", "String");
    }

} // TestTupleGenerator

