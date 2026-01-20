import java.util.*;

public class Sorting {

    // =========================
    // MERGE SORT by Name (A-Z)
    // =========================
    public static void mergeSortByName(List<Student> arr) {
        if (arr == null || arr.size() <= 1) return;
        List<Student> temp = new ArrayList<>(Collections.nCopies(arr.size(), (Student)null));
        mergeSortRec(arr, temp, 0, arr.size() - 1);
    }

    private static void mergeSortRec(List<Student> arr, List<Student> temp, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortRec(arr, temp, left, mid);
        mergeSortRec(arr, temp, mid + 1, right);
        merge(arr, temp, left, mid, right);
    }

    private static void merge(List<Student> arr, List<Student> temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;

        while (i <= mid && j <= right) {
            if (arr.get(i).name.compareToIgnoreCase(arr.get(j).name) <= 0) {
                temp.set(k++, arr.get(i++));
            } else {
                temp.set(k++, arr.get(j++));
            }
        }
        while (i <= mid) temp.set(k++, arr.get(i++));
        while (j <= right) temp.set(k++, arr.get(j++));

        for (int x = left; x <= right; x++) {
            arr.set(x, temp.get(x));
        }
    }

    // =========================
    // QUICK SORT by CGPA
    // (ascending or descending)
    // =========================
    public static void quickSortByCgpa(List<Student> arr, boolean ascending) {
        if (arr == null || arr.size() <= 1) return;
        quickSortRec(arr, 0, arr.size() - 1, ascending);
    }

    private static void quickSortRec(List<Student> arr, int low, int high, boolean ascending) {
        if (low >= high) return;
        int p = partition(arr, low, high, ascending);
        quickSortRec(arr, low, p - 1, ascending);
        quickSortRec(arr, p + 1, high, ascending);
    }

    private static int partition(List<Student> arr, int low, int high, boolean ascending) {
        Student pivot = arr.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition;
            if (ascending) {
                condition = arr.get(j).cgpa <= pivot.cgpa;
            } else {
                condition = arr.get(j).cgpa >= pivot.cgpa;
            }

            if (condition) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Student> arr, int i, int j) {
        Student tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }
}
