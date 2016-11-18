package com.ictwsn.api.Algorithm;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public  class algorithmAPI {
	public static void main(String[] args){
	}
	public static double getMean(double[] nums){
		Mean mean=new Mean();
		return mean.evaluate(nums);
		
	}
	public static double getMax(double[] nums){
		Max max=new Max();
		return max.evaluate(nums);
		
	}
	public static double getMin(double[] nums){
		Min min=new Min();
		return min.evaluate(nums);
		
	}
	public static double getPearsonsCorrelation(double[] nums1,double[] nums2){
		double res=new PearsonsCorrelation().correlation(nums1, nums2);
		return res;
		
	}
	public static double getSpearmansCorrelation(double[] nums1,double[] nums2){
		double res=new SpearmansCorrelation().correlation(nums1, nums2);
		return res;
		
	}
}
