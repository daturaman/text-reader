package com.mcarter.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;

import com.mcarter.tools.TextFileReader;

/**
 * Acceptance tests for {@link TextFileReader}. The tests can also be run against the
 * Perfidix/Perclipse benchmarking library.
 * 
 * @author Michael Carter
 */
@BenchClass(runs = 50)
public class TextFileReaderTest {
	private TextFileReader textFileReader;

	/**
	 * Initialise the {@code TextFileReader} under test with the default text file.
	 * 
	 * @throws URISyntaxException if the URL of the testDocument cannot be converted to a URI.
	 */
	@Before
	@BeforeEachRun
	public void setUp() throws URISyntaxException {
		textFileReader = new TextFileReader(testDocument("defaultTestFile.txt"));
	}

	@SkipBench
	@Test(expected = IllegalArgumentException.class)
	public void readerIsLoadedWithInvalidTextFile() {
		textFileReader = new TextFileReader(new File(StringUtils.EMPTY));
	}

	@Test
	public void countsAllLinesInPlainTextFile() throws IOException {
		final int expectedLineCount = 21;
		assertThat(textFileReader.getLineCount(false), is(equalTo(expectedLineCount)));
	}

	@Test
	public void countsNonBlankLinesInPlainTextFile() throws IOException {
		final int expectedLineCount = 18;
		assertThat(textFileReader.getLineCount(true), is(equalTo(expectedLineCount)));
	}

	@Test
	public void countsLinesInBlankFile() throws IOException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("blankFile.txt"));
		final int expectedLineCount = 0;
		assertThat(textFileReader.getLineCount(false), is(equalTo(expectedLineCount)));
	}

	@Test
	public void countsWhitespaceDelimitedWordsInPlainTextFile() throws IOException {
		final int expectedWordCount = 477;
		assertThat(textFileReader.getWordCount(), is(equalTo(expectedWordCount)));
	}

	@Test
	public void countsWordsInBlankFile() throws IOException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("blankFile.txt"));
		final int expectedWordCount = 0;
		assertThat(textFileReader.getWordCount(), is(equalTo(expectedWordCount)));
	}

	@Test
	public void calculatesAverageWordLength() throws FileNotFoundException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("averageWordLength.txt"));
		final double expectedAverage = 6.9;
		assertThat(textFileReader.getAverageWordLength(), is(equalTo(expectedAverage)));
	}

	@Test
	public void calculatesAverageWordLengthInBlankFile() throws FileNotFoundException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("blankFile.txt"));
		final double expectedAverage = 0.0;
		assertThat(textFileReader.getAverageWordLength(), is(equalTo(expectedAverage)));
	}

	@Test
	public void findsMostCommonLetter() throws FileNotFoundException, IOException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("mostUsedLetter.txt"));
		final char expectedLetter = 'b';
		assertThat(textFileReader.getMostCommonLetter(), is(equalTo(expectedLetter)));
	}

	@Test
	public void attemptsToFindMostCommonLetterInBlankFile()
			throws FileNotFoundException, IOException, URISyntaxException {
		textFileReader = new TextFileReader(testDocument("blankFile.txt"));
		final char expectedLetter = Character.UNASSIGNED;
		assertThat(textFileReader.getMostCommonLetter(), is(equalTo(expectedLetter)));
	}

	/**
	 * Creates a {@code File} instance using the passed in filename.
	 * 
	 * @param filename name of the file to reference.
	 * @return A {@codeFile} instance representing the entity specified by file.
	 * @throws URISyntaxException an error was encountered converting the URL of the resource to a
	 *             URI.
	 */
	private File testDocument(final String filename) throws URISyntaxException {
		return new File(this.getClass().getClassLoader().getResource(filename).toURI());
	}
}
