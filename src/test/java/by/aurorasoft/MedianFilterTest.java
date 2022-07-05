package by.aurorasoft;

import by.aurorasoft.filter.MedianFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public final class MedianFilterTest {
    @Test
    public void listShouldBeFiltered() {
        final int medianFilterSizeOfWindow = 5;
        final MedianFilter<Integer> medianFilter = new MedianFilter<>(medianFilterSizeOfWindow);
        final List<Integer> filteredData = List.of(2, 80, 6, 3);
        final List<Integer> actualResult = medianFilter.filter(filteredData);
        final List<Integer> expectedResult = List.of(2, 80, 6, 3);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void dataShouldBeFilteredByMedianFilterWithEvenSizeOfWindow()
    {
        final int medianFilterSizeOfWindow = 6;
        final MedianFilter<Integer> medianFilter = new MedianFilter<>(medianFilterSizeOfWindow);
        final List<Integer> filteredData = List.of(2, 80, 6, 3);
        final List<Integer> actualResult = medianFilter.filter(filteredData);
        final List<Integer> expectedResult = List.of(2, 80, 6, 3);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void listWithOneDigitShouldBeFiltered() {
        final MedianFilter<Integer> medianFilter = new MedianFilter<>();
        final List<Integer> filteredData = List.of(2);
        final List<Integer> actualResult = medianFilter.filter(filteredData);
        final List<Integer> expectedResult = List.of(2);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void emptyListShouldBeTransformed() {
        final MedianFilter<Integer> medianFilter = new MedianFilter<>();
        final List<Integer> filteredData = List.of();
        final List<Integer> actualFilteredData = medianFilter.filter(filteredData);
        final List<Integer> expectedFilteredData = List.of();
        Assert.assertEquals(actualFilteredData, expectedFilteredData);
    }

    @Test
    public void filteredDataShouldBeTheSameBecauseOfWindowSizeGreaterOrEqualThanAmountOfFilteredData() {
        final List<Integer> filteredData = List.of(2, 80, 6, 3);
        final int windowSize = filteredData.size() + 1;
        final MedianFilter<Integer> medianFilter = new MedianFilter<>(windowSize);
        final List<Integer> actualResultData = medianFilter.filter(filteredData);
        final List<Integer> expectedResultData = List.of(2, 80, 6, 3);
        Assert.assertEquals(actualResultData, expectedResultData);
    }
}
