package by.aurorasoft;

import au.com.bytecode.opencsv.CSVWriter;
import by.aurorasoft.filter.MedianFilter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Test
public final class MedianFilterITTest {

    private List<Double> filteredData;

    @BeforeClass
    public void readFilteredData()
            throws IOException
    {
        final Path path = Path.of(FILE_PATH_TO_READ_DATA);
        final byte[] readBytes = Files.readAllBytes(path);
        final String readText = new String(readBytes, StandardCharsets.UTF_8);

        final String[] splittedReadLines = readText.split(NEW_LINE_CHAR_SEQUENCE);
        final List<String> readLines = Arrays.asList(splittedReadLines);

        this.filteredData = readLines.stream().filter((final String researchString) ->
            !researchString.contains(SYMBOLS_OF_COMMENT_LINE))
                .map((final String line) ->
        {
            final String[] componentsOfLine = line.split(LINE_COMPONENTS_SEPARATOR_REGEX);
            String descriptionOfFilteredData = componentsOfLine[INDEX_OF_FILTERED_DATA_IN_COMPONENTS_OF_LINE];
            descriptionOfFilteredData = descriptionOfFilteredData.replace(
                    POINT_ANALOG_IN_DESCRIPTION_OF_FILTERED_DATA, POINT);
            return Double.parseDouble(descriptionOfFilteredData);
        }).collect(Collectors.toList());
    }

    private static final String FILE_PATH_TO_READ_DATA = "./src/test/resources/data.txt";
    private static final String NEW_LINE_CHAR_SEQUENCE = "\r\n";
    private static final String SYMBOLS_OF_COMMENT_LINE = "###";
    private static final String LINE_COMPONENTS_SEPARATOR_REGEX = "( |\t)+";
    private static final int INDEX_OF_FILTERED_DATA_IN_COMPONENTS_OF_LINE = 3;
    private static final String POINT_ANALOG_IN_DESCRIPTION_OF_FILTERED_DATA = ",";
    private static final String POINT = ".";

    @Test
    public void filteredDataShouldBeFilteredAndResultShouldWrittenInFile()
            throws IOException
    {
        final int medialFileWindowSize = 50;
        final MedianFilter<Double> medianFilter = new MedianFilter<>(medialFileWindowSize);
        final List<Double> filterResult = medianFilter.filter(this.filteredData);

        /*final List<Pair<Double, Double>> pairsOfBeforeAndAfterFilteringData = new ArrayList<>();
        double currentBeforeFilteringData;
        double currentAfterFilteringData;
        Pair<Double, Double> currentPairOfBeforeAndAfterFilteringData;
        for(int i = 0; i < this.filteredData.size(); i++)
        {
            currentBeforeFilteringData = this.filteredData.get(i);
            currentAfterFilteringData = filterResult.get(i);
            currentPairOfBeforeAndAfterFilteringData = new Pair<>(currentBeforeFilteringData,
                    currentAfterFilteringData);
            pairsOfBeforeAndAfterFilteringData.add(currentPairOfBeforeAndAfterFilteringData);
        }*/

        final CSVWriter csvWriter = new CSVWriter(new FileWriter(FILE_PATH_TO_WRITE_DATA));
        /*pairsOfBeforeAndAfterFilteringData.forEach(
                (final Pair<Double, Double> pairOfBeforeAndAfterFilteringData) -> {
                    final String[] nextToWriteOfCSVWriter = new String[]{
                            Double.toString(pairOfBeforeAndAfterFilteringData.first()),
                            Double.toString(pairOfBeforeAndAfterFilteringData.second())
                            };
                    csvWriter.writeNext(nextToWriteOfCSVWriter);
                });*/
        int runnerIndex = 0;
        while(runnerIndex < filterResult.size())
        {
            csvWriter.writeNext(new String[]{
                    Double.toString(this.filteredData.get(runnerIndex)),
                    Double.toString(filterResult.get(runnerIndex))});
            runnerIndex++;
        }

    }

    private static final String FILE_PATH_TO_WRITE_DATA = "./src/test/resources/data.csv";
}
