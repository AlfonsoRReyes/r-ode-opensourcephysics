

package org.opensourcephysics.sip.ch02;
// gets needed classes, asterisk * means get all classes in controls subdirectory

import org.opensourcephysics.controls.*;

public class CalculationApp extends AbstractCalculation {

  /**
   * Does a calculation.
   */
  public void calculate() { // Does a calculation
    control.println("Calculation button pressed.");
    double x = control.getDouble("x value"); // String must match argument of setValue
    control.println("x*x = "+(x*x));
    control.println("random = "+Math.random());
  }

  /**
   * Resets the program to its initial state.
   */
  public void reset() {
    control.setValue("x value", 10.0); // describes parameter and sets its value
  }

  /**
   * Starts the Java application.
   * @param args  command line parameters
   */
  public static void main(String[] args) { // Create a calculation control structure using this class
    CalculationControl control = CalculationControl.createApp(new CalculationApp());
  }
}

