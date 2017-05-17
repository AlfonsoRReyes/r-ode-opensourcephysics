package org.opensourcephysics.davidson.ode.flow;
import org.opensourcephysics.ejs.control.GroupControl;
import javax.swing.JDialog;

public class CatControl {

   private GroupControl control = new GroupControl();
   private JDialog dialog;
   FlowLineApp app;
   public CatControl(FlowLineApp app) {
      this.app = app;
      control = new GroupControl(app);
      dialog = (JDialog) control.add("Dialog", "name=controlWindow;title=Create Cat;visible=false;location=300,300;size=250,200").getComponent();
      control.addTarget("control", this);
      control.add("Panel", "name=variablesPanel;position=north;layout=vbox;parent=controlWindow");
      control.add("Panel", "name=xPanel;position=left;layout=flow;parent=variablesPanel");
      control.add("Label", "parent=xPanel;text=cat x=;");
      control.add("NumberField", "parent=xPanel;variable=x;format= 00.00");
      control.add("Panel", "name=yPanel;position=left;layout=flow;parent=variablesPanel");
      control.add("Label", "parent=yPanel;text=cat y=;");
      control.add("NumberField", "parent=yPanel;variable=y;format= 00.00");
      control.add("Panel", "name=rPanel;position=left;layout=flow;parent=variablesPanel");
      control.add("Label", "parent=rPanel;text=cat r=;");
      control.add("NumberField", "parent=rPanel;variable=r;format= 00.00");
      control.add("Panel", "name=nPanel;position=left;layout=flow;parent=variablesPanel");
      control.add("Label", "parent=nPanel;text=# states=;");
      control.add("NumberField", "parent=nPanel;variable=n;format=000");
      control.add("Panel", "name=buttonPanel;position=left;layout=flow;parent=variablesPanel");
      control.add("Button", "parent=buttonPanel;text=Create;action=control.createCat");
      control.add("Button", "parent=buttonPanel;text=Cancel;action=control.cancel");
   }

   public void setVisible(double x, double y, double r, int n) {
      control.setValue("x", x);
      control.setValue("y", y);
      control.setValue("r", r);
      control.setValue("n", n);
      dialog.setVisible(true);
   }

   public void createCat() {
      app.makeCat(control.getDouble("x"), control.getDouble("y"), control.getDouble("r"),control.getInt("n"));
      dialog.setVisible(false);
   }

   public void cancel() {}
}
