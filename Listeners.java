import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

@SuppressWarnings("unchecked")
class Listeners {
   private static final Demo CANVAS = Demo.getInstance();
   protected static final int FRAMES = 25, INTERVAL = 30, FPS = 140;
   protected static Demo initializeCanvas() {
      CANVAS.addComponentListener(new ComponentListener() {
         @Override
         public void componentResized(ComponentEvent e) {
            CANVAS.updateSizeFields();
         }
         @Override
         public void componentMoved(ComponentEvent e) {}
         @Override
         public void componentShown(ComponentEvent e) {}
         @Override
         public void componentHidden(ComponentEvent e) {}
      });
      CANVAS.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseDragged(MouseEvent e) {
            CANVAS.screenPositionToAngles(e.getX(), e.getY());
         }
         @Override
         public void mouseMoved(MouseEvent e) {}
      });
      CANVAS.addMouseWheelListener(e -> CANVAS.incrementI(e.getWheelRotation() << 3));
      
      new Timer(1000/FPS, e -> CANVAS.repaint()).start();
      
      return CANVAS;
   }

   protected static ActionListener createToggleShapesListener() {
      return new ActionListener() {
         private boolean isVectorsVisible = true;
         private Object[] vectorsBackup;
         private Object[][] shapesBackup;    

         @Override
         public void actionPerformed(ActionEvent e) {
            if (isVectorsVisible) {
               shapesBackup = CANVAS._shapes;
               CANVAS.setVectors((V3[])vectorsBackup);
               vectorsBackup = null;
               CANVAS.setShapes(null);
               CANVAS.updateVectors();
               Window.appendVectors();
            } else {
               vectorsBackup = CANVAS._vectors;
               CANVAS._shapes = (V3[][]) shapesBackup;
               shapesBackup = null;
               CANVAS.setVectors(null);
               CANVAS.updateShapes();
               Window.appendShapes();
            }
            isVectorsVisible = !isVectorsVisible;
         }
      };
   }
   
   
   @SuppressWarnings("rawtypes")
   protected static ListSelectionListener createListSelectionListener() {
      return e -> {
         if (!e.getValueIsAdjusting()) {
            Vector selected = new Vector<>();
            Window.list.getSelectedValuesList().forEach(selected::add);
            if(!selected.isEmpty() && selected.firstElement() instanceof V3) {
               CANVAS.setVectors((V3[]) selected.toArray(new V3[0]));
            } else {
               CANVAS.setShapes((V3[][])selected.toArray(new V3[0][]));
            }
         }
      };
   }

   protected static ActionListener createInsertVectorListener() {
      return e -> {
         String x = Window._x.getText(),
            y = Window._y.getText(),
            z = Window._z.getText();
         if (validateInput(x) && validateInput(y) && validateInput(z)) {
            Window.vectors.add(new V3(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z)));
            Window.appendVectors();
            Window._x.setText("");
            Window._y.setText("");
            Window._z.setText("");
         }
      };
   }

   protected static ActionListener createTranslateListener() {
      return e -> {
         String x = Window._x.getText(), y = Window._y.getText(), z = Window._z.getText();
         double dx = validateInput(x) ? Double.parseDouble(x) : 0,
            dy = validateInput(y) ? Double.parseDouble(y) : 0,
            dz = validateInput(z) ? Double.parseDouble(z) : 0;
         if (CANVAS._vectors != null) {
            applyTranslations(CANVAS._vectors, dx, dy, dz);
         }
         if(CANVAS._shapes != null) {
            for (V3[] shape : CANVAS._shapes) {
               applyTranslations(shape, dx, dy, dz);
            }
         }
      };
   }

   protected static ActionListener createScaleListener() {
      return e -> {
         String input = Window._x.getText();
         if(!validateInput(input)) {
            return;
         }
         double f = Double.parseDouble(input);

         if(CANVAS._vectors != null) {
            applyScales(CANVAS._vectors, f);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyScales(aux, f);
            }
         }
      };
   }

   protected static ActionListener createProjXYListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyProjections(CANVAS._vectors, Utils.projXY);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyProjections(aux, Utils.projXY);
            }
         }
      };
   }

   protected static ActionListener createProjXZListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyProjections(CANVAS._vectors, Utils.projXZ);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyProjections(aux, Utils.projXZ);
            }
         }
      };
   }

   protected static ActionListener createProjYZListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyProjections(CANVAS._vectors, Utils.projYZ);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyProjections(aux, Utils.projYZ);
            }
         }
      };
   }

   protected static ActionListener createRefXListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyReflections(CANVAS._vectors, Utils.refX);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyReflections(aux, Utils.refX);
            }
         }
      };
   }

   protected static ActionListener createRefYListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyReflections(CANVAS._vectors, Utils.refY);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyReflections(aux, Utils.refY);
            }
         }
      };
   }

   protected static ActionListener createRefZListener() {
      return e -> {
         if(CANVAS._vectors != null) {
            applyReflections(CANVAS._vectors, Utils.refZ);
         } else if(CANVAS._shapes != null){
            for (V3[] aux : CANVAS._shapes) {
               applyReflections(aux, Utils.refZ);
            }
         }
      };
   }

   protected static ActionListener createRotateListener(){
      return new ActionListener() {
         Timer idle= new Timer(INTERVAL, new ActionListener() {
            int curr = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
               CANVAS.setAngleZ(curr++ * 0.04);
               CANVAS.updateVectors();
               CANVAS.updateShapes();
               CANVAS.updateGridLines();
            }
         });
         boolean flag = true;

         @Override
         public void actionPerformed(ActionEvent e) {
            String x = Window._x.getText(), y = Window._y.getText(), z = Window._z.getText();
            double angleX = validateInput(x) ? Math.toRadians(Double.parseDouble(x)) : 0,
               angleY = validateInput(y) ? Math.toRadians(Double.parseDouble(y)) : 0,
               angleZ = validateInput(z) ? Math.toRadians(Double.parseDouble(z)) : 0;

            if(angleX == angleY && angleY == angleZ && angleZ == 0) {
               if(flag){
                  idle.start();
               } else {
                  idle.stop();
               }
               flag = !flag;
            } else if(CANVAS._vectors != null) {
               applyRotations(CANVAS._vectors, angleX, angleY, angleZ);
            } else if(CANVAS._shapes != null) {
               for (V3[] aux : CANVAS._shapes) {
                  applyRotations(aux, angleX, angleY, angleZ);
               }
            }
         }
      };
   }
   protected static ActionListener createShearOnXListener() {
      return e -> {
         String z = Window._z.getText(), y = Window._y.getText();
         double factorZ = validateInput(z) ? Double.parseDouble(z) : 0,
            factorY = validateInput(y) ? Double.parseDouble(y) : 0;
         
         if(CANVAS._vectors != null) {
            V3[] aux = CANVAS._vectors;
            applyShears(aux, Utils.shearOnX, factorY, factorZ);
         } else {
            for (V3[] aux : CANVAS._shapes) {
               applyShears(aux, Utils.shearOnX, factorY, factorZ);
            }
         }
      };
   }
   protected static ActionListener createShearOnYListener() {
      return e -> {
         String x = Window._x.getText(), z = Window._z.getText();
         double factorX = validateInput(x) ? Double.parseDouble(x) : 0,
            factorZ = validateInput(z) ? Double.parseDouble(z) : 0;
         
         if(CANVAS._vectors != null) {
            V3[] aux = CANVAS._vectors;
            applyShears(aux, Utils.shearOnY, factorX, factorZ);
         } else {
            for (V3[] aux : CANVAS._shapes) {
               applyShears(aux, Utils.shearOnY, factorX, factorZ);
            }
         }
      };
   }
   
   protected static ActionListener createShearOnZListener() {
      return e -> {
         String x = Window._x.getText(), y = Window._y.getText();
         double factorX = validateInput(x) ? Double.parseDouble(x) : 0,
            factorY = validateInput(y) ? Double.parseDouble(y) : 0;
         
         if(CANVAS._vectors != null) {
            V3[] aux = CANVAS._vectors;
            applyShears(aux, Utils.shearOnZ, factorX, factorY);
         } else {
            for (V3[] aux : CANVAS._shapes) {
               applyShears(aux, Utils.shearOnZ, factorX, factorY);
            }
         }
      };
   }

   protected static ActionListener createSphereListener() {
      return e -> {
         Window.shapes.add(Shape.SPHERE.getVectors());
         Window.appendShapes();  
      };
   }

   protected static ActionListener createCubeListener() {
      return e -> {
         Window.shapes.add(Shape.CUBE.getVectors());
         Window.appendShapes();  
      };
   }

   protected static ActionListener createPyramidListener() {
      return e -> {
         Window.shapes.add(Shape.PYRAMID.getVectors());
         Window.appendShapes();
      };
   }

   private static void applyTranslations(V3[] vectors, double dx, double dy, double dz) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         int curr = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            applyTranslation(vectors, copy, dx / FRAMES * curr, dy / FRAMES * curr,dz / FRAMES * curr);
            if (curr == FRAMES) {
               ((Timer) e.getSource()).stop();     
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
            } else {
               curr++;
            }
         }
      }).start();
   }

   private static void applyTranslation(V3[] vectors, V3[] copy, double dx, double dy, double dz) {
      for (int i = 0; i < copy.length; i++) {
         vectors[i].x = copy[i].x + dx;
         vectors[i].y = copy[i].y + dy;
         vectors[i].z = copy[i].z + dz;
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }

   private static void applyScales(V3[] vectors, double f) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         int curr = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            applyScale(vectors, copy, f, curr);
            if (curr == FRAMES) {
            	((Timer) e.getSource()).stop();
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
            } else {
               curr++;
            }
         }
      }).start();
   }

   private static void applyScale(V3[] vectors, V3[] original, double factor, int frame) {
      for (int i = 0; i < vectors.length; i++) {
         V3 scaled = Utils.scale.apply(original[i], factor);
         vectors[i].x = original[i].x + (scaled.x - original[i].x) / FRAMES * frame;
         vectors[i].y = original[i].y + (scaled.y - original[i].y) / FRAMES * frame;
         vectors[i].z = original[i].z + (scaled.z - original[i].z) / FRAMES * frame;
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }

   private static void applyProjections(V3[] vectors, TransformFunction transform) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         double curr = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            applyProjection(vectors, copy, transform, 1 - curr/FRAMES);
            if (curr == FRAMES) {
               ((Timer) e.getSource()).stop();     
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
            } else {
               curr++;
            }
         }
      }).start();
   }

   private static void applyProjection(V3[] vectors, V3[] copy, TransformFunction transform, double f) {
      for(int i = 0; i < copy.length; i++) {
         vectors[i] = transform.apply(copy[i], f);
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }

   private static void applyReflections(V3[] vectors, TransformFunction transform) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         double curr = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            applyReflection(vectors, copy, transform, 1 - curr/FRAMES);
            if (curr == FRAMES << 1) {
               ((Timer) e.getSource()).stop();     
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
            } else {
               curr+=2;
            }
         }
      }).start();
   }

   private static void applyReflection(V3[] vectors, V3[] copy, TransformFunction transform, double f) {
      for(int i = 0; i < copy.length; i++) {
         vectors[i] = transform.apply(copy[i], f);
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }

   private static void applyRotations(V3[] vectors, double ax, double ay, double az) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         int curr = 0;
         @Override
         public void actionPerformed(ActionEvent e) {
            if (curr <= FRAMES) {
               if (ax == 0) {
                  curr += FRAMES;
               } else 
               applyRotation(vectors, copy, Utils.rotX, ax / FRAMES * curr);
            } else if (curr <= FRAMES << 1) {
               if (ay == 0) {
                  curr += FRAMES;
               } else 
               applyRotation(vectors, copy, Utils.rotY, ay / FRAMES * (curr - FRAMES));
            } else {
               if (az == 0) {
                  curr += FRAMES;
               } else
               applyRotation(vectors, copy, Utils.rotZ, az / FRAMES *(curr - (FRAMES << 1)));
            }
            if (curr == FRAMES * 3) {
               ((Timer) e.getSource()).stop();     
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
               
            } else {
               curr++;
            }
         }
      }).start();
   }
   
   private static void applyRotation(V3[] vectors, V3[] original, TransformFunction rotation, double angle) {
      for (int i = 0; i < vectors.length; i++) {
         vectors[i] = rotation.apply(original[i], angle);
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }

   private static void applyShears(V3[] vectors, TransformFunction transform, double s, double t) {
      V3[] copy = copyVectors(vectors);
      new Timer(INTERVAL, new ActionListener() {
         int curr = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            applyShear(vectors, copy, transform, s / FRAMES * curr, t / FRAMES * curr);

            if (curr == FRAMES) {
               ((Timer) e.getSource()).stop();     
               if(vectors == CANVAS._vectors) updateWindowVectors(vectors);
            } else {
               curr++;
            }
         }
      }).start();
   }

   private static void applyShear(V3[] vectors, V3[] original, TransformFunction transform, double s, double t) {
      for (int i = 0; i < vectors.length; i++) {
         vectors[i] = transform.apply(original[i], s, t);
      }
      if(vectors == CANVAS._vectors) {
         CANVAS.updateVectors();
      } else {
         CANVAS.updateShapes();
      }
   }
   
   private static boolean validateInput(String in) {
      if (in.isEmpty()) {
         return false;
      }
      boolean hasDecimalPoint = false;
      char[] c = in.toCharArray();
      for (int i = 0; i < c.length - 1; i++) {
         if ((c[i] == '-' && i != 0) && c[i] != '.' && c[i] < 48 || c[i] > 57) {
            return false;
         }
         if(c[i] == '.'){
            if(hasDecimalPoint) {
               return false;
            }
            hasDecimalPoint = true;
         }
      }
      return c[c.length-1] > 47 && c[c.length-1] < 58;
   }

   

   private static void updateWindowVectors(V3[] vectors) {
      if(!Window.vectors.isEmpty()) {
         int[] selectedIndices = Window.list.getSelectedIndices();
         for (int i = 0; i < selectedIndices.length; i++) {
            Window.vectors.set(selectedIndices[i], vectors[i]);
         }
         Window.appendVectors();
         Window.list.setSelectedIndices(selectedIndices);
      }
   }

   private static V3[] copyVectors(V3[] vectors) {
      V3[] copy = new V3[vectors.length];
      for (int i = 0; i < vectors.length; i++) {
         copy[i] = new V3(vectors[i].x, vectors[i].y, vectors[i].z);
      }
      return copy;
   }
}
