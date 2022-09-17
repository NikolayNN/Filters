package by.aurorasoft.filter.median;

import by.aurorasoft.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return findWindows(addDuplicatesOnBorders(values))
                .stream()
                .map(Window::filteredValue)
                .collect(Collectors.toList());
    }

    private List<Window<T>> findWindows(List<T> values) {
        List<Window<T>> result = new ArrayList<>();
        int startIndex = 0;
        int endIndex = this.windowSize - 1;
        while (endIndex < values.size()) {
            result.add(new Window<>(values.subList(startIndex++, ++endIndex)));
        }
        return result;
    }

    private List<T> addDuplicatesOnBorders(List<T> values) {
        int duplicatesAmount = this.windowSize / 2;
        values = addToStart(values, values.get(0), duplicatesAmount);
        return addToEnd(values, values.get(values.size() - 1), duplicatesAmount);
    }
}
