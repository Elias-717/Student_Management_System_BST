import java.util.*;

public class BST {
    private Node root;

    // =========================
    // INSERT (reject duplicates)
    // =========================
    public boolean insert(String name, String matric, double cgpa) {
        Student s = new Student(name, matric, cgpa);
        if (root == null) {
            root = new Node(s);
            return true;
        }
        Node cur = root;
        while (true) {
            int cmp = matric.compareTo(cur.data.matric);
            if (cmp == 0) return false; // duplicate -> reject
            if (cmp < 0) {
                if (cur.left == null) {
                    cur.left = new Node(s);
                    return true;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = new Node(s);
                    return true;
                }
                cur = cur.right;
            }
        }
    }

    // =========================
    // SEARCH
    // =========================
    public Student search(String matric) {
        Node cur = root;
        while (cur != null) {
            int cmp = matric.compareTo(cur.data.matric);
            if (cmp == 0) return cur.data;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    // =========================
    // DELETE (0/1/2 children)
    // =========================
    public boolean delete(String matric) {
        if (root == null) return false;
        int before = countNodes();
        root = deleteRec(root, matric);
        int after = countNodes();
        return after < before;
    }

    private Node deleteRec(Node node, String matric) {
        if (node == null) return null;

        int cmp = matric.compareTo(node.data.matric);
        if (cmp < 0) {
            node.left = deleteRec(node.left, matric);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, matric);
        } else {
            // Found node to delete

            // Case 1: Leaf
            if (node.left == null && node.right == null) return null;

            // Case 2: One child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Case 3: Two children -> inorder successor (min in right subtree)
            Node successor = minNode(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data.matric);
        }
        return node;
    }

    // =========================
    // TRAVERSALS
    // =========================
    public List<Student> inOrderList() {
        List<Student> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node node, List<Student> list) {
        if (node == null) return;
        inOrderRec(node.left, list);
        list.add(node.data);
        inOrderRec(node.right, list);
    }

    public List<Student> preOrderList() {
        List<Student> list = new ArrayList<>();
        preOrderRec(root, list);
        return list;
    }


    private void preOrderRec(Node node, List<Student> list) {
        if (node == null) return;
        list.add(node.data);
        preOrderRec(node.left, list);
        preOrderRec(node.right, list);
    }

    public List<Student> postOrderList() {
        List<Student> list = new ArrayList<>();
        postOrderRec(root, list);
        return list;
    }

    private void postOrderRec(Node node, List<Student> list) {
        if (node == null) return;
        postOrderRec(node.left, list);
        postOrderRec(node.right, list);
        list.add(node.data);
    }

    // BONUS: Level order traversal (helps viva)
    public List<Student> levelOrderList() {
        List<Student> list = new ArrayList<>();
        if (root == null) return list;

        Queue<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node n = q.remove();
            list.add(n.data);
            if (n.left != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        return list;
    }

    // =========================
    // UTILITIES
    // =========================
    public int countNodes() {
        return countRec(root);
    }

    private int countRec(Node node) {
        if (node == null) return 0;
        return 1 + countRec(node.left) + countRec(node.right);
    }

    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node node) {
        if (node == null) return -1; // height of empty tree
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }

    public Student minRecord() {
        Node m = minNode(root);
        return (m == null) ? null : m.data;
    }

    public Student maxRecord() {
        Node m = maxNode(root);
        return (m == null) ? null : m.data;
    }

    private Node minNode(Node node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maxNode(Node node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // =========================
    // ASCII BST DIAGRAM PRINT
    // =========================
    public void printTreeDiagram() {
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }
        printRec(root, "", true);
    }

    private void printRec(Node node, String prefix, boolean isTail) {
        if (node == null) return;
        System.out.println(prefix + (isTail ? "+-- " : "|-- ") + node.data.matric);
        List<Node> children = new ArrayList<>();
        if (node.right != null) children.add(node.right); // show right first (nice layout)
        if (node.left != null) children.add(node.left);

        for (int i = 0; i < children.size(); i++) {
            boolean last = (i == children.size() - 1);
            printRec(children.get(i), prefix + (isTail ? "    " : "|   "), last);
        }
    }
}
