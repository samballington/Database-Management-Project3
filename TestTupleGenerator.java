 
/*****************************************************************************************
 * @file  TestTupleGenerator.java
 *
 * @author   Sadiq Charaniya, John Miller
 */

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
    public static void main (String [] args)
    {
        var test = new TupleGeneratorImpl ();

        test.addRelSchema ("Student",
                           "id name address status",
                           "Integer String String String",
                           "id",
                           null);
        
        test.addRelSchema ("Professor",
                           "id name deptId",
                           "Integer String String",
                           "id",
                           null);
        
        test.addRelSchema ("Course",
                           "crsCode deptId crsName descr",
                           "String String String String",
                           "crsCode",
                           null);
        
        test.addRelSchema ("Teaching",
                           "crsCode semester profId",
                           "String String Integer",
                           "crcCode semester",
                           new String [][] {{ "profId", "Professor", "id" },
                                            { "crsCode", "Course", "crsCode" }});
        
        test.addRelSchema ("Transcript",
                           "studId crsCode semester grade",
                           "Integer String String String",
                           "studId crsCode semester",
                           new String [][] {{ "studId", "Student", "id"},
                                            { "crsCode", "Course", "crsCode" },
                                            { "crsCode semester", "Teaching", "crsCode semester" }});

        var tables = new String [] { "Student", "Professor", "Course", "Teaching", "Transcript" };
        var tups   = new int [] { 10000, 1000, 2000, 50000, 5000 };
    
        var resultTest = test.generate (tups);
        
       // new code
        Table[] dbTables = new Table[tables.length];

        // Assuming 'attributes' and 'domains' are appropriately defined for each table
        for (int i = 0; i < tables.length; i++) {
            dbTables[i] = new Table(tables[i], attributes[i], domains[i], keys[i]);
            for (Comparable[] tuple : resultTest[i]) {
                dbTables[i].insert(tuple);
            }
        }

        // Print the content of each Table for debugging
        for (Table table : dbTables) {
            System.out.println("Table: " + table.getName());  // Assume getName() method exists to get table name
            for (Comparable[] tuple : table.getTuples()) {    // Assume getTuples() method exists to retrieve tuples
                System.out.println(Arrays.toString(tuple));
            }
            System.out.println();  // Blank line for better separation between tables
        }
    } // main

} // TestTupleGenerator

