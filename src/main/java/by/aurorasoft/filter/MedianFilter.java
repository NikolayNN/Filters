package by.aurorasoft.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MedianFilter<T extends Comparable<? super T>> implements Filter<T> {

    private static final int DEFAULT_WINDOW_SIZE = 3;
    private final int windowSize;

    public MedianFilter() {
        this.windowSize = DEFAULT_WINDOW_SIZE;
    }

    public MedianFilter(final int windowSize) {
        this.windowSize = windowSize % 2 != 0 ? windowSize : windowSize - 1;
    }

    @Override
    public List<T> filter(final List<T> values) {
        if (values.isEmpty()) {
            return Collections.emptyList();
        }
        if (this.windowSize >= values.size()) {
            return values;
        }

        final int amountOfDuplicatesOfEachBorder = this.windowSize / 2;
        final List<T> filteredObjectsWithDuplicatedBorders
                = this.duplicateBorders(values, amountOfDuplicatesOfEachBorder);
        final List<MedianFilter.Window<T>> windows = this.findWindows(
                filteredObjectsWithDuplicatedBorders);
        return windows.stream().map(MedianFilter.Window::filter).collect(Collectors.toList());
    }

    private List<T> duplicateBorders(List<T> filteredObjects, int amountOfDuplicatesOfEachBorder) {
        final T firstFilteredObject = filteredObjects.get(0);
        final T lastFilteredObject = filteredObjects.get(filteredObjects.size() - 1);

        final List<T> filteredObjectsWithDuplicatedBorders = new ArrayList<>();

        IntStream.range(0, amountOfDuplicatesOfEachBorder).forEach(
                rangeIndex -> filteredObjectsWithDuplicatedBorders.add(firstFilteredObject));
        filteredObjectsWithDuplicatedBorders.addAll(filteredObjects);
        IntStream.range(0, amountOfDuplicatesOfEachBorder).forEach(
                rangeIndex -> filteredObjectsWithDuplicatedBorders.add(lastFilteredObject));

        return filteredObjectsWithDuplicatedBorders;
    }

    private List<MedianFilter.Window<T>> findWindows(List<T> filteredObject) {
        final List<MedianFilter.Window<T>> resultWindows = new ArrayList<>();

        int runnerIndexOfStartWindow = 0;
        int runnerIndexOfEndWindow = this.windowSize - 1;
        List<T> contentOfCurrentWindow;
        MedianFilter.Window<T> currentWindow;
        while (runnerIndexOfEndWindow < filteredObject.size()) {
            contentOfCurrentWindow = filteredObject.subList(
                    runnerIndexOfStartWindow, runnerIndexOfEndWindow + 1);
            currentWindow = new MedianFilter.Window<>(contentOfCurrentWindow);
            resultWindows.add(currentWindow);
            runnerIndexOfStartWindow++;
            runnerIndexOfEndWindow++;
        }

        return resultWindows;
    }

    private static class Window<Type extends Comparable<? super Type>>
            implements Filter.Filterable<Type> {

        private final List<Type> content;

        public Window(List<Type> content) {
            this.content = new ArrayList<>(content);
            this.content.sort(Comparator.naturalOrder());
        }

        @Override
        public final Type filter() {
            final int indexOfResult = this.content.size() / 2;
            return this.content.get(indexOfResult);
        }
    }
}
