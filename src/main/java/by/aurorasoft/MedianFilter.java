package by.aurorasoft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class MedianFilter<TypeOfData extends Comparable<TypeOfData>>
        implements Filter<TypeOfData>
{
    private final int sizeOfWindow;

    public MedianFilter()
    {
        super();
        this.sizeOfWindow = MedianFilter.VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW;
    }

    private static final int VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW = 3;

    public MedianFilter(final int sizeOfWindow)
    {
        super();
        this.sizeOfWindow = sizeOfWindow;
    }

    @Override
    public final List<TypeOfData> filter(final List<TypeOfData> filteredObjects)
    {
        if(filteredObjects.isEmpty())
        {
            return new ArrayList<TypeOfData>();
        }
        final List<TypeOfData> filteredObjectsWithDuplicatedBorders
                = this.duplicateBorders(filteredObjects);
        final List<MedianFilter.Window<TypeOfData>> windows = this.findWindows(
                filteredObjectsWithDuplicatedBorders);
        return windows.stream().map(MedianFilter.Window::filter).collect(Collectors.toList());
    }

    private List<TypeOfData> duplicateBorders(final List<TypeOfData> filteredObjects)
    {
        final TypeOfData firstFilteredObject = filteredObjects.get(0);
        final TypeOfData lastFilteredObject = filteredObjects.get(filteredObjects.size() - 1);

        final List<TypeOfData> filteredObjectsWithDoubled = new ArrayList<TypeOfData>();
        filteredObjectsWithDoubled.add(firstFilteredObject);
        filteredObjectsWithDoubled.addAll(filteredObjects);
        filteredObjectsWithDoubled.add(lastFilteredObject);

        return filteredObjectsWithDoubled;
    }

    private List<MedianFilter.Window<TypeOfData>> findWindows(final List<TypeOfData> filteredObject)
    {
        final List<MedianFilter.Window<TypeOfData>> windows
                = new ArrayList<MedianFilter.Window<TypeOfData>>();

        int runnerIndexOfStartWindow = 0;
        int runnerIndexOfEndWindow = this.sizeOfWindow - 1;
        List<TypeOfData> contentOfCurrentWindow;
        MedianFilter.Window<TypeOfData> currentWindow;
        while(runnerIndexOfEndWindow < filteredObject.size())
        {
            contentOfCurrentWindow = filteredObject.subList(
                    runnerIndexOfStartWindow, runnerIndexOfEndWindow + 1);
            currentWindow = new MedianFilter.Window<TypeOfData>(contentOfCurrentWindow);
            windows.add(currentWindow);
            runnerIndexOfStartWindow++;
            runnerIndexOfEndWindow++;
        }

        return windows;
    }

    private static class Window<TypeOfData extends Comparable<? super TypeOfData>>
            implements Filter.Filterable<TypeOfData>
    {
        private final List<TypeOfData> content;

        public Window(final List<TypeOfData> content)
        {
            super();
            this.content = new ArrayList<TypeOfData>(content);
            this.content.sort(Comparator.naturalOrder());
        }

        @Override
        public final TypeOfData filter()
        {
            final int indexOfResult = this.content.size() / 2;
            return this.content.get(indexOfResult);
        }
    }
}
