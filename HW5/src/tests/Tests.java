package tests;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import code.LRStruct;
import code.Visitor;

public class Tests {

	private Visitor v;
	private LRStruct<String> lst;
	private Random random = new Random();

	private String feedback(LRStruct<String> lst, int e, int a) {
		return "\nI executed your visitor on this list, "+lst+", \nexpecting the answer to be "+e+", \nbut your code produced "+a;
	}

	@Before public void setUp() {
		v = new Visitor();
		lst = new LRStruct<String>();		
	}
	
	@Test public void testA() {
		int expected = 0;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}
		
	@Test public void testB() {
		lst.insertFront("");
		int expected = 0;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}

	@Test public void testC() {
		lst.insertFront("Fred");
		int expected = 4;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}

	@Test public void testD() {
		lst.insertFront("Wilma").insertFront("Fred");
		int expected = 9;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}

	@Test public void testE() {
		lst.insertFront("Wilma").insertFront("");
		int expected = 5;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}
	@Test public void testF() {
		lst.insertFront("Fred").insertFront("Fred");
		int expected = 8;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}
	@Test public void testG() {
		lst.insertFront("Wilma").insertFront("Fred").insertFront("Pebbles");
		int expected = 16;
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}
	
	public void randomTest() {
		String[] words = {"","a","aa","aaa","aaaa","aaaaa"};
		int expected = 0;
		int size = random.nextInt(9);
		for (int i=0; i<size; i++) {
			int index = random.nextInt(6); // int from [0,6)
			lst.insertFront(words[index]);
			expected += index;
		}
		int actual = lst.execute(v, null);
		assertTrue(feedback(lst,expected,actual), expected == actual);
	}
	
	@Test public void randomTest01() { randomTest(); }
	@Test public void randomTest02() { randomTest(); }
	@Test public void randomTest03() { randomTest(); }
	@Test public void randomTest04() { randomTest(); }
	@Test public void randomTest05() { randomTest(); }
	@Test public void randomTest06() { randomTest(); }
	@Test public void randomTest07() { randomTest(); }
	@Test public void randomTest08() { randomTest(); }
	@Test public void randomTest09() { randomTest(); }
	@Test public void randomTest10() { randomTest(); }
	@Test public void randomTest11() { randomTest(); }
	@Test public void randomTest12() { randomTest(); }
	@Test public void randomTest13() { randomTest(); }

}
