import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

class Demo extends JComponent {
   private final double far = 40, near = 10;
   public int _i = 80, _H = 1, _W = 1;
   private double angleZ = 0, angleX = 0;
   public V3[] _vectors, vectors;
   public V3[][] _shapes, shapes;
   private V3[] lines = {
      new V3(10, 0, 0), new V3(-10, 0, 0), new V3(0, 10, 0),
      new V3(0, -10, 0), new V3(0, 0, 10), new V3(0, 0, -10)
   },
    _lines = {
         new V3(10, 0, 0), new V3(-10, 0, 0), new V3(0, 10, 0),
         new V3(0, -10, 0), new V3(0, 0, 10), new V3(0, 0, -10)
      };
   private final V3[] gridLines = new V3[84], _gridLines = new V3[84];
   private static Demo instance;
   private Image image;
   private Graphics graphics;

   public static Demo getInstance() {
      if (instance == null) {
         instance = new Demo();
      }
      return instance;
   }

   private Demo() {
      super();
      for (int i = -10; i <= 10; i++) {
         int idx = (i + 10) << 2;
         gridLines[idx] = new V3(i, 10, 0);
         gridLines[idx + 1] = new V3(i, -10, 0);
         gridLines[idx + 2] = new V3(10, i, 0);
         gridLines[idx + 3] = new V3(-10, i, 0);
         _gridLines[idx] = new V3(i, 10, 0);
         _gridLines[idx + 1] = new V3(i, -10, 0);
         _gridLines[idx + 2] = new V3(10, i, 0);
         _gridLines[idx + 3] = new V3(-10, i, 0);
      }
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

            for (int j = 0; j < n; j++) { // TODO: draw_edges (3-Regular graph)
               g.drawLine(points[j][0], points[j][1], points[(j + 1) % n][0], points[(j + 1) % n][1]);
               g.drawLine(points[j][0], points[j][1], points[(j + 3) % n][0], points[(j + 3) % n][1]);
            }
         }
      }
   }

   private void drawVector(Graphics g) {
      if (_vectors != null) {
         g.setColor(Color.PINK);
         for (int i = 0; i < _vectors.length; i++) {
            int px = (int) ((_W >> 1) + (vectors[i].x * near / (vectors[i].y + far)) * _i),
               py = (int) ((_H >> 1) + (vectors[i].z * near / (vectors[i].y + far)) * _i);
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
      for (int i = 0; i < 3; i++) {
         int px1 = (int) (center_x + (lines[i << 1].x * near / (lines[i << 1].y + far)) * _i),
            py1 = (int) (center_y + (lines[i << 1].z * near / (lines[i << 1].y + far)) * _i),
            px2 = (int) (center_x + (lines[(i << 1) + 1].x * near / (lines[(i << 1) + 1].y + far)) * _i),
            py2 = (int) (center_y + (lines[(i << 1) + 1].z * near / (lines[(i << 1) + 1].y + far)) * _i);
         g.setColor(new Color(255 - i * 100, i * 110, 22 << i << 1));
         g.drawLine(px1, py1, px2, py2);
         for (int j = -10; j <= 10; j++) {
            g.drawString(j + "", center_x + (px2 - center_x) / 10 * j, center_y + (py2 - center_y) / 10 * j);
         }
      }
      g.setColor(new Color(90, 90, 90, 120));
      for (int i = 0; i < 21; i++) {
         int px1 = (int) (center_x + (gridLines[i << 2].x * near / (gridLines[i << 2].y + far)) * _i),
            py1 = (int) (center_y + (gridLines[i << 2].z * near / (gridLines[i << 2].y + far)) * _i),
            px2 = (int) (center_x + (gridLines[(i << 2) + 1].x * near / (gridLines[(i << 2) + 1].y + far)) * _i),
            py2 = (int) (center_y + (gridLines[(i << 2) + 1].z * near / (gridLines[(i << 2) + 1].y + far)) * _i);
         g.drawLine(px1, py1, px2, py2);
         px1 = (int) (center_x + (gridLines[(i << 2) + 2].x * near / (gridLines[(i << 2) + 2].y + far)) * _i);
         py1 = (int) (center_y + (gridLines[(i << 2) + 2].z * near / (gridLines[(i << 2) + 2].y + far)) * _i);
         px2 = (int) (center_x + (gridLines[(i << 2) + 3].x * near / (gridLines[(i << 2) + 3].y + far)) * _i);
         py2 = (int) (center_y + (gridLines[(i << 2) + 3].z * near / (gridLines[(i << 2) + 3].y + far)) * _i);
         g.drawLine(px1, py1, px2, py2);
      }
   }

   public void updateSizeFields() {
      _W = getWidth();
      _H = getHeight();
   }

   public void updateGridLines() {
      for (int i = 0; i < 6; i++) {
         lines[i] = Utils.rotX.apply(Utils.rotZ.apply(_lines[i], angleZ), angleX);
      }
      for (int i = 0; i < 42; i++) {
         gridLines[i << 1] = Utils.rotX.apply(Utils.rotZ.apply(_gridLines[i << 1], angleZ), angleX);
         gridLines[(i << 1) + 1] = Utils.rotX.apply(Utils.rotZ.apply(_gridLines[(i << 1) + 1], angleZ), angleX);
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
            vectors[i] = Utils.rotX.apply(Utils.rotZ.apply(_vectors[i], angleZ), angleX);
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
               shapes[i][j] = Utils.rotX.apply(Utils.rotZ.apply(_shapes[i][j], angleZ), angleX);
            }
         }
      } else {
         shapes = null;
      }
   }

   public void incrementI(int i) {
      _i += i;
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
