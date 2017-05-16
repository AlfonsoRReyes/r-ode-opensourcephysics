/**
 * Exercise 2.6.a, b, c Write a program to sum the following series for a
 * given value of N:
 */
package org.opensourcephysics.sip.ch02;

/**
 * @author ZDE38397
 *
 */
public class SumSeriesEpsilonApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 
		double N = 1;			// initial value for N
		double epsilon = 0.01;	// error or tolerance value
		double sum0 = 0;		// previous value of the summation
		double delta = 0;		// difference of summations

	while (N < 25)	{
		double sum  = 0;
		for (int m=1; m<=N; m++)	{
			sum0 = sum;
			sum = sum + 1.0 / (m*m);
		}	
		
		N = N +1;
		delta = sum - sum0;
		System.out.println("N=" + N + "Sum0=" + sum0 + "Sum=" + sum + "delta=" + delta);
		if (delta <= 0.01) break;
	}

	}

}
