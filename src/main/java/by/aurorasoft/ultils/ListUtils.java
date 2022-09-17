package by.aurorasoft.ultils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ListUtils {

    public static <T> List<T> addToStart(List<T> values, T duplicatedValue, int duplicatesAmount) {
        List<T> result = new ArrayList<>(values.size() + duplicatesAmount);
        IntStream.range(0, duplicatesAmount)
                .forEach(i -> result.add(duplicatedValue));
        result.addAll(values);
        return result;
    }

    public static <T> List<T> addToEnd(List<T> values, T duplicatedValue, int duplicatesAmount) {
        IntStream.range(0, duplicatesAmount)
                .forEach(i -> values.add(duplicatedValue));
        return values;
    }
}
