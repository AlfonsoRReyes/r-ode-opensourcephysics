/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package org.opensourcephysics.manual.ch08;
import org.opensourcephysics.display.*;
import org.opensourcephysics.display2d.CellLattice;

/**
 * CellLatticeApp tests the CellLattice using the default color palette.
 *
 * @author Wolfgang Christian
 * @version 1.0
 */
public class CellLatticeApp {
  static final int SIZE = 32;

  /**
   * The main method starts the Java application.
   *
   * @param args[]  the input parameters
   */
  public static void main(String[] args) {
    PlottingPanel panel = new PlottingPanel("x", "y", "Byte Lattice");
    DrawingFrame frame = new DrawingFrame(panel);
    CellLattice lattice = new CellLattice(SIZE, SIZE);
    byte[][] data = new byte[SIZE][SIZE];
    for(int iy = 0;iy<SIZE;iy++) {
      for(int ix = 0;ix<SIZE;ix++) {
        data[ix][iy] = (byte) (256*Math.random());
      }
    }
    lattice.setAll(data, -1, 1, 1, -1);
    lattice.setBlock(4, 8, new byte[12][9]); // sets a block of cells to new values
    panel.addDrawable(lattice);
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    lattice.showLegend();
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
 * For additional information and documentation on Open Source Physics,
 * please see <http://www.opensourcephysics.org/>.
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */
