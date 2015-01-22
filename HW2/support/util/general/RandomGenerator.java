package util.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RandomGenerator {
	
	final private Random rand;

	public RandomGenerator() {
		rand = new Random();
	}
	public RandomGenerator(long seed) {
		rand = new Random(seed);
	}
	
	/**
	 * Creates an unmodifiable collection of the specified length,
	 * consisting of random integers in the range minValue to maxValue.
	 * This range is inclusive of minValue, but excludes of maxValue.
	 * @param length number of elements
	 * @param minValue lowest value for range (inclusive)
	 * @param maxValue highest value for range (exclusive)
	 * @return collection of random integers
	 */
	public Collection<Integer> getIntSequence(int length, int minValue, int maxValue) {
		ArrayList<Integer> result = new ArrayList<Integer>(length);
		for (int i=0; i<length; ++i) {
			result.add(randomIntInRange(minValue, maxValue));
		}
		return Collections.unmodifiableList(result);
	}
	
	public int randomIntInRange(int minValue, int maxValue) {
		return rand.nextInt(maxValue-minValue)+minValue;
	}
	
	/**
	 * Creates an unmodifiable collection of the specified length, consisting of random strings.
	 * @param length number of elements
	 * @return collection of random strings
	 */
	public Collection<String> getStringSequence(int length) {
		ArrayList<String> result = new ArrayList<String>(length);
		for (int i=0; i<length; ++i) {
			result.add(randomLetterString(MIN_STRING_LENGTH, MAX_STRING_LENGTH));
		}
		return Collections.unmodifiableList(result);
	}
	
	public Collection<String> getRandomElements(Collection<String> source, int numToTake) {
		ArrayList<String> temp = new ArrayList<String>(source);
		Collections.shuffle(temp, rand);
		return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(temp.toArray(new String[0]), numToTake)));
	}
	

	private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final int MIN_STRING_LENGTH = 1;
	private static final int MAX_STRING_LENGTH = 15;
	
	private String randomLetterString(int min, int max) {
		String s = "";
		int howMany = rand.nextInt(max - min) + min + 1;
		for (int i=0 ; i<howMany; i++) {
			int whichOne = rand.nextInt(LETTERS.length());
			s += LETTERS.charAt(whichOne);
		}
		return s;
	}
	
}
