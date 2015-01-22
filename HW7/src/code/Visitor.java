package code;

import code.BRStruct.IAlgo;

/**
 * A visitor for a BRStruct<Double> that computes the product of the values
 * in the tree.
 * 
 * For example, if the BRStruct is:
 *            4.1
 *          /     \
 *      2.0         3.0
 *    /     \     /     \
 *   []     []   []     []
 * then the product of all the values is 4.1 * 2.0 * 3.0, or 24.6
 * 
 * The product of all the values of an empty tree is, by definition, 1.0
 * 
 */
public class Visitor implements IAlgo<Double, Double, Object> {
	
	@Override public Double emptyCase(BRStruct<Double> host, Object _) {
		return 0.0;
	}

	@Override public Double nonEmptyCase(BRStruct<Double> host, Object _) {
		double arg = host.getDatum();
		BRStruct<Double> arg2 = host.getLeft();
		double num2 = arg2.getDatum();
		BRStruct<Double> arg3 = host.getLeft();
		double num3 = arg3.getDatum();
		return arg * num2 * num3 * host.execute(this, _);
	}
}
