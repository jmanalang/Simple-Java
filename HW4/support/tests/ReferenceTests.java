package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import util.general.SOLUTION;
import util.jrtl.CallableMethod;
import util.jrtl.ClassWalker;
import util.jrtl.DynamicType;
import util.jrtl.MethodInfo;

public class ReferenceTests {
	
	private static util.jrtl.CallableMethod wordCountMethod;
	private static List<util.jrtl.DynamicType> classes;
	private static String wordCountMethodError = null;
	private static String classError = null;

	static {
		util.jrtl.ClassFinder matchAllClasses = new util.jrtl.ClassFinder() {
			@Override public boolean matches(DynamicType c) {
				return ! c.getPackage().equals("tests")
				    && ! c.getPackage().startsWith("util.");
			}
		};
		
		classes = ClassWalker.findClass(matchAllClasses);

		if (classes.size() != 1) {
			classError = "Define a single class; " + (classes.size()==0 ? "you defined no classes at all!" : "you defined "+classes.size()+":");
			for (DynamicType t : classes) {
				classError = classError + "\n\t" + t.getPackage() + " :: " + t.getName();
			}
		}
		else {
			DynamicType type = classes.get(0);
			List<MethodInfo> letterCountMethodCandidates = type.getMethodsByType(HashMap.class, String.class);
//			List<MethodInfo> letterCountMethodCandidates = type.getMethodsByType("HashMap<String,Integer>", "String", "String", "int"); // given order
//			List<MethodInfo> letterCountMethodCandidates = type.getMethodsByType("HashMap<String,Integer>", anyOrder("String", "String", "int")); // any order
			switch (letterCountMethodCandidates.size()) {
				case 0:
					wordCountMethodError = "Define a method to count letters, of type java.lang.String -> int[]";
					break;
				case 1:
					util.jrtl.MethodInfo m = letterCountMethodCandidates.get(0);
					if(type.hasConstructor()) // check for default constructor
						wordCountMethod = CallableMethod.Instance(m,type.createInstance());
					else if (m.isStatic())
						wordCountMethod = CallableMethod.Static(m);
					else
						wordCountMethodError = "Create a zero-argument constructor or make the letter count method static.";
					break;
				default:
					wordCountMethodError = "Define only one method of type String -> int[].  You defined "+letterCountMethodCandidates.size()+" methods:";
					for (MethodInfo mi: letterCountMethodCandidates) {
						wordCountMethodError += "\n\t"+mi.getSignature();
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
	
	public void testSingleError() { testClassError(); foilStub(); assertNull(wordCountMethodError,wordCountMethodError); }
	public void testClassError() { assertNull(classError,classError); }

	public void foilStub() {
		Object[] args = {null};
		util.jrtl.CallResult result = wordCountMethod.call(args);
		assertTrue("It looks like your method is a simple stub.\nYou must provide a suitable implementation of the method for any tests to pass.",
					result.isError());
	}

	@SuppressWarnings("unchecked")
	private void testSingle(int i) {
		testSingleError();  // guard on this test
		String filePath = "SampleInputFiles/SampleInputFile"+i+".xml";
		HashMap<String,Integer> expected = new SOLUTION().runner(filePath);
		util.jrtl.CallResult callResult = wordCountMethod.call(filePath);
		assertFalse("An error occured when I called your method with this arguments: (\""+filePath+"\").  \nThe reported problem is: "+callResult.getProblem(), callResult.isError());
		HashMap<String,Integer> actual = (HashMap<String,Integer>) (callResult.getResult().getRawObject());
		String result = hashMapsAreTheSame(expected,actual);
		assertTrue(result, result.length()==0);
	}

	public String hashMapsAreTheSame(HashMap<String,Integer> a, HashMap<String,Integer> b) {
		String diff = "";
		Set<String> aKeySet = a.keySet();
		Set<String> bKeySet = b.keySet();
		if (!aKeySet.equals(bKeySet)) {
			ArrayList<String> al = new ArrayList<String>(aKeySet);
			ArrayList<String> bl = new ArrayList<String>(bKeySet);
			al.removeAll(bKeySet);
			bl.removeAll(aKeySet);
			diff += "\nThe keysets differ: ";
			if (!al.isEmpty()) {
				diff += "\n\t***********************************************************************";
				diff += "\n\tExpected answer has these extra keys (words that your solution missed):"; 
				for (String s : al) {
					diff += "\n    \""+s+"\"";
				}
			}
			if (!bl.isEmpty()) {
				diff += "\n\t**************************************************************************";
				diff += "\n\tComputed answer has these extra keys (non-words that your solution found):"; 
				for (String s : bl) {
					diff += "\n    \""+s+"\"";
				}
			}
		}
		else { // keysets are the same, check counts
			String diffValues = "";
			for (String s : aKeySet) {
				int aCount = a.get(s);
				int bCount = b.get(s);
				if (aCount != bCount) {
					diffValues += "\n\tfor key \""+s+"\", expected count is "+aCount+" while computed count is "+bCount+".";
				}
			}
			if (diffValues.length() != 0) {
				diff = "\nThe computed counts differ as follows:"+diffValues;
			}
		}
		return diff;
	}
	
	@Test public void test01() { testSingle(1);	}
	@Test public void test02() { testSingle(2);	}
	@Test public void test03() { testSingle(3);	}
	@Test public void test04() { testSingle(4);	}
	@Test public void test05() { testSingle(5);	}

}
