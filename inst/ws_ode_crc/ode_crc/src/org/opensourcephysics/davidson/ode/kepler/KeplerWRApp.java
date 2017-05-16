package org.opensourcephysics.davidson.ode.kepler;

import org.opensourcephysics.ejs.control.EjsControlFrame;
import org.opensourcephysics.display.DrawingPanel;

/**
 *
 * @author W. Christian
 * @version 1.0
 */
public class KeplerWRApp extends EjsControlFrame {

   KeplerApp model;
   DrawingPanel  drawingPanel;

   public KeplerWRApp(KeplerApp model) {
      super(model,"name=controlFrame;title=Inverse Square Law;location=400,0;layout=border;exit=true; visible=false");
      this.model = model;
      drawingPanel=model.orbitFrame.getDrawingPanel();
      model.orbitFrame.dispose();
      addTarget("control", this);
      addObject(drawingPanel, "Panel", "name=drawingPanel; parent=controlFrame; position=center");
      add("Panel", "name=controlPanel; parent=controlFrame; layout=border; position=south");
      add("Panel", "name=buttonPanel;position=south;parent=controlPanel;layout=flow");
      add("Button", "parent=buttonPanel; text=Start; action=control.runAnimation(); name=runButton");
      add("Button", "parent=buttonPanel; text=Step; action=stepAnimation()");
      add("Button", "parent=buttonPanel; text=Reset; action=control.resetModel()");
      add ("CheckBox","parent=buttonPanel;variable=showEnergy;text=E;selected=false;action=control.showEnergy();");
      model.setControl(this);
      model.reset();
      getMainFrame().setAnimated(true);      // the frame's render method will be called automatically
      getMainFrame().setVisible(true);
      addPropertyChangeListener(model);  // loading an XML data file will a fire property change event
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
         model.startSimulation();
      }
   }

   public void resetModel(){
      if (model.isRunning()){
         model.stopSimulation();
         getControl("runButton").setProperty("text", "Start");
      }
      model.reset();
      model.energyFrame.clearDataAndRepaint();
      drawingPanel.repaint();
   }

   public void showEnergy(){
      model.energyFrame.setVisible(getBoolean("showEnergy"));
   }

    public static void main(String[] args) {
       KeplerWRApp app = new KeplerWRApp((new KeplerApp()));
       app.loadXML(args);
       app.model.initialize();
    }

}
