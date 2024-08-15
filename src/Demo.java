import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

class Demo extends JComponent {
   private int _i = 80, _H = 1, _W = 1, gridRes = 300; // TODO: gridRes from user input, toggle perspective projection
   private final double far = gridRes << 2, near = gridRes >> 1; // near and far distances for perspective projection
   public V3[] _vectors, vectors; // V3 arrays storing point vectors inserted by user
   private V3[] _gridLines = new V3[gridRes << 2], gridLines = new V3[gridRes << 2]; // V3 lines arrays storing start and end points to render lines
   private V3[] _lines, lines;
   public V3[][] _shapes, shapes;
   private double angleZ = 0, angleX = 0;
   // arrays followed by _ store original values while their counterpart stores rotated values based on camera angle used by render methods to perform rotations only when camera moves 

   private static Demo instance; // singleton instance

   private Graphics graphics; // components for double buffering
   private Image image;       // --

   public static Demo getInstance() {
      return instance != null ? instance : new Demo();
   }

   private Demo() {
      super();
      int j = gridRes >> 1;

      for (int i = -j; i < j; i++) {
         gridLines[i + j << 2] = new V3(i, j, 0); //
         gridLines[(i + j << 2) + 1] = new V3(i, -j, 0);
         gridLines[(i + j << 2) + 2] = new V3(j, i, 0);
         gridLines[(i + j << 2) + 3] = new V3(-j, i, 0);
         _gridLines[i + j << 2] = new V3(i, j, 0);
         _gridLines[(i + j << 2) + 1] = new V3(i, -j, 0);
         _gridLines[(i + j << 2) + 2] = new V3(j, i, 0);
         _gridLines[(i + j << 2) + 3] = new V3(-j, i, 0);
      }
      lines = new V3[] {
            new V3(j, 0, 0), new V3(-j, 0, 0), new V3(0, j, 0),
            new V3(0, -j, 0), new V3(0, 0, j), new V3(0, 0, -j)
      };
      _lines = new V3[] {
            new V3(j, 0, 0), new V3(-j, 0, 0), new V3(0, j, 0),
            new V3(0, -j, 0), new V3(0, 0, j), new V3(0, 0, -j)
      };
   }

   private void drawFrame(Graphics g) {
      if (image == null || image.getWidth(null) != _W || image.getHeight(null) != _H) {
         image = createImage(_W, _H);
         graphics = image.getGraphics();
      }
      drawLines(graphics);
      drawShapes(graphics);
      drawVector(graphics);
      g.drawImage(image, 0, 0, null);
      graphics.clearRect(0, 0, _W, _H);
   }

   private void drawShapes(Graphics g) {
      if (shapes != null) {
         g.setColor(Color.PINK);
         for (int i = 0; i < _shapes.length; i++) {
            int n = _shapes[i].length;
            int[][] points = new int[n][2];
            for (int j = 0; j < n; j++) {
               points[j][0] = (int) ((_W >> 1) + (shapes[i][j].x * near / (shapes[i][j].y + far)) * _i);
               points[j][1] = (int) ((_H >> 1) + (shapes[i][j].z * near / (shapes[i][j].y + far)) * _i);
            }
            for (int j = 0; j < n - 1; j++) {
               g.setColor(Color.YELLOW);
               g.drawLine(points[j][0], points[j][1], points[j + 1][0], points[j + 1][1]);
               // g.setColor(Color.PINK); uncomment line for tsa path func
               g.drawLine(points[j][0], points[j][1], points[(j + 3) % n][0], points[(j + 3) % n][1]); // draw cube ?
            }
            g.drawLine(points[n - 1][0], points[n - 1][1], points[0][0], points[0][1]);
         }
      }
   }

   private void drawVector(Graphics g) {
      if (_vectors != null) {
         g.setColor(Color.PINK);
         for (int i = 0; i < _vectors.length; i++) {
            int px = (int) ((_W >> 1) + (vectors[i].x * near / (vectors[i].y + far)) * _i);
            int py = (int) ((_H >> 1) + (vectors[i].z * near / (vectors[i].y + far)) * _i);
            g.drawString(i + "", px, py);
            g.drawLine(_W >> 1, _H >> 1, px, py);
            g.drawString(_vectors[i] + "", px - 10, py - 10);
         }
      }
   }

   private void drawLines(Graphics g) {
      int center_x = _W >> 1, center_y = _H >> 1;

      g.setColor(Color.BLACK);
      g.fillRect(0, 0, _W, _H);
      for (int i = 0; i < 3; i++) { // shifts i - draws lines from v3 in i to v3 in i+1
         g.setColor(new Color(255 - i * 100, i * 110, 22 << i));
         g.drawLine(
               (int) (center_x + (lines[i << 1].x * near / (lines[i << 1].y + far)) * _i),
               (int) (center_y + (lines[i << 1].z * near / (lines[i << 1].y + far)) * _i),
               (int) (center_x + (lines[(i << 1) + 1].x * near / (lines[(i << 1) + 1].y + far)) * _i),
               (int) (center_y + (lines[(i << 1) + 1].z * near / (lines[(i << 1) + 1].y + far)) * _i));
         for (int j = 0; j <= gridRes; j++) { // calculates interpolation between initial x, y to final x, y based on
                                              // perspective
            double factor = j / (double) (gridRes);
            double interpX = lines[i << 1].x + factor * (lines[(i << 1) + 1].x - lines[i << 1].x);
            double interpY = lines[i << 1].y + factor * (lines[(i << 1) + 1].y - lines[i << 1].y);
            double interpZ = lines[i << 1].z + factor * (lines[(i << 1) + 1].z - lines[i << 1].z);
            int screenX = (int) (center_x + (interpX * near / (interpY + far)) * _i);
            int screenY = (int) (center_y + (interpZ * near / (interpY + far)) * _i);
            g.drawString(j - (gridRes >> 1) + "", screenX, screenY); // draws unit label
         }
      }
      g.setColor(new Color(90, 90, 90, 120));
      for (int i = 0; i < gridRes; i++) {
         g.drawLine( // draws grid y lines
               (int) (center_x + (gridLines[i << 2].x * near / (gridLines[i << 2].y + far)) * _i),
               (int) (center_y + (gridLines[i << 2].z * near / (gridLines[i << 2].y + far)) * _i),
               (int) (center_x + (gridLines[(i << 2) + 1].x * near / (gridLines[(i << 2) + 1].y + far)) * _i),
               (int) (center_y + (gridLines[(i << 2) + 1].z * near / (gridLines[(i << 2) + 1].y + far)) * _i));
         g.drawLine(// draws grid x lines
               (int) (center_x + (gridLines[(i << 2) + 2].x * near / (gridLines[(i << 2) + 2].y + far)) * _i),
               (int) (center_y + (gridLines[(i << 2) + 2].z * near / (gridLines[(i << 2) + 2].y + far)) * _i),
               (int) (center_x + (gridLines[(i << 2) + 3].x * near / (gridLines[(i << 2) + 3].y + far)) * _i),
               (int) (center_y + (gridLines[(i << 2) + 3].z * near / (gridLines[(i << 2) + 3].y + far)) * _i));
      }
   }

   public void updateSizeFields() {
      _W = getWidth();
      _H = getHeight();
   }

   public void updateGridLines() {
      for (int i = 0; i < 6; i++) {
         lines[i] = Utils.rotZX.apply(_lines[i], angleZ, angleX);
      }
      for (int i = 0; i < gridRes << 1; i++) {
         gridLines[i << 1] = Utils.rotZX.apply(_gridLines[i << 1], angleZ, angleX);
         gridLines[(i << 1) + 1] = Utils.rotZX.apply(_gridLines[(i << 1) + 1], angleZ, angleX);
      }
   }

   public void setAngleZ(double angleZ) {
      this.angleZ = angleZ;
   }

   public void setAngleX(double angleX) {
      this.angleX = angleX;
   }

   public void screenPositionToAngles(int x, int y) {
      setAngleZ(x * 6.283185 / _W);
      setAngleX(y * 6.283185 / _H);
      updateGridLines();
      updateShapes();
      updateVectors();
   }

   public void setVectors(V3[] vectors) {
      _vectors = vectors;
      updateVectors();
   }

   public void setShapes(V3[][] shapes) {
      _shapes = shapes;
      updateShapes();
   }

   public void updateVectors() {
      if (_vectors != null) {
         vectors = new V3[_vectors.length];
         if (vectors == null) {
            vectors = new V3[_vectors.length];
         }
         for (int i = 0; i < _vectors.length; i++) {
            vectors[i] = Utils.rotZX.apply(_vectors[i], angleZ, angleX);
         }
      }
   }

   public void updateShapes() {
      if (_shapes != null) {
         shapes = new V3[_shapes.length][];
         for (int i = 0; i < _shapes.length; i++) {
            if (shapes[i] == null) {
               shapes[i] = new V3[_shapes[i].length];
            }
            for (int j = 0; j < _shapes[i].length; j++) {
               shapes[i][j] = Utils.rotZX.apply(_shapes[i][j], angleZ, angleX);
            }
         }
      } else {
         shapes = null;
      }
   }

   public void incrementI(int amount) {
      _i += amount;
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawFrame(g);
   }
}

enum Shape {
   CUBE {
      @Override
      public V3[] getVectors() {
         V3[] vectors = {
               new V3(-1, 1, 1), new V3(-1, 1, -1), new V3(1, 1, -1), new V3(1, 1, 1),
               new V3(1, -1, 1), new V3(1, -1, -1), new V3(-1, -1, -1), new V3(-1, -1, 1)
         };
         return vectors;
      }
   },
   PYRAMID {
      @Override
      public V3[] getVectors() {
         V3[] vectors = {
               new V3(0, 0, 1), new V3(-1, 1, -1), new V3(1, 1, -1),
               new V3(1, -1, -1), new V3(-1, -1, -1)
         };
         return vectors;
      }
   },
   SPHERE(96) {
      @Override
      public V3[] getVectors() {
         V3 aux = new V3(1, 0, 0);
         V3[] vectors = new V3[res * res];
         for (int i = 0; i < res; i++) {
            aux = Utils.rotY.apply(aux, i * (2 * Math.PI / res));
            for (int j = 0; j < res; j++) {
               vectors[i * res + j] = Utils.rotX.apply(aux, j * (2 * Math.PI / res));
            }
         }
         return vectors;
      }
   };

   Shape() {
   }

   Shape(int res) {
      this.res = res;
   };

   public int res;
   public abstract V3[] getVectors();
}