package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import code.LRStruct;
import code.Visitor;

public class Tests {

	private Visitor v;
	private String LETTERS = "abcde";
	private Random random = new Random();

	private String feedback(LRStruct<String> lst, int e, int a) {
		return "I executed your visitor on this list, "+lst+", expecting the answer to be "+e+", but the answer I got was "+a;
	}

	private String random(int minLength, int maxLength) {
		String s = "";
		int length = minLength + random.nextInt(maxLength-minLength+1);
		for (int i=0; i<length; i++) {
			int r = random.nextInt(LETTERS.length());
			s += LETTERS.charAt(r);
		}
		return s;
	}
	
	private ArrayList<String> randomList(int nLong) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<nLong; i++) {
			list.add(random(4,6));
		}
		Collections.shuffle(list);
		return list;
	}

	private LRStruct<String> copyToLRS(ArrayList<String> list) {
		LRStruct<String> lrs = new LRStruct<String>();
		for (String s: list) {
			lrs.insertFront(s);
		}
		return lrs;
	}
	
	@Before public void setUp() {
		v = new Visitor();
	}
	
	@Test public void test01() { randomTest(); }
	@Test public void test02() { randomTest(); }
	@Test public void test03() { randomTest(); }
	@Test public void test04() { randomTest(); }
	@Test public void test05() { randomTest(); }
	@Test public void test06() { randomTest(); }
	@Test public void test07() { randomTest(); }
	@Test public void test08() { randomTest(); }
	@Test public void test09() { randomTest(); }
	@Test public void test10() { randomTest(); }
	@Test public void test11() { randomTest(); }
	@Test public void test12() { randomTest(); }
	@Test public void test13() { randomTest(); }
	@Test public void test14() { randomTest(); }
	@Test public void test15() { randomTest(); }
	@Test public void test16() { randomTest(); }
	@Test public void test17() { randomTest(); }
	@Test public void test18() { randomTest(); }
	@Test public void test19() { randomTest(); }
	@Test public void test20() { randomTest(); }

	public void randomTest() {
		ArrayList<String>  aList = randomList(20);
		LRStruct<String> lst = copyToLRS(aList);
		int expected = expected(aList, new Predicate() {
			@Override public boolean apply(String s) { return s.charAt(0) == 'a'; }
		});
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}

	private int expected(ArrayList<String> aList, Predicate predicate) {
		int expected = 0;
		for (String s : aList) {
			if (predicate.apply(s)) { expected++; }
		}
		return expected;
	}
	
	private interface Predicate {
		public boolean apply(String s);
	}


}
