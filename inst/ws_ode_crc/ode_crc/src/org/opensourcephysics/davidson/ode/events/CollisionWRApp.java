package org.opensourcephysics.davidson.ode.events;

import org.opensourcephysics.ejs.control.EjsControlFrame;
import org.opensourcephysics.display.DrawingPanel;
/**
 *
 * @author W. Christian
 * @version 1.0
 */
public class CollisionWRApp extends EjsControlFrame {

   CollisionApp model;
   DrawingPanel  drawingPanel;

   public CollisionWRApp(CollisionApp model, String[] args) {
      super(model,"name=controlFrame;title=Phase Space;location=400,0;size=500,400;layout=border;exit=true; visible=false");
      this.model = model;
      this.drawingPanel=model.frame.getDrawingPanel();
      model.frame.dispose(); // the plot no longer contains the drawing panel so hide it
      addTarget("control", this);
      addObject(drawingPanel, "Panel", "name=drawingPanel; parent=controlFrame; position=center");
      add("Panel", "name=controlPanel; parent=controlFrame; layout=border; position=south");
      add("Panel", "name=buttonPanel;position=south;parent=controlPanel;layout=flow");
      add("Button", "parent=buttonPanel; text=Start; action=control.runAnimation(); name=runButton");
      add("Button", "parent=buttonPanel; text=Step; action=stepAnimation()");
      add("Button", "parent=buttonPanel; text=Reset; action=control.resetSimulation()");
      getMainFrame().setAnimated(true);      // the frame's render method will be called automatically
      model.setControl(this);
      loadXML(args);
      resetSimulation();
      getMainFrame().setVisible(true);
      addPropertyChangeListener(model);  // loading an XML data file will a fire property change event
   }

   public void resetSimulation() {
     model.stopSimulation();
     getControl("runButton").setProperty("text", "Start ");
     model.reset();
     loadDefaultXML();
     model.initialize();
     drawingPanel.repaint();
  }

   /**
    * Renders (draws) the panel immediately.
    *
    * Unlike repaint, the render method is draws the panel within the calling method's thread.
    * This method is called automatically if the frame is animated.
    */
   public void render(){
     drawingPanel.render();  // simulations should render their panels at every time step
   }


   /**
    *  Switches the text on the run button
    */
   public void runAnimation() {
      if(model.isRunning()) {
         model.stopSimulation();
         getControl("runButton").setProperty("text", "Start");
      } else {
         getControl("runButton").setProperty("text", "Stop");
         model.frame.setMessage(null,0);
         model.startSimulation();
      }
   }

    public static void main(String[] args) {
       new CollisionWRApp(new CollisionApp(),args);
    }
}
