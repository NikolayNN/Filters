package by.aurorasoft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MedianFilter<TypeOfData extends Comparable<TypeOfData>>
        implements Filter<TypeOfData>
{
    private final int sizeOfWindow;

    public MedianFilter()
    {
        this.sizeOfWindow = MedianFilter.VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW;
    }

    private static final int VALUE_OF_NOT_DEFINED_SIZE_OF_WINDOW = 3;

    public MedianFilter(final int sizeOfWindow)
    {
        this.sizeOfWindow = sizeOfWindow;
    }

    @Override
    public final List<TypeOfData> filter(final List<TypeOfData> filteredObjects)
    {
        final TypeOfData firstFilteredObject = filteredObjects.get(0);
        final TypeOfData lastFilteredObject = filteredObjects.get(filteredObjects.size() - 1);

        final List<TypeOfData> filteredObjectsWithDoubled = new ArrayList<TypeOfData>();
        filteredObjectsWithDoubled.add(firstFilteredObject);
        filteredObjectsWithDoubled.addAll(filteredObjects);
        filteredObjectsWithDoubled.add(lastFilteredObject);

        final List<MedianFilter.Window<TypeOfData>> windows
                = new ArrayList<MedianFilter.Window<TypeOfData>>();

        int runnerIndexOfStartWindow = 0;
        int runnerIndexOfEndWindow = this.sizeOfWindow - 1;
        List<TypeOfData> contentOfCurrentWindow;
        MedianFilter.Window<TypeOfData> currentWindow;
        while(runnerIndexOfEndWindow < filteredObjectsWithDoubled.size())
        {
            contentOfCurrentWindow = filteredObjectsWithDoubled.subList(
                    runnerIndexOfStartWindow, runnerIndexOfEndWindow);
            currentWindow = new MedianFilter.Window<TypeOfData>(contentOfCurrentWindow);
            windows.add(currentWindow);
            runnerIndexOfStartWindow++;
            runnerIndexOfEndWindow++;
        }

        return windows.stream().map(MedianFilter.Window::filter).collect(Collectors.toList());
    }

    private static class Window<TypeOfData extends Comparable<? super TypeOfData>>
            implements Filterable<TypeOfData>
    {
        private final List<TypeOfData> content;

        public Window(final List<TypeOfData> content)
        {
            super();
            this.content = content;
            this.content.sort(Comparator.naturalOrder());
        }

        @Override
        public final TypeOfData filter()
        {
            this.content.sort(Comparator.naturalOrder());
            final int indexOfResult = this.content.size() / 2;
            return this.content.get(indexOfResult);
        }
    }
}
