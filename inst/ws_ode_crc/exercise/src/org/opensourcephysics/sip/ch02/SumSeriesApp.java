/**
 * Exercise 2.6.a, b Write a program to sum the following series for a
 * given value of N:
 */
package org.opensourcephysics.sip.ch02;

/**
 * @author ZDE38397
 *
 */
public class SumSeriesApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 
		double N = 5;
		double sum  = 0;
		for (int m =1; m<=N; m++)	{
			sum = sum + 1.0 / (m*m);
		}

		System.out.println(sum);
	}

}
