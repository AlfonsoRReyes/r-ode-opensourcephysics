

package org.opensourcephysics.sip.ch02;
import org.opensourcephysics.frames.PlotFrame;

/**
 * PlotFrameApp demonstrates the use of the Open Source Physics library to produce a simple plot.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0 05/07/05
 */
public class PlotFrameApp {

  /**
   * Starts the Java application.
   * @param args  command line parameters
   */
  public static void main(String[] args) {
    PlotFrame frame = new PlotFrame("x", "sin(x)/x", "Plot example");
    for(int i = -100;i<=100;i++) {
      double x = i*0.2;
      frame.append(0, x, Math.sin(x)/x);
    }
    frame.setVisible(true);
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
  }
}

