package by.aurorasoft.filter.median;

import by.aurorasoft.filter.Filter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static by.aurorasoft.ultils.ListUtils.addToEnd;
import static by.aurorasoft.ultils.ListUtils.addToStart;

public class MedianFilter<T extends Comparable<T>> implements Filter<T> {

    private static final int DEFAULT_WINDOW_SIZE = 3;
    private final int windowSize;

    public MedianFilter() {
        this.windowSize = DEFAULT_WINDOW_SIZE;
    }

    public MedianFilter(int windowSize) {
        this.windowSize = windowSize % 2 != 0 ? windowSize : windowSize - 1;
    }

    @Override
    public List<T> filter(List<T> values) {
        if (values.isEmpty() || windowSize >= values.size()) {
            return values;
        }
        return doFilter(addDuplicatesOnBorders(values));
    }

    private List<T> doFilter(List<T> values) {
        List<T> result = new ArrayList<>();
        WindowIterator<T> iterator = new WindowIterator<>(values, windowSize);
        while (iterator.hasNext()) {
            result.add(getFilteredValue(iterator.next()));
        }
        return result;
    }

    private T getFilteredValue(List<T> window) {
        window.sort(Comparator.naturalOrder());
        return window.get(window.size() / 2);
    }

    private List<T> addDuplicatesOnBorders(List<T> values) {
        int duplicatesAmount = this.windowSize / 2;
        values = addToStart(values, values.get(0), duplicatesAmount);
        return addToEnd(values, values.get(values.size() - 1), duplicatesAmount);
    }
}
