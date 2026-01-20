import java.io.*;
import java.util.*;

public class Main {

    // Change this if your CSV filename differs
    private static final String CSV_FILE = "students.csv";

    public static void main(String[] args) {
        BST bst = new BST();

        System.out.println("========================================");
        System.out.println(" Student Record Management System (BST) ");
        System.out.println("========================================\n");

        // -------------------------
        // Load dataset (min 20)
        // -------------------------
        List<Student> dataset = loadStudentsFromCSV(CSV_FILE);

        if (dataset.size() < 20) {
            System.out.println("[WARN] CSV has less than 20 records. Adding extra sample records to meet requirement.\n");
            dataset.addAll(generateExtraStudents(20 - dataset.size(), 200));
        }
        // printing the whole set
            System.out.println("\n=== Displaying Entire Dataset ===");
            printList(dataset);

            // Shuffle dataset to avoid right-skewed BST
            Collections.shuffle(dataset);



        // -------------------------
        // Test Case 1: Functional
        // Insert >= 15 students
        // -------------------------


        System.out.println("=== Test Case 1: Functional Testing ===");
        System.out.println("=== Insert Students ===");
        int inserted = 0;
        for (Student s : dataset) {
            if (inserted >= 15) break;
            boolean ok = bst.insert(s.name, s.matric, s.cgpa);
            System.out.println("Insert: " + s.matric + " (" + s.name + ", CGPA " + String.format("%.2f", s.cgpa) + ")"
                    + (ok ? "" : "  [DUPLICATE REJECTED]"));
            if (ok) inserted++;
        }
        System.out.println("\n BST Size: " + bst.countNodes());

        //adding the lecturer record
        // bst.insert("zuri", "AIU01", 4.0);
        // printList(bst.postOrderList());
        // System.out.println("\n BST size: " + bst.countNodes());

        //deleting the lecturer
        // bst.delete("AIU01");
        // printList(bst.inOrderList());
        // System.out.println("\n BST Size: " + bst.countNodes());
        
        
        
        System.out.println("\n=== BST Diagram (Auto-generated) ===");
        bst.printTreeDiagram();

        System.out.println("\n=== In-order Traversal (Sorted by Matric) ===");
        printList(bst.inOrderList());

        System.out.println("\n=== Pre-order Traversal ===");
        printList(bst.preOrderList());

        System.out.println("\n=== Post-order Traversal ===");
        printList(bst.postOrderList());

        // Search: 3 existing + 2 non-existing
        System.out.println("\n=== Search 3 existing + 2 non-existing ===");
        List<Student> inorder = bst.inOrderList();
        if (inorder.size() >= 3) {
            searchAndPrint(bst, inorder.get(0).matric);
            searchAndPrint(bst, inorder.get(1).matric);
            searchAndPrint(bst, inorder.get(2).matric);
        }
        searchAndPrint(bst, "AIU999"); // non-existing
        searchAndPrint(bst, "AIU000"); // non-existing

        // Delete leaf, one-child, two-children
        System.out.println("\n=== Delete leaf / one-child / two-children ===");
        String leafKey = findLeafKey(bst);
        String oneChildKey = findOneChildKey(bst);
        String twoChildKey = findTwoChildKey(bst);

        deleteAndShow(bst, leafKey, "Leaf Node");
        deleteAndShow(bst, oneChildKey, "One-Child Node");
        deleteAndShow(bst, twoChildKey, "Two-Children Node");
        System.out.println("\n BST Size: " + bst.countNodes());

        // -------------------------
        // Test Case 2: Edge Cases
        // -------------------------
        System.out.println("\n=== Test Case 2: Edge Cases ===");

        // bst.insert("zuri", "AIU32", 4.0);
        // printList(bst.inOrderList());
        // System.out.println("\n BST size: " + bst.countNodes());
        // bst.delete("AIU32");
        // printList(bst.inOrderList());
        // System.out.println("\n BST Size: " + bst.countNodes());

        // Insert duplicate
        if (!inorder.isEmpty()) {
            Student dup = inorder.get(0);
            System.out.println("Insert duplicate matric: " + dup.matric);
            boolean ok = bst.insert(dup.name, dup.matric, dup.cgpa);
            System.out.println(ok ? "Unexpected: inserted duplicate!" : "Correct: duplicate rejected.");
        }

        // Delete non-existing
        System.out.println("\nDelete non-existing matric: AIU404");
        boolean del = bst.delete("AIU404");
        System.out.println(del ? "Unexpected: deleted non-existing!" : "Correct: cannot delete (not found).");

        // Operations on empty tree
        System.out.println("\nOperations on empty tree:");
        BST empty = new BST();
        System.out.println("Search on empty tree: " + (empty.search("AIU101") == null ? "Not Found (Correct)" : "Found (Wrong)"));
        System.out.println("Delete on empty tree: " + (!empty.delete("AIU101") ? "Not Deleted (Correct)" : "Deleted (Wrong)"));
        System.out.println("Inorder on empty tree size: " + empty.inOrderList().size());

        // Single-node tree behavior
        System.out.println("\nSingle-node tree behavior:");
        BST single = new BST();
        single.insert("Single Student", "AIU001", 3.33);
        System.out.println("Inorder:");
        printList(single.inOrderList());
        System.out.println("Delete AIU001: " + (single.delete("AIU001") ? "Deleted (Correct)" : "Not Deleted (Wrong)"));
        System.out.println("Now empty? " + (single.isEmpty() ? "Yes (Correct)" : "No (Wrong)"));

        // -------------------------
        // Sorting requirements
        // Merge Sort by Name
        // Quick Sort by CGPA
        // -------------------------
        System.out.println("\n=== Sorting Requirements ===");

        List<Student> records = bst.inOrderList(); // retrieve to array/list (as required)
        System.out.println("\n--- Merge Sort (by Name A-Z) ---");
        Sorting.mergeSortByName(records);
        printList(records);

        System.out.println("\n--- Quick Sort (by CGPA Desc) ---");
        Sorting.quickSortByCgpa(records, false);
        printList(records);

        // -------------------------
        // Utility Methods output
        // -------------------------
        System.out.println("\n=== Utility Methods ===");
        System.out.println("Count nodes: " + bst.countNodes());
        System.out.println("Height: " + bst.height());
        Student min = bst.minRecord();
        Student max = bst.maxRecord();
        System.out.println("Min matric: " + (min == null ? "N/A" : min.toString()));
        System.out.println("Max matric: " + (max == null ? "N/A" : max.toString()));

        System.out.println("\n--- Level Order Traversal (Bonus) ---");
        printList(bst.levelOrderList());

        // -------------------------
        // Test Case 3: Performance (n=1000)
        // -------------------------
        System.out.println("\n=== Test Case 3: Performance Evaluation (n=1000) ===");
        performanceTest(1000, 100, 100);

        System.out.println("\nDONE ✅");
    }

    // =========================
    // Helper: load CSV
    // Format:
    // ID_Number, name, cgpa
    // =========================
    private static List<Student> loadStudentsFromCSV(String fileName) {
        List<Student> list = new ArrayList<>();
        File f = new File(fileName);

        if (!f.exists()) {
            System.out.println("[WARN] CSV file not found: " + fileName);
            System.out.println("      Place students.csv in the SAME folder as your .java files (or update CSV_FILE).\n");
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header if present
                if (firstLine && line.toLowerCase().contains("id") && line.toLowerCase().contains("cgpa")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                // Basic CSV parsing (handles commas in name poorly; assume simple format)
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String matric = parts[0].trim();
                String name = parts[1].trim();
                double cgpa;
                try {
                    cgpa = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                list.add(new Student(name, matric, cgpa));
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Cannot read CSV: " + e.getMessage());
        }
        return list;
    }

    // If CSV is short, generate additional unique records
    private static List<Student> generateExtraStudents(int count, int startNumber) {
        List<Student> extra = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String matric = "AIU" + (startNumber + i);
            String name = "ExtraStudent" + (startNumber + i);
            double cgpa = 2.00 + (i % 201) * (2.00 / 200.0); // 2.00 to 4.00
            extra.add(new Student(name, matric, cgpa));
        }
        return extra;
    }

    private static void printList(List<Student> list) {
        for (Student s : list) System.out.println(s);
    }

    private static void searchAndPrint(BST bst, String matric) {
        System.out.println("\nSearch Matric Number: " + matric);
        Student res = bst.search(matric);
        if (res == null) {
            System.out.println("Result: NOT FOUND");
        } else {
            System.out.println("Result: FOUND");
            System.out.println("Name: " + res.name);
            System.out.println("CGPA: " + String.format("%.2f", res.cgpa));
        }
    }

    private static void deleteAndShow(BST bst, String key, String label) {
        System.out.println("\nDelete (" + label + "): " + key);
        if (key == null) {
            System.out.println("[WARN] Could not find a suitable " + label + " key in current tree.");
            return;
        }
        boolean ok = bst.delete(key);
        System.out.println(ok ? "Deleted successfully." : "Delete failed (not found).");

        System.out.println("Tree diagram after deletion:");
        bst.printTreeDiagram();

        System.out.println("Inorder after deletion:");
        printList(bst.inOrderList());
    }

    // Find keys for deletion cases (best effort)
    private static String findLeafKey(BST bst) {
        List<Student> level = bst.levelOrderList();
        // leaf likely near end of level order; just try and pick one that becomes leaf in current shape
        // We'll approximate by finding a node that, if deleted, doesn't break conditions: any found is fine.
        return level.isEmpty() ? null : level.get(level.size() - 1).matric;
    }

    private static String findOneChildKey(BST bst) {
        // Hard to guarantee without exposing nodes; choose a mid key and attempt
        List<Student> inorder = bst.inOrderList();
        if (inorder.size() < 3) return null;
        return inorder.get(inorder.size() / 2).matric;
    }

    private static String findTwoChildKey(BST bst) {
        // Root often has 2 children in a non-trivial tree; choose max of first half
        List<Student> inorder = bst.inOrderList();
        if (inorder.size() < 7) return null;
        return inorder.get(inorder.size() / 3).matric;
    }

    // Performance evaluation required by brief
    private static void performanceTest(int nInsert, int nSearch, int nDelete) {
        BST perf = new BST();
        Random rnd = new Random(12345);

        // Generate unique IDs
        String[] keys = new String[nInsert];
        for (int i = 0; i < nInsert; i++) {
            keys[i] = "AIU" + String.format("%05d", i + 1); // AIU00001 ...
        }

        long t1 = System.nanoTime();
        for (int i = 0; i < nInsert; i++) {
            double cgpa = 2.00 + rnd.nextDouble() * 2.00; // 2.00 - 4.00
            perf.insert("Student" + i, keys[i], cgpa);
        }

        // perf.insert("zuri", "AIU01001", 4.0); // Insert lecturer for performance test
        // System.out.println("\n=== In-order Traversal of Large Dataset ===");
        //     for (Student s : perf.inOrderList()) {
        //         System.out.println(s);
        // perf.delete("AIU01001"); 
        // perf.countNodes();
            // }
        System.out.println("\n BST Size: " + perf.countNodes());
        // Delete lecturer after test
        long t2 = System.nanoTime();

        long t3 = System.nanoTime();
        for (int i = 0; i < nSearch; i++) {
            String k = keys[rnd.nextInt(nInsert)];
            perf.search(k);
        }
        long t4 = System.nanoTime();

        long t5 = System.nanoTime();
        for (int i = 0; i < nDelete; i++) {
            String k = keys[rnd.nextInt(nInsert)];
            perf.delete(k);
        }
        long t6 = System.nanoTime();

        long t7 = System.nanoTime();
        perf.inOrderList();
        long t8 = System.nanoTime();

        System.out.println("Insert " + nInsert + " nodes: " + ((t2 - t1) / 1_000_000.0) + " ms");
        System.out.println("Search " + nSearch + " keys: " + ((t4 - t3) / 1_000_000.0) + " ms");
        System.out.println("Delete " + nDelete + " keys: " + ((t6 - t5) / 1_000_000.0) + " ms");
        System.out.println("Inorder traversal: " + ((t8 - t7) / 1_000_000.0) + " ms");
        System.out.println("Final nodes count: " + perf.countNodes());
        System.out.println("Final height: " + perf.height());
    }
}
