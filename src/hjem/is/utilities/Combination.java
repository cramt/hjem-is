package hjem.is.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Combination {
    public static <T> List<List<T>> combination(List<T> values, int size) {

        if (0 == size) {
            return Collections.singletonList(Collections.<T>emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<T>> combination = new LinkedList<List<T>>();

        T actual = values.iterator().next();

        List<T> subSet = new LinkedList<T>(values);
        subSet.remove(actual);

        List<List<T>> subSetCombination = combination(subSet, size - 1);

        for (List<T> set : subSetCombination) {
            List<T> newSet = new LinkedList<T>(set);
            newSet.add(0, actual);
            combination.add(newSet);
        }

        combination.addAll(combination(subSet, size));

        return combination;
    }

    private static interface RepeatedRecursive {
        void func(int post, int start, RepeatedRecursive f);
    }

    public static <T> List<List<T>> repeated(List<T> arr, int length) {
        Object[] data = new Object[length];
        ArrayList<List<T>> res = new ArrayList<>();
        RepeatedRecursive finalFunc = (int pos, int start, RepeatedRecursive f) -> {
            if (pos == length) {
                res.add((List<T>) List.of(data.clone()));
                return;
            }
            for (int i = start; i < arr.size(); ++i) {
                data[pos] = arr.get(i);
                f.func(pos + 1, i, f);
            }
        };
        finalFunc.func(0, 0, finalFunc);
        return res;
    }
}
