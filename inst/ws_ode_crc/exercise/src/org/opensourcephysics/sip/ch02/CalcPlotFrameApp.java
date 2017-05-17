/*
 * Exercise 2.14 b
 * Incorporate plot statements in Listing 2.18 into a new class that extends
 * the class AbstracxtCalculation and plots the function sin kx for various
 * values of the input parameter k 
 */

package org.opensourcephysics.sip.ch02;
// gets needed classes, asterisk * means get all classes in controls subdirectory

import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.PlotFrame;

public class CalcPlotFrameApp extends AbstractCalculation {
  PlotFrame frame = new PlotFrame("x", "k*sin(x)", "Plot example");
  double k = 0;


  /**
   * Does a calculation when button pressed
   */
  public void calculate() { // Does a calculation
    control.println("Calculation button pressed now");
    k = control.getDouble("k value"); // String must match argument of setValue
    control.println("k = " + k );
	  for(int i = -100;i<=100;i++) {
		  double x = i*0.2;
		  frame.append(0, x, k * Math.sin(x));
	  } 
  }
  
  /**
   * Resets the program to its initial state when button pressed
   */
  public void reset() {
    control.setValue("k value", 10.0); // describes parameter and sets its value
    calculate();	// show plot
	frame.setVisible(true);
  }

  /**
   * Starts the Java application.
   * @param args  command line parameters
   */
  public static void main(String[] args) { // Create a calculation control structure using this class
    CalculationControl.createApp(new CalcPlotFrameApp());

  }
}

