/* This file is part of the jgpml Project.
 * http://github.com/renzodenardi/jgpml
 *
 * Copyright (c) 2011 Renzo De Nardi and Hugo Gravato-Marques
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.ictwsn.api.Algorithm.jgpml;

import Jama.Matrix;

import java.util.ArrayList;


/**
 * Simple Class to load the example data from files straight into Matrices.
 */
public class ArrayToMatrix {

	/**
	 * Load data
	 * 
	 * @param filename
	 *            data file
	 * @param sizeofInputs
	 * @param sizeofOutputs
	 * @return [X, Y]
	 */
	public static Matrix[] load2(double[] nums, int sizeofInputs, int sizeofOutputs) {
		ArrayList<double[]> inputsList = new ArrayList<double[]>();
		ArrayList<double[]> outputsList = new ArrayList<double[]>();
		int len = nums.length - sizeofInputs - sizeofOutputs;
		for (int i = 0; i <= len; i++) {
			double[] in = new double[sizeofInputs];
			double[] out = new double[sizeofOutputs];
			int j = i;
			for (int m = 0; m < sizeofInputs; m++) {
				in[m] = nums[j++];
			}
			for (int n = 0; n < sizeofOutputs; n++) {
				out[n] = nums[j++];
			}
			inputsList.add(in);
			outputsList.add(out);
		}
		double[][] inmat = new double[inputsList.size()][sizeofInputs];
		double[][] outmat = new double[inputsList.size()][sizeofOutputs];
		inputsList.toArray(inmat);
		outputsList.toArray(outmat);
		return new Matrix[] { new Matrix(inmat), new Matrix(outmat) };
	}

	/**
	 * Simple example of how to use this class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double[]temp={1,2,3,4,3,4,5,76,4,5,6,7,8,9,67,5,4,3,32,22,34,56,44,33,77,86,34};

		Matrix[] data = ArrayToMatrix.load2(temp, 6, 1);
		System.out.println(data);

	}

}
