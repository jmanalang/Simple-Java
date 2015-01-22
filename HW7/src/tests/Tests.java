package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import code.BRStruct;
import code.Visitor;

public class Tests {

	private Visitor v;
	private static Random r = new Random();
	private static double epsilon = 0.000000001;
	

	private String feedback(BRStruct<Double> tree, double e, double a) {
		return "I executed your Visitor on this tree, "+tree+",\n expecting the answer to be "+e+",\n but the answer I got was "+a;
	}
	
	private BRStruct<Double> populate(double[] v) {
		switch(v.length) {
		case 7: return new BRStruct<Double>().insertRoot(v[0]
				).setLeft(new BRStruct<Double>().insertRoot(v[1])
						.setLeft( new BRStruct<Double>().insertRoot(v[2]) )
						.setRight( new BRStruct<Double>().insertRoot(v[3]) )
				).setRight(new BRStruct<Double>().insertRoot(v[4])
						.setLeft( new BRStruct<Double>().insertRoot(v[5]) )
						.setRight( new BRStruct<Double>().insertRoot(v[6]) )
				);
		case 3: return new BRStruct<Double>().insertRoot(v[0]
				).setLeft(new BRStruct<Double>().insertRoot(v[1])
				).setRight(new BRStruct<Double>().insertRoot(v[2])
				);
		case 1: return new BRStruct<Double>().insertRoot(v[0]
				);
		default: throw new IllegalArgumentException("size: "+v.length);
		}
	}

	private double product(double[] values) {
		double p = 1.0;
		for (double d : values) { p = p * d; }
		return p;
	}
	
	private double[] randomArray(int size) {
		double[] a = new double[size];
		for (int i=0; i<size; i++) {
			a[i] = r.nextDouble();
		}
		return a;
	}

	private boolean withinEpsilon(double x, double y, double eps) {
		return Math.abs(x-y) < eps;
	}

	@Before public void setUp() {
		v = new Visitor();
	}
	
	private void testFramework(double[] values) {
		BRStruct<Double> t = populate(values);
		double expected = product(values);
		double actual = t.execute(v, null);
		assertTrue(feedback(t,expected,actual), withinEpsilon(expected,actual,epsilon));
	}
	
	@Test public void test01() { testFramework(randomArray(1)); }
	@Test public void test02() { testFramework(randomArray(3)); }
	@Test public void test03() { testFramework(randomArray(3)); }
	@Test public void test04() { testFramework(randomArray(3)); }
	@Test public void test05() { testFramework(randomArray(7)); }
	@Test public void test06() { testFramework(randomArray(7)); }
	@Test public void test07() { testFramework(randomArray(7)); }
	@Test public void test08() { testFramework(randomArray(7)); }
	@Test public void test09() { testFramework(randomArray(7)); }
	@Test public void test10() { testFramework(randomArray(7)); }
	@Test public void test11() { 
		double [] values = {1.0};
		testFramework(values);
	}
	@Test public void test12() { 
		double [] values = {1.0,2.0,2.0};
		testFramework(values);
	}
	@Test public void test13() { 
		double [] values = {1.0,2.0,0.0};
		testFramework(values);
	}
	@Test public void test14() { 
		double [] values = {0.0,2.0,1.0};
		testFramework(values);
	}
	@Test public void test15() { 
		double [] values = {2.0,0.0,3.0};
		testFramework(values);
	}
	
	

}
