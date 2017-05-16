/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.opensourcephysics.manual.ch09;
import org.opensourcephysics.ode.*;

/**
 * Test program for the Radau5 class.
 */
public class RadauApp {
  public static void main(String[] args) {
    ODETest ode = new ODETest();
    Radau5 ode_solver = new Radau5(ode);
    ode_solver.setStepSize(0.02);
    ode_solver.setTolerance(1e-6);
    double time = 0;
    double[] s1 = ode.getState();
    while(time<25) {
      System.out.println("x1 = "+s1[0]+" \t t="+time+" \t  step size="+ode_solver.getStepSize());
      time += ode_solver.step();
    }
    System.out.println("rate evaluated #: "+ode.n);
  }
}

/* 
 * Open Source Physics software is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.

 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be released
 * under the GNU GPL license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307 USA
 * or view the license online at http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */
