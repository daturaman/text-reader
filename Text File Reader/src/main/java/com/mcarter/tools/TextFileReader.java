package com.mcarter.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * The TextFileReader takes a reference to a plain text file and generates a variety of statistics.
 * 
 * @author Michael Carter
 *
 */
public class TextFileReader {

	private final File textFile;

	public static void main(String[] filePaths) {
		if (filePaths.length == 0) {
			throw new IllegalArgumentException("Please provided the path of at least one text file.");
		}
		for (String filePath : filePaths) {
			if (StringUtils.isNotBlank(filePath)) {
				TextFileReader reader = new TextFileReader(new File(filePath));
				try {
					System.out.println(String.format("\n=================\nStatistics for %s", filePath));
					System.out.println(String.format("Line count is: %s", reader.getLineCount(false)));
					System.out.println(String.format("Word count is: %s", reader.getWordCount()));
					System.out.println(String.format("Average word length is: %s", reader.getAverageWordLength()));
					System.out.println(String.format("Most common letter is: %s", reader.getMostCommonLetter()));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} else {
				throw new IllegalArgumentException(String.format("An invalid filename %s was provided", filePath));
			}
		}
	}

	/**
	 * Creates a TextFileReader with the specifed {@link File}.
	 * 
	 * @param file the text file to be analysed by the reader
	 */
	public TextFileReader(File file) {
		if (file.isFile()) {
			textFile = file;
		} else {
			throw new IllegalArgumentException(String.format("%s is not a valid file.", file.getPath()));
		}
	}

	/**
	 * Calculates the average length of all words found in the current document, to a single decimal
	 * place.
	 * 
	 * @return a double representing the average word length, rounded to one decimal place.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	public double getAverageWordLength() throws FileNotFoundException {
		return getAverageWordLength(null);
	}

	/**
	 * Calculates the average length of all words found in the current document, to a single decimal
	 * place. A delimiter other than the default whitespace can be specified.
	 * 
	 * @param delimiter the String delimiter to be used to distinguish words. Set as null to use the
	 *            default whitespace delimiter.
	 * @return a double representing the average word length, rounded to one decimal place.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	public double getAverageWordLength(final String delimiter) throws FileNotFoundException {
		List<String> words = getWords(delimiter);
		double wordLengthSum = 0;
		for (String word : words) {
			wordLengthSum += word.length();
		}
		final String oneDecimalPlace = "#.#";
		return Double.parseDouble(new DecimalFormat(oneDecimalPlace).format(wordLengthSum / words.size()));
	}

	/**
	 * Calculates the lines in the current document and returns the value as an integer.
	 * 
	 * @param ignoreBlankLines when set to true, lines of whitespace will not be included in the
	 *            final count.
	 * @return the number of lines in the document.
	 * @throws IOException an error reading the current document.
	 */
	public int getLineCount(final boolean ignoreBlankLines) throws IOException {
		try (Stream<String> lines = Files.lines(textFile.toPath())) {
			if (ignoreBlankLines) {
				return (int) lines.filter(isNotBlank()).count();
			} else {
				return (int) lines.count();
			}
		}
	}

	/**
	 * Finds the most commonly used letter in the current document.
	 * 
	 * @return most commonly used letter as a char.
	 * @throws IOException an error reading the current document.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	public char getMostCommonLetter() throws FileNotFoundException, IOException {
		Map<Character, Integer> letters = new HashMap<>();
		try (BufferedReader docReader = new BufferedReader(new FileReader(textFile))) {
			int charInt = 0;
			Character mostCommon = null;
			int mostCommonCount = 0;
			while ((charInt = docReader.read()) != -1) {
				Character character = Character.toLowerCase((char) charInt);
				int letterCount = 1;
				if (letters.containsKey(character)) {
					letterCount = letters.get(character);
					letters.put(character, ++letterCount);
				} else if (Character.isLetter(character)) {
					letters.put(character, letterCount);
				}

				if (letterCount > mostCommonCount) {
					mostCommon = character;
					mostCommonCount = letterCount;
				}
			}
			return mostCommon;
		}
	}

	/**
	 * Calculates the word count in the current document.
	 * 
	 * @return the number of words in the document.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	public int getWordCount() throws FileNotFoundException {
		return getWords(null).size();
	}

	/**
	 * Calculates the word count in the current document. The delimiter will default to whitespace
	 * if a null or blank string argument is provided.
	 * 
	 * @param delimiter used to distinguish "words" in a document. Leave as null to default to a
	 *            whitespace delimiter.
	 * @return the number of words in the document.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	public int getWordCount(final String delimiter) throws FileNotFoundException {
		return getWords(delimiter).size();
	}

	/**
	 * Gets the words from the current document, based on the provided delimiter (defaulting to
	 * whitespace if null or blank), and returns them in a {@link List}.
	 * 
	 * @param delimiter used to distinguish "words" in a document. Leave as null to default to a
	 *            whitespace delimiter.
	 * @return a {@link List} containing all the words in the current document.
	 * @throws FileNotFoundException if the current text file cannot be found.
	 */
	private List<String> getWords(final String delimiter) throws FileNotFoundException {
		try (Scanner wordScanner = new Scanner(textFile)) {
			if (StringUtils.isNotBlank(delimiter)) {
				wordScanner.useDelimiter(delimiter);
			}
			List<String> words = new ArrayList<>();
			while (wordScanner.hasNext()) {
				words.add(wordScanner.next());
			}
			return words;
		}
	}

	/**
	 * Predicate for non blank lines in a document.
	 * 
	 * @return true if the line is not blank (i.e. is whitespace only).
	 */
	private Predicate<String> isNotBlank() {
		return line -> StringUtils.isNotBlank(line);
	}

	/**
	 * Returns the string representation of this TextFileReader and its underlying file.
	 */
	@Override
	public String toString() {
		return String.format("TextFileReader [textFile=%s]", textFile);
	}
}
