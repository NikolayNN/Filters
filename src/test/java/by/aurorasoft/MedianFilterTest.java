package by.aurorasoft;

import by.aurorasoft.filter.MedianFilter;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

public final class MedianFilterTest {

    @Test
    public void listShouldBeFiltered01() {
        final MedianFilter<Value> filter = new MedianFilter<>(5);
        final List<Value> actual = filter.filter(Value.of(0, 50, 100, 100, 150, 100));
        final List<Value> expected = List.of(Value.of(0, 0), Value.of(1, 50), Value.of(2,100),  Value.of(3,100), Value.of(5, 100), Value.of(5,100));
        assertEquals(actual, expected);
    }


    @Test
    public void listShouldBeFiltered0() {
        final MedianFilter<Integer> filter = new MedianFilter<>(5);
        final List<Integer> actual = filter.filter(List.of(0, 50, 100, 100, 150, 100));
        final List<Integer> expected = List.of(0, 50, 100, 100, 100, 100);
        assertEquals(actual, expected);
    }

    @Test
    public void listShouldBeFiltered() {
        final MedianFilter<Integer> filter = new MedianFilter<>(5);
        final List<Integer> actual = filter.filter(List.of(2, 80, 6, 3));
        final List<Integer> expected = List.of(2, 80, 6, 3);
        assertEquals(actual, expected);
    }

    @Test
    public void dataShouldBeFilteredByMedianFilterWithEvenSizeOfWindow() {
        final MedianFilter<Integer> filter = new MedianFilter<>(6);
        final List<Integer> actual = filter.filter(List.of(2, 80, 6, 3));
        final List<Integer> expected = List.of(2, 80, 6, 3);
        assertEquals(actual, expected);
    }

    @Test
    public void listWithOneDigitShouldBeFiltered() {
        final MedianFilter<Integer> filter = new MedianFilter<>();
        final List<Integer> actual = filter.filter(List.of(2));
        final List<Integer> expected = List.of(2);
        assertEquals(actual, expected);
    }

    @Test
    public void emptyListShouldBeTransformed() {
        final MedianFilter<Integer> filter = new MedianFilter<>();
        final List<Integer> actual = filter.filter(List.of());
        final List<Integer> expected = List.of();
        assertEquals(actual, expected);
    }

    @Test
    public void filteredDataShouldBeTheSameBecauseOfWindowSizeGreaterOrEqualThanAmountOfFilteredData() {
        final List<Integer> given = List.of(2, 80, 6, 3);
        final MedianFilter<Integer> filter = new MedianFilter<>(given.size() + 1);
        final List<Integer> actual = filter.filter(given);
        final List<Integer> expected = List.of(2, 80, 6, 3);
        assertEquals(actual, expected);
    }

    public static class Value implements Comparable<Value> {
        private final int id;
        private final int value;

        @Override
        public int compareTo(Value o) {
            return value - o.value;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "id=" + id +
                    ", value=" + value +
                    '}';
        }

        public Value(int id, int value) {
            this.id = id;
            this.value = value;
        }

        static Value of(int id, int value){
            return new Value(id, value);
        }

        static List<Value> of(int... values) {
            return IntStream.range(0, values.length)
                    .mapToObj(i -> of(i, values[i]))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value1 = (Value) o;
            return id == value1.id && value == value1.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, value);
        }
    }
}
