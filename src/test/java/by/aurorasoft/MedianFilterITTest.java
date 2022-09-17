package by.aurorasoft;

import au.com.bytecode.opencsv.CSVWriter;
import by.aurorasoft.filter.median.MedianFilter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Test
public final class MedianFilterITTest {

    private static final String FILE_PATH_TO_READ_DATA = "./src/test/resources/t2.txt";
    private static final String NEW_LINE_CHAR_SEQUENCE = "\r\n";
    private static final String SYMBOLS_OF_COMMENT_LINE = "###";
    private static final String LINE_COMPONENTS_SEPARATOR_REGEX = "( |\t)+";
    private static final String DELIMITER_OF_DATE_AND_TIME = " ";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final int INDEX_OF_FILTERED_DATA_IN_COMPONENTS_OF_LINE = 3;
    private static final String POINT_ANALOG_IN_DESCRIPTION_OF_FILTERED_DATA = ",";
    private static final String POINT = ".";


    private List<TwoDimensionalTuple<LocalDateTime, Double>> dateTimeAndFilteredData;

    @BeforeClass
    public void readFilteredData() throws IOException {
        final Path path = Path.of(FILE_PATH_TO_READ_DATA);
        final byte[] readBytes = Files.readAllBytes(path);
        final String readText = new String(readBytes, StandardCharsets.UTF_8);

        final String[] splittedReadLines = readText.split(NEW_LINE_CHAR_SEQUENCE);
        final List<String> readLines = Arrays.asList(splittedReadLines);

        this.dateTimeAndFilteredData = readLines.stream().filter((final String researchString) ->
                        !researchString.contains(SYMBOLS_OF_COMMENT_LINE))
                .map((final String line) -> {
                    final String[] componentsOfLine = line.split(LINE_COMPONENTS_SEPARATOR_REGEX);

                    final String descriptionOfDateTime = componentsOfLine[0] + DELIMITER_OF_DATE_AND_TIME
                            + componentsOfLine[1];
                    final LocalDateTime dateTime = LocalDateTime.parse(descriptionOfDateTime,
                            DATE_TIME_FORMATTER);

                    String descriptionOfFilteredData = componentsOfLine[INDEX_OF_FILTERED_DATA_IN_COMPONENTS_OF_LINE];
                    descriptionOfFilteredData = descriptionOfFilteredData.replace(
                            POINT_ANALOG_IN_DESCRIPTION_OF_FILTERED_DATA, POINT);
                    final double filteredData = Double.parseDouble(descriptionOfFilteredData);

                    return new TwoDimensionalTuple<>(dateTime, filteredData);
                }).collect(Collectors.toList());
    }

    @Test
    public void filteredDataShouldBeFilteredAndResultShouldWrittenInFile() throws IOException {
        final List<Double> filteredData = new ArrayList<>();
        this.dateTimeAndFilteredData.forEach(tuple -> filteredData.add(tuple.getSecond()));

        final int medianFilterWindowSize = 49;
        final MedianFilter<Double> medianFilter = new MedianFilter<>(medianFilterWindowSize);
        final List<Double> filterResult = medianFilter.filter(filteredData);

        final List<ThreeDimensionalTuple<LocalDateTime, Double, Double>> timesAndBeforeAndAfterFilteringData
                = new ArrayList<>();

        IntStream.range(0, dateTimeAndFilteredData.size()).forEach(rangeIndex -> {
            final ThreeDimensionalTuple<LocalDateTime, Double, Double> timeAndBeforeAndAfterFilteringData
                    = new ThreeDimensionalTuple<>(this.dateTimeAndFilteredData.get(rangeIndex),
                    filterResult.get(rangeIndex));
            timesAndBeforeAndAfterFilteringData.add(timeAndBeforeAndAfterFilteringData);
        });

        final CSVWriter csvWriter = new CSVWriter(new FileWriter(FILE_PATH_TO_WRITE_DATA));
        timesAndBeforeAndAfterFilteringData.forEach(
                (final ThreeDimensionalTuple<LocalDateTime, Double, Double> timeAndBeforeAndAfterFilteringData) -> {
                    final String[] nextToWriteOfCSVWriter = new String[]{
                            timeAndBeforeAndAfterFilteringData.getFirst().format(DATE_TIME_FORMATTER),
                            Double.toString(timeAndBeforeAndAfterFilteringData.getSecond()),
                            Double.toString(timeAndBeforeAndAfterFilteringData.getThird())
                    };
                    csvWriter.writeNext(nextToWriteOfCSVWriter);
                });
    }

    private static final String FILE_PATH_TO_WRITE_DATA = "./src/test/resources/data.csv";

    private static class TwoDimensionalTuple<TypeOfFirst, TypeOfSecond> {
        private final TypeOfFirst first;
        private final TypeOfSecond second;

        public TwoDimensionalTuple(final TypeOfFirst first, final TypeOfSecond second) {
            this.first = first;
            this.second = second;
        }

        public final TypeOfFirst getFirst() {
            return this.first;
        }

        public final TypeOfSecond getSecond() {
            return this.second;
        }
    }

    private static final class ThreeDimensionalTuple<TypeOfFirst, TypeOfSecond, TypeOfThird>
            extends TwoDimensionalTuple<TypeOfFirst, TypeOfSecond> {
        private final TypeOfThird third;

        public ThreeDimensionalTuple(final TwoDimensionalTuple<TypeOfFirst, TypeOfSecond> tuple,
                                     final TypeOfThird third) {
            super(tuple.getFirst(), tuple.getSecond());
            this.third = third;
        }

        public TypeOfThird getThird() {
            return this.third;
        }
    }
}
