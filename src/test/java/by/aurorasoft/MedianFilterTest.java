package by.aurorasoft;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public final class MedianFilterTest
{
    private final MedianFilter<Integer> medianFilter;

    public MedianFilterTest()
    {
        super();
        this.medianFilter = new MedianFilter<Integer>();
    }

    @Test
    public final void listShouldBeFiltered()
    {
        final List<Integer> filteredData = List.of(2, 80, 6, 3);
        final List<Integer> actualResult = this.medianFilter.filter(filteredData);
        final List<Integer> expectedResult = List.of(2, 6, 6, 3);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public final void listWithOneDigitShouldBeFiltered()
    {
        final List<Integer> filteredData = List.of(2);
        final List<Integer> actualResult = this.medianFilter.filter(filteredData);
        final List<Integer> expectedResult = List.of(2);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public final void emptyListShouldNotBeTransformed()
    {
        final List<Integer> filteredData = List.of();
        this.medianFilter.filter(filteredData);
    }
}
