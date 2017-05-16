/*
 * The org.opensourcephysics.demoapps package contains longer programs that demonstrate
 * how to use various Open Source Physics tools.
 * These examples are proof-of-concept only.
 *
 * Copyright (c) 2007  W. Christian.
 */
package org.opensourcephysics.davidson.ode.flow;

import org.opensourcephysics.numerics.*;
import org.opensourcephysics.display.*;
import java.awt.Graphics;
import org.opensourcephysics.controls.*;
import java.lang.reflect.*;

/**
 * A FlowLine
 * @author Wolfgang Christian
 * @version 1.0
 */
public class FlowLine extends Trail implements ODE {
  double[]      state = new double[2];
  ODESolver odeSolver;
  FlowLineApp app;
  Circle circle = new Circle(0,0,3);
  double x0,y0;
  double tol;

  public FlowLine(double x, double y, FlowLineApp app){
    this.app=app;
    state[0]=x0=x;
    state[1]=y0=y;
    addPoint(x,y);
    odeSolver  = ODESolverFactory.createODESolver(this, app.solverName);
    odeSolver.setStepSize (app.dt);
    tol=app.getControl().getDouble("adaptive solver tolerance");
    if (odeSolver instanceof ODEAdaptiveSolver){
       ( (ODEAdaptiveSolver) odeSolver).setTolerance(tol);
    }
  }

  private FlowLine(){
  }

  public void setXY (double x, double y) {
    x0=x;
    y0=y;
    reset();
  }

  void reset(){
     clear();  // clears trail when dragging
     state[0]=x0;
     state[1]=y0;
     addPoint(x0,y0);
  }

  public void stepFlow () {
    odeSolver.step();
    boolean xwrap=false, ywrap=false;
    if(app.periodicx){
       double dx= app.xmax-app.xmin;
       while(state[0]>app.xmax){
        state[0]-= dx;
        xwrap=true;
       }
       while (state[0]<app.xmin){
          state[0] += dx;
          xwrap=true;
       }
    }
    if (app.periodicy){
       double dy = app.ymax-app.ymin;
       while (state[0]>app.ymax){
          state[1] -= dy;
          ywrap=true;
       }
       while (state[0]<app.ymin){
          state[1] += dy;
          ywrap=true;
       }
    }
    if(xwrap || ywrap){
       moveToPoint(state[0],state[1]);
    }else{
       addPoint(state[0], state[1]);
    }
  }

  /**
   * Gets the state.
   *
   * @return the state
   */
  public double[] getState () {
    return state;
  }

  /**
   * Gets the rate of change using the argument's state variables.
   *
   * @param state  the state
   * @param rate  the rate
   */
  public void getRate (double[] state, double[] rate) {
    rate[0] = app.fxParser.evaluate(state);
    rate[1] = app.fyParser.evaluate(state);
    if(Double.isNaN(rate[0]) || Double.isInfinite(rate[0])) rate[0]=0;
    if(Double.isNaN(rate[1]) || Double.isInfinite(rate[1])) rate[1]=0;
  }

  /**
 * Draw the trail on the panel.
 * @param g
 */
  public void draw(DrawingPanel panel, Graphics g) {
      if(app.showPaths) super.draw(panel, g);
      circle.setXY(state[0],state[1]);
      circle.draw(panel,g);
  }

  /**
   * Returns an XML.ObjectLoader to save and load data for this program.
   *
   * @return the object loader
   */
  public static XML.ObjectLoader getLoader(){
     return new FlowLineLoader();
  }


  /**
 * A class to save and load Dataset data in an XMLControl.
 */
private static class FlowLineLoader extends XMLLoader {

  public void saveObject(XMLControl control, Object obj) {
    FlowLine fl = (FlowLine) obj;
    super.saveObject(control, obj);
    control.setValue("x0", fl.x0);
    control.setValue("y0", fl.y0);
    control.setValue("x", fl.state[0]);
    control.setValue("y", fl.state[1]);
    control.setValue("circle", fl.circle);
    control.setValue("solver name",fl.odeSolver.getClass().getName() );
    control.setValue("solver step",fl.odeSolver.getStepSize() );
    control.setValue("adaptive solver tolerance",fl.tol);
  }

  public Object createObject(XMLControl control) {
    return new FlowLine();  // not enough information to create object
  }

  public Object loadObject(XMLControl control, Object obj) {
    super.loadObject(control, obj);
    FlowLine fl = (FlowLine) obj;
    fl.circle= (Circle) control.getObject("circle");
    fl.x0=control.getDouble("x0");
    fl.y0=control.getDouble("y0");
    fl.state[0]=control.getDouble("x");
    fl.state[1]=control.getDouble("y");
    fl.addPoint(fl.state[0],fl.state[1]);
    String solverName=control.getString("solver name");
   try{
      fl.odeSolver =
         (ODESolver) Class.forName(solverName).getConstructor(new Class[]{ODE.class}).newInstance((Object[])new FlowLine[]{fl});
   } catch (ClassNotFoundException ex){
      System.err.println(ex.toString());
   } catch (SecurityException ex){
      System.err.println(ex.toString());
   } catch (NoSuchMethodException ex){
      System.err.println(ex.toString());
   } catch (InvocationTargetException ex){
      System.err.println(ex.toString());
   } catch (IllegalArgumentException ex){
      System.err.println(ex.toString());
   } catch (IllegalAccessException ex){
      System.err.println(ex.toString());
   } catch (InstantiationException ex){
      System.err.println(ex.toString());
   }
   if (fl.odeSolver==null){
      fl.odeSolver  = ODESolverFactory.createODESolver(fl, "multistep");
   }
   fl.odeSolver.setStepSize(control.getDouble("solver step"));
   fl.tol=control.getDouble("adaptive solver tolerance");
   if (fl.odeSolver instanceof ODEAdaptiveSolver){
      ( (ODEAdaptiveSolver) fl.odeSolver).setTolerance(fl.tol);
   }
   return obj;
  }
}

}




