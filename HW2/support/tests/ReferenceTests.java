package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import util.general.RandomGenerator;
import util.jrtl.CallableMethod;
import util.jrtl.ClassWalker;
import util.jrtl.DynamicType;
import util.jrtl.MethodInfo;

public class ReferenceTests {
	
	private static util.jrtl.CallableMethod letterCountMethod;
	private static List<util.jrtl.DynamicType> classes;
	private static String letterCountMethodError = null;
	private static String classError = null;

	static {
		util.jrtl.ClassFinder matchAllClasses = new util.jrtl.ClassFinder() {
			@Override public boolean matches(DynamicType c) {
				return ! c.getPackage().equals("tests") 
				    && ! c.getPackage().startsWith("util."); // anything in 'util' automagically excluded
			}
		};
		
		classes = ClassWalker.findClass(matchAllClasses);

		if (classes.size() != 1) {
			classError = "Define a single class; you defined "+classes.size()+":";
			for (DynamicType t : classes) {
				classError = classError + "\n\t" + t.getPackage() + " :: " + t.getName();
			}
		}
		else {
			DynamicType type = classes.get(0);
			List<MethodInfo> letterCountMethodCandidates = type.getMethodsByType(int[].class, String.class);
			switch (letterCountMethodCandidates.size()) {
				case 0:
					letterCountMethodError = "Define a method to count letters, of type java.lang.String -> int[]";
					break;
				case 1:
					util.jrtl.MethodInfo m = letterCountMethodCandidates.get(0);
					if(type.hasConstructor()) // check for default constructor
						letterCountMethod = CallableMethod.Instance(m,type.createInstance());
					else if (m.isStatic())
						letterCountMethod = CallableMethod.Static(m);
					else
						letterCountMethodError = "Create a zero-argument constructor or make the letter count method static.";
					break;
				default:
					letterCountMethodError = "Define only one method of type String -> int[].  You defined "+letterCountMethodCandidates.size()+" methods:";
					for (MethodInfo mi: letterCountMethodCandidates) {
						letterCountMethodError += "\n\t"+mi.getSignature();
					}
			}
		}
	}

	public String arraysAreTheSame(String input, int[] a, int[] b) {
		String diff = "";
		if (a.length != b.length) {
			return "\nThe length of the array should have been "+a.length+", but is was "+b.length+".";
		}
		for (int i=0; i<a.length; i++) {
			if (a[i] != b[i]) {
				diff = diff + "\n    array entry ["+i+"] should be "+a[i]+" but was "+b[i];
			}
		}
		if (diff.length() > 0) {
			diff = "\nFor the string \""+input+"\":" + diff;
		}
		return diff;
	}
	
	public void testSingleError() { testClassError(); foilStub(); assertNull(letterCountMethodError,letterCountMethodError); }
	public void testClassError() { assertNull(classError,classError); }

	public void foilStub() {
		Object[] args = {null};
		util.jrtl.CallResult result = letterCountMethod.call(args);
		assertTrue("It looks like your method is a simple stub.\nYou must provide a suitable implementation of the method for any tests to pass.",
					result.isError());
	}

	private void testSingle(int v, int c, int o) {
		String s = randomString(v,c,o);  //System.out.println("Testing with String \""+s+"\"");
		int[] expected = {v, c, o};
		testSingleError();  // guard on this test
		util.jrtl.CallResult callResult = letterCountMethod.call(s);
		assertFalse("An error occured when I called your method with this arguments: (\""+s+"\").  \nThe reported problem is: "+callResult.getProblem(), callResult.isError());
		int [] actual = (int []) (callResult.getResult().getRawObject());
		String result = arraysAreTheSame(s,expected,actual);
		assertTrue(result, result.length()==0);
	}

	private static final String VOWELS = "aeiouAEIOU";
	private static final String CONSONANTS = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
	private static final String OTHERS = "~!@#$%^&*()_+`1234567890-={}|[]\\:\";'<>?,./ \t\n";
	
	private static final RandomGenerator RANDOM = new RandomGenerator();
	
	private String randomString(int vowels, int consonants, int others) {
		List<Character> chars = new ArrayList<Character>();
		addCharactersToList(vowels, VOWELS, chars);
		addCharactersToList(consonants, CONSONANTS, chars);
		addCharactersToList(others, OTHERS, chars);
		Collections.shuffle(chars);
		return list2String(chars);
	}

	private String list2String(List<Character> chars) {
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			sb.append(c);
		}
		return sb.toString();
	}

	private void addCharactersToList(int howMany, String source, List<Character> sink) {
		for (int i=0; i<howMany; i++) {
			int randomIndex = RANDOM.randomIntInRange(0, source.length());
			sink.add(source.charAt(randomIndex));
		}
	}
	
	@Test public void test01() { testSingle(0,0,0);	}
	@Test public void test02() { testSingle(0,0,1);	}
	@Test public void test03() { testSingle(0,1,0);	}
	@Test public void test04() { testSingle(0,1,1);	}
	@Test public void test05() { testSingle(1,0,0);	}
	@Test public void test06() { testSingle(1,0,1);	}
	@Test public void test07() { testSingle(1,1,0);	}
	@Test public void test08() { testSingle(1,1,1);	}
	@Test public void test09() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test10() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test11() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test12() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test13() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test14() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test15() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test16() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test17() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test18() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test19() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }
	@Test public void test20() { testSingle(RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10),RANDOM.randomIntInRange(2,10)); }

}
