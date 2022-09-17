package by.aurorasoft.filter.median;

import java.util.Comparator;
import java.util.List;

public class Window<T extends Comparable<T>> {

    private final List<T> content;

    public Window(List<T> content) {
        this.content = content;
    }

    public T filteredValue() {
        this.content.sort(Comparator.naturalOrder());
        return this.content.get(this.content.size() / 2);
    }
}
