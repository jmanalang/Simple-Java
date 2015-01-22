package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import code.BRStruct;
import code.Visitor;

public class Tests {

	private Visitor<Integer> v;
	private BRStruct<Integer> tree;
//	private String LETTERS = "abcdefghijklmnopqrstuvwxyz \t\n";
//	private Random random = new Random();

//	private String random(int minLength, int maxLength) {
//		String s = "";
//		int length = minLength + random.nextInt(maxLength-minLength+1);
//		for (int i=0; i<length; i++) {
//			int r = random.nextInt(LETTERS.length());
//			s += LETTERS.charAt(r);
//		}
//		return s;
//	}
	
	private BRStruct<Integer> randomTree(int number) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<number*10; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		BST<Integer> tree = new BST<Integer>();
		for (int i=0; i<number; i++) {
			tree.insert(list.get(i));
		}
		return tree.getTree();
	}
	
	@Before public void setUp() {
		v = new Visitor<Integer>();
		tree = new BRStruct<Integer>();		
	}
	
	public void randomTest(int expected) {
		tree = randomTree(expected);
		int actual = tree.execute(v, null);
		assertTrue("I inserted "+expected+" random values into an initially empty tree, expecting its size to be "+expected+", but your visitor says it contains "+actual+" values.",
				expected == actual);
	}
	
	@Test public void test00() { randomTest( 0); }
	@Test public void test01() { randomTest( 1); }
	@Test public void test02() { randomTest( 2); }
	@Test public void test03() { randomTest( 3); }
	@Test public void test04() { randomTest( 4); }
	@Test public void test05() { randomTest( 5); }
	@Test public void test06() { randomTest( 6); }
	@Test public void test07() { randomTest( 7); }
	@Test public void test08() { randomTest( 8); }
	@Test public void test09() { randomTest( 9); }
	@Test public void test10() { randomTest(10); }
	@Test public void test11() { randomTest(11); }
	@Test public void test12() { randomTest(12); }
	@Test public void test13() { randomTest(13); }
	@Test public void test14() { randomTest(14); }
	@Test public void test15() { randomTest(15); }
	@Test public void test16() { randomTest(16); }
	@Test public void test17() { randomTest(17); }
	@Test public void test18() { randomTest(18); }
	@Test public void test19() { randomTest(19); }
}