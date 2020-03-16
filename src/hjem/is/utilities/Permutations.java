package hjem.is.utilities;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static <T> List<List<T>> findPermutations(List<T> nums) {
        List<List<T>> results = new ArrayList<>();
        if (nums == null || nums.size() == 0) {
            return results;
        }
        List<T> result = new ArrayList<>();
        dfs(nums, results, result);
        return results;
    }

    public static <T> void dfs(List<T> nums, List<List<T>> results, List<T> result) {
        if (nums.size() == result.size()) {
            List<T> temp = new ArrayList<>(result);
            results.add(temp);
        }
        for (int i = 0; i < nums.size(); i++) {
            if (!result.contains(nums.get(i))) {
                result.add(nums.get(i));
                Permutations.dfs(nums, results, result);
                result.remove(result.size() - 1);
            }
        }
    }
}
