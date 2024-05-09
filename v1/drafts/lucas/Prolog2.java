package autobot.v1.drafts.lucas;

import org.jpl7.Compound;
import org.jpl7.Query;
import org.jpl7.Term;

import java.util.ArrayList;
import java.util.List;

public class Prolog2 {
    public static void main(String[] args) {
        System.out.println("\n============== STRING ===================");
        testStr();
        System.out.println("\n============== DOUBLE ===================");
        testDouble();
    }

//    private static String queryBuilder(String ruleName, String... values) {
//        String valuesStr = String.join(", ", values);
//        return String.format("%s(%s)", ruleName, valuesStr);
//    }
//
//    private static String queryBuilderWithVars(String ruleName, String... values) {
//        String valuesStr;
//        if (values.length == 0) {
//            valuesStr = "X";
//        } else {
//            valuesStr = String.join(", ", values).replace("null", "X");
//        }
//        return String.format("%s(%s)", ruleName, valuesStr);
//    }
//
//    private static String getOneSolution(String ruleName, String... values) {
//        String query = queryBuilderWithVars(ruleName, values);
//        System.out.print(query + " = ");
//        Query q = new Query(query);
//        return q.hasSolution() ? q.oneSolution().get("X").toString() : null;
//    }
//
//    private static List<String> getAllSolutions(String ruleName, String... values) {
//        List<String> solutions = new ArrayList<>();
//        String query = queryBuilderWithVars(ruleName, values);
//        System.out.print(query + " = ");
//        Query q = new Query(query);
//        for (Map<String, Term> solution : q.allSolutions())
//            solutions.add(solution.get("X").toString());
//        return solutions;
//    }

    // ============================ TEST ============================

    static void testStr() {
        System.out.println("\n=================================");
        System.out.println("Adding facts...");

        addFact("amigos", "bia", "lucas");
        addFact("amigos", "bia", "gabriel");

        addFact("feliz", "bia");
        addFact("feliz", "lucas");

        System.out.println("\n=================================");
        System.out.println("Testing isValid...");

        System.out.println(isValid("amigos", "bia", "lucas"));
        System.out.println(isValid("amigos", "bia", "gabriel"));

        System.out.println(isValid("amigos", "lucas", "bia"));
        System.out.println(isValid("amigos", "gabriel", "bia"));

        System.out.println(isValid("amigos", "lucas", "gabriel"));
        System.out.println(isValid("amigos", "gabriel", "lucas"));

        System.out.println(isValid("feliz", "bia"));
        System.out.println(isValid("feliz", "lucas"));
        System.out.println(isValid("feliz", "gabriel"));

//        System.out.println("\n=================================");
//        System.out.println("\nTesting getOneSolution...");
//
//        System.out.println(getOneSolution("amigos", null, "bia"));
//        System.out.println(getOneSolution("amigos", null, "lucas"));
//        System.out.println(getOneSolution("amigos", null, "gabriel"));
//
//        System.out.println(getOneSolution("amigos", "bia", null));
//        System.out.println(getOneSolution("amigos", "lucas", null));
//        System.out.println(getOneSolution("amigos", "gabriel", null));
//
//        System.out.println(getOneSolution("feliz"));
//
//        System.out.println("\n=================================");
//        System.out.println("\nTesting getAllSolutions...");
//
//        System.out.println(getAllSolutions("amigos", null, "bia"));
//        System.out.println(getAllSolutions("amigos", null, "lucas"));
//        System.out.println(getAllSolutions("amigos", null, "gabriel"));
//
//        System.out.println(getAllSolutions("amigos", "bia", null));
//        System.out.println(getAllSolutions("amigos", "lucas", null));
//        System.out.println(getAllSolutions("amigos", "gabriel", null));
//
//        System.out.println(getAllSolutions("feliz"));
    }

    static void testDouble() {
        System.out.println("\n=================================");
        System.out.println("Adding facts...");

        addFact("amigos", 1.0, 2.0);
        addFact("amigos", 1.0, 3.0);

        addFact("feliz", 1.0);
        addFact("feliz", 2.0);

        System.out.println("\n=================================");
        System.out.println("Testing isValid...");

        System.out.println(isValid("amigos", 1.0, 2.0));
        System.out.println(isValid("amigos", 1.0, 3.0));

        System.out.println(isValid("amigos", 2.0, 1.0));
        System.out.println(isValid("amigos", 3.0, 1.0));

        System.out.println(isValid("amigos", 2.0, 3.0));
        System.out.println(isValid("amigos", 3.0, 2.0));

        System.out.println(isValid("feliz", 1.0));
        System.out.println(isValid("feliz", 2.0));
        System.out.println(isValid("feliz", 3.0));

//        System.out.println("\n=================================");
//        System.out.println("\nTesting getOneSolution...");
//
//        System.out.println(getOneSolution("amigos", null, 1.0));
//        System.out.println(getOneSolution("amigos", null, 2.0));
//        System.out.println(getOneSolution("amigos", null, 3.0));
//
//        System.out.println(getOneSolution("amigos", 1.0, null));
//        System.out.println(getOneSolution("amigos", 2.0, null));
//        System.out.println(getOneSolution("amigos", 3.0, null));
//
//        System.out.println(getOneSolution("feliz"));
//
//        System.out.println("\n=================================");
//        System.out.println("\nTesting getAllSolutions...");
//
//        System.out.println(getAllSolutions("amigos", null, 1.0));
//        System.out.println(getAllSolutions("amigos", null, 2.0));
//        System.out.println(getAllSolutions("amigos", null, 3.0));
//
//        System.out.println(getAllSolutions("amigos", 1.0, null));
//        System.out.println(getAllSolutions("amigos", 2.0, null));
//        System.out.println(getAllSolutions("amigos", 3.0, null));
//
//        System.out.println(getAllSolutions("feliz"));
    }

    // ============================= OLD =============================

    static void checkHasSolution(String filepath) {
        String goal = String.format("consult('%s').", filepath);
        if (!Query.hasSolution(goal)) {
            System.out.println("Consult failed");
        }
    }

    static boolean isEnemyClose(double EnemyDistance, double LimitDistance) {
        // isEnemyClose(EnemyDistance, LimitDistance) :- less(EnemyDistance, LimitDistance)
        Term[] terms = new Term[]{
                new org.jpl7.Float(EnemyDistance),
                new org.jpl7.Float(LimitDistance)
        };
        return hasSolution("isEnemyClose", terms);
    }

    private static boolean hasSolution(String ruleName, Term[] terms) {
        Query q = new Query(ruleName, terms);
        return q.hasSolution();
    }

    ///////////////////////////////////////////////////////////////////////////

    static Term[] toTermArr(Double... values) {
        List<Term> termsList = new ArrayList<>();
        for (Double value : values) termsList.add(new org.jpl7.Float(value));
        return termsList.toArray(new Term[0]);
    }

    static Term[] toTermArr(String... values) {
        List<Term> termsList = new ArrayList<>();
        for (String value : values) termsList.add(new org.jpl7.Atom(value));
        return termsList.toArray(new Term[0]);
    }

    private static void addFact(String ruleName, String... values) {
        Term[] terms = toTermArr(values);
        Term fact = new Compound(ruleName, terms);
        Query q = new Query("assert", fact);
        System.out.println(q);
        q.hasSolution();
    }

    private static void addFact(String ruleName, Double... values) {
        Term[] terms = toTermArr(values);
        Term fact = new Compound(ruleName, terms);
        Query q = new Query("assert", fact);
        System.out.println(q);
        q.hasSolution();
    }

    static boolean isValid(String ruleName, Double... values) {
        Term[] terms = toTermArr(values);
        Query q = new Query(ruleName, terms);
        System.out.print(q + " = ");
        return q.hasSolution();
    }

    static boolean isValid(String ruleName, String... values) {
        Term[] terms = toTermArr(values);
        Query q = new Query(ruleName, terms);
        System.out.print(q + " = ");
        return q.hasSolution();
    }
}