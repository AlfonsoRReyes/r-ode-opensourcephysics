/*
 * The org.opensourcephysics.demoapps package contains longer programs that demonstrate
 * how to use various Open Source Physics tools.
 * These examples are proof-of-concept only.
 *
 * Copyright (c) 2007  W. Christian.
 */
package org.opensourcephysics.davidson.ode.flow;

/**
 * A wrapper that allows the FlowLineApp to be run inside a WrapperApplet.
 * @author Wolfgang Christian
 * @version 1.0
 */
public class FlowLineWRApp extends FlowLineApp {


  /**
   * Resets the plot;
   *
   */
  public void resetPlot () {
    drawingPanel.removeObjectsOfClass(FlowLine.class);
    sampleField();
    drawableBuffer.invalidateImage ();
    drawingPanel.repaint ();
  }
    /**
   * Method sliderMoved
   *
   */
  public void sliderMoved () {
    initField (control.getInt ("size"));
    sampleField();
    drawableBuffer.invalidateImage ();
    drawingPanel.repaint ();
  }

  public void setShowPaths(){
     showPaths=control.getBoolean("show paths");
     drawingPanel.repaint ();
  }

  public void setShowField(){
     drawableBuffer.setVisible(control.getBoolean("show field"));
     drawingPanel.repaint ();
  }



  /**
   * The main entry point for the program.
   *
   * @param args
   */
  public static void main (String[] args) {
    new FlowLineControl (new FlowLineWRApp (), args);
  }
}
