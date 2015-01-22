package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import util.jrtl.CallableMethod;
import util.jrtl.ClassWalker;
import util.jrtl.DynamicType;
import util.jrtl.MethodInfo;

public class ReferenceTests {
	
	private static util.jrtl.CallableMethod studentMethod;
	private static List<util.jrtl.DynamicType> studentClasses;
	private static String studentMethodError = null;
	private static String studentClassError = null;

	static {
		util.jrtl.ClassFinder matchAllClasses = new util.jrtl.ClassFinder() {
			@Override public boolean matches(DynamicType c) {
				return ! c.getPackage().equals("tests")
			        && ! c.getPackage().startsWith("util."); // anything in 'util' automagically excluded
			}
		};
		
		studentClasses = ClassWalker.findClass(matchAllClasses);

		switch (studentClasses.size()) {
			case 0:
				studentClassError = "You must define a class; you defined "+studentClasses.size()+".";
				break;
			case 1:
				DynamicType type = studentClasses.get(0);
				List<MethodInfo> methodCandidates = type.getMethodsByType(HashMap.class, String.class);
				switch (methodCandidates.size()) {
					case 0:
						studentMethodError = "Define a method to record name:telephone number mappings, of type java.lang.String -> HashMap<String,String>";
						break;
					case 1:
						util.jrtl.MethodInfo m = methodCandidates.get(0);
						if(type.hasConstructor()) // check for default constructor
							studentMethod = CallableMethod.Instance(m,type.createInstance());
						else if (m.isStatic())
							studentMethod = CallableMethod.Static(m);
						else
							studentMethodError = "Create a zero-argument constructor or make the letter count method static.";
						break;
					default:
						studentMethodError = "Define only one method of type String -> HashMap<String,String>.  You defined "+methodCandidates.size()+" methods:";
						for (MethodInfo mi: methodCandidates) {
							studentMethodError += "\n\t"+mi.getSignature();
						}
				}
				break;
			default: 
				studentClassError = "You must define just one class; you defined "+studentClasses.size()+":";
				for (DynamicType t : studentClasses) {
					studentClassError = studentClassError + "\n\t" + t.getPackage() + " :: " + t.getName();
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
	
	public String hashMapsAreTheSame(HashMap<String,String> a, HashMap<String,String> b) {
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
				diff += "\n\tExpected answer has these extra keys (names that your solution missed):"; 
				for (String s : al) {
					diff += "\n    \""+s+"\"";
				}
			}
			if (!bl.isEmpty()) {
				diff += "\n\t**************************************************************************";
				diff += "\n\tComputed answer has these extra keys (non-names that your solution found):"; 
				for (String s : bl) {
					diff += "\n    \""+s+"\"";
				}
			}
		}
		else { // keysets are the same, check counts
			String diffValues = "";
			for (String name : aKeySet) {
				String aNumber = a.get(name);
				String bNumber = b.get(name);
				if (!aNumber.equals(bNumber)) {
					diffValues += "\n\tfor name \""+name+"\", expected telephone number is "+aNumber+" while computed telephone number is "+bNumber+".";
				}
			}
			if (diffValues.length() != 0) {
				diff = "\nThe telephone numbers differ as follows:"+diffValues;
			}
		}
		return diff;
	}

	public void testGuard() {
		assertNull(studentClassError,studentClassError);  // check for problem with class existence
		assertNull(studentMethodError,studentMethodError);// check for problem with method existence
		foilStub();										 // check for problem with method being a stub
	}
	
	public void foilStub() {
		Object[] args = {null};
		util.jrtl.CallResult callResult = studentMethod.call(args);
		assertTrue("It looks like your method is a simple stub.\nYou must provide a suitable implementation of the method for any tests to pass.",
					callResult.isError());
	}

	private void testSingle(String filePath) {
		testGuard();  // guard on this test
		HashMap<String,String> expected = util.general.Solution.solution(filePath);
		util.jrtl.CallResult callResult = studentMethod.call(filePath);
		assertFalse("An error occured when I called your method with this arguments: (\""+filePath+"\").  \nThe reported problem is: "+callResult.getProblem(), callResult.isError());
		@SuppressWarnings("unchecked")
		HashMap<String,String> actual = (HashMap<String,String>) (callResult.getResult().getRawObject());
		String result = hashMapsAreTheSame(expected,actual);
		assertTrue(result, result.length()==0);
	}
	
	@Test public void test01() { testSingle("SampleInputFiles/SampleTextFile7.txt");	}
	@Test public void test02() { testSingle("SampleInputFiles/SampleTextFile8.txt");	}

}
