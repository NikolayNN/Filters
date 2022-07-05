package by.aurorasoft.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MedianFilter<TypeOfData extends Comparable<? super TypeOfData>>
        implements Filter<TypeOfData> {
    private final int sizeOfWindow;

    public MedianFilter() {
        this.sizeOfWindow = VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW;
    }

    private static final int VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW = 3;

    public MedianFilter(final int sizeOfWindow) {
        this.sizeOfWindow = sizeOfWindow % 2 != 0 ? sizeOfWindow : sizeOfWindow - 1;
    }

    @Override
    public List<TypeOfData> filter(final List<TypeOfData> filteredObjects) {
        if (filteredObjects.isEmpty()) {
            return Collections.emptyList();
        }
        if (this.sizeOfWindow >= filteredObjects.size()) {
            return filteredObjects;
        }

        final int amountOfDuplicatesOfEachBorder = this.sizeOfWindow / 2;
        final List<TypeOfData> filteredObjectsWithDuplicatedBorders
                = this.duplicateBorders(filteredObjects, amountOfDuplicatesOfEachBorder);
        final List<MedianFilter.Window<TypeOfData>> windows = this.findWindows(
                filteredObjectsWithDuplicatedBorders);
        return windows.stream().map(MedianFilter.Window::filter).collect(Collectors.toList());
    }

    private List<TypeOfData> duplicateBorders(final List<TypeOfData> filteredObjects,
                                              final int amountOfDuplicatesOfEachBorder) {
        final TypeOfData firstFilteredObject = filteredObjects.get(0);
        final TypeOfData lastFilteredObject = filteredObjects.get(filteredObjects.size() - 1);

        final List<TypeOfData> filteredObjectsWithDuplicatedBorders = new ArrayList<>();

        IntStream.range(0, amountOfDuplicatesOfEachBorder).forEach(
                rangeIndex -> filteredObjectsWithDuplicatedBorders.add(firstFilteredObject));
        filteredObjectsWithDuplicatedBorders.addAll(filteredObjects);
        IntStream.range(0, amountOfDuplicatesOfEachBorder).forEach(
                rangeIndex -> filteredObjectsWithDuplicatedBorders.add(lastFilteredObject));

        return filteredObjectsWithDuplicatedBorders;
    }

    private List<MedianFilter.Window<TypeOfData>> findWindows(final List<TypeOfData> filteredObject) {
        final List<MedianFilter.Window<TypeOfData>> resultWindows = new ArrayList<>();

        int runnerIndexOfStartWindow = 0;
        int runnerIndexOfEndWindow = this.sizeOfWindow - 1;
        List<TypeOfData> contentOfCurrentWindow;
        MedianFilter.Window<TypeOfData> currentWindow;
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

    private static class Window<TypeOfData extends Comparable<? super TypeOfData>>
            implements Filter.Filterable<TypeOfData> {
        private final List<TypeOfData> content;

        public Window(final List<TypeOfData> content) {
            this.content = new ArrayList<>(content);
            this.content.sort(Comparator.naturalOrder());
        }

        @Override
        public final TypeOfData filter() {
            final int indexOfResult = this.content.size() / 2;
            return this.content.get(indexOfResult);
        }
    }
}
