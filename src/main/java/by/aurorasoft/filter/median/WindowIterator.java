package by.aurorasoft.filter.median;

import java.util.ArrayList;
import java.util.List;

class WindowIterator<T> {

    int startIndex;
    int endIndex;
    final List<T> values;

    public WindowIterator(List<T> values, int windowSize) {
        startIndex = 0;
        endIndex = windowSize - 1;
        this.values = values;
    }

    boolean hasNext() {
        return endIndex < values.size();
    }

    List<T> next() {
        return new ArrayList<>(values.subList(startIndex++, ++endIndex));
    }
}
