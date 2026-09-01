package Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class StringQuickSort {
    private String names[];
    private int length;

    public StringQuickSort(String[] toSort) {
        this.names = toSort;
        length = toSort.length;
        sort(names);
    }

    public StringQuickSort(ArrayList<String> toSort) {
        this.names = toSort.toArray(new String[0]);
        length = toSort.size();
        sort(names);
    }

    public String[] getArray() {
        return names;
    }

    public ArrayList<String> getArrayList() {
        return new ArrayList<>(Arrays.asList(names));
    }

    void sort(String array[]) {
        if (array == null || array.length == 0) {
            return;
        }
        this.names = array;
        this.length = array.length;
        quickSort(0, length - 1);
    }

    void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        String pivot = this.names[lowerIndex + (higherIndex - lowerIndex) / 2];

        while (i <= j) {
            while (this.names[i].compareToIgnoreCase(pivot) < 0) {
                i++;
            }

            while (this.names[j].compareToIgnoreCase(pivot) > 0) {
                j--;
            }

            if (i <= j) {
                exchangeNames(i, j);
                i++;
                j--;
            }
        }
        //call quickSort recursively
        if (lowerIndex < j) {
            quickSort(lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSort(i, higherIndex);
        }
    }

    void exchangeNames(int i, int j) {
        String temp = this.names[i];
        this.names[i] = this.names[j];
        this.names[j] = temp;
    }
}
