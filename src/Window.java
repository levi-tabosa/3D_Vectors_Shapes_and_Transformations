import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

@SuppressWarnings("rawtypes")
/**
 * This class represents the main window of the application.
 * It is responsible for creating and managing the GUI components, including:
 * - Input fields for vector coordinates and transformation parameters.
 * - Buttons for triggering various transformations and operations.
 * - A JList to display and select vectors and shapes.
 * - A JSplitPane to divide the window between the canvas and the control panel.
 */
class Window extends JFrame {
   // GUI components for user input
   public static JTextField _x, _y, _z; // Input fields for vector coordinates
   public static JSplitPane splitPane; // Split pane to separate canvas and controls
   public static JList list; // JList to display and select vectors/shapes
   
   // Data structures for storing vectors and shapes
   public static Vector vectors = new Vector<>(), shapes = new Vector<>();

   /**
    * Initializes and displays the main application window.
    * Creates and arranges the GUI components, and sets up listeners.
    */
   public static void initGUI() {
       // Create main window instance and set up its properties
       Window wd = new Window();
       wd.setLayout(new BorderLayout());
       wd.setDefaultCloseOperation(EXIT_ON_CLOSE);

       // Create and configure the split pane for canvas and menu
       splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createMenuPanel(), createCanvasPanel());
       splitPane.setOneTouchExpandable(true);
       wd.add(splitPane, BorderLayout.CENTER);

       // Add the panel for vector insertion at the bottom
       wd.add(createInsertPanel(), BorderLayout.SOUTH);

       // Set window size and make it visible
       wd.setSize(800, 600);
       wd.setVisible(true);
   }

   /**
    * Creates and returns a JPanel containing the canvas.
    * 
    * @return JPanel containing the initialized canvas.
    */
   private static JPanel createCanvasPanel() {
       JPanel panel = new JPanel(new BorderLayout());
       panel.add(Listeners.initializeCanvas()); // Add the canvas to the panel
       return panel;
   }

   /**
    * Creates and returns a JPanel containing the menu panel.
    * 
    * @return JPanel containing the menu components.
    */
   private static JPanel createMenuPanel() {
       JPanel panel = new JPanel(new BorderLayout());

       // Create and configure JList for displaying vectors/shapes
       list = new JList<>();
       list.addListSelectionListener(Listeners.createListSelectionListener());

       // Add input panel and JList to the menu panel
       panel.add(createInputPanel(), BorderLayout.SOUTH);
       panel.add(new JScrollPane(list), BorderLayout.CENTER);
       return panel;
   }

   /**
    * Creates and returns a JPanel containing transformation buttons.
    * 
    * @return JPanel containing transformation control buttons.
    */
   private static JPanel createInputPanel() {
      // Create buttons for various transformations and operations
      JButton collapse = new JButton("Collapse"), scale = new JButton("Scale"),
            translate = new JButton("Translate"), rotate = new JButton("Rotate"),
            toggleShapes = new JButton("Toggle Mode"), tsa = new JButton("TSA"),
            shearZ = new JButton("Shear (Z)"), pyramid = new JButton("Pyramid"),
            shearX = new JButton("Shear (X)"), shearY = new JButton("Shear (Y)"),
            projXY = new JButton("Projection XY"), projXZ = new JButton("Projection XZ"),
            projYZ = new JButton("Projection YZ"), refX = new JButton("Reflection X"),
            refY = new JButton("Reflection Y"), refZ = new JButton("Reflection Z"),
            sphere = new JButton("Sphere"), cube = new JButton("Cube");
      // Add action listeners to the buttons
      collapse.addActionListener(e -> splitPane.setDividerLocation(0));
      rotate.addActionListener(Listeners.createRotateListener());
      scale.addActionListener(Listeners.createScaleListener());
      translate.addActionListener(Listeners.createTranslateListener());
      toggleShapes.addActionListener(Listeners.createToggleShapesListener());
      tsa.addActionListener(Listeners.createTravellingSalesmanListener());
      shearX.addActionListener(Listeners.createShearOnXListener());
      shearY.addActionListener(Listeners.createShearOnYListener());
      shearZ.addActionListener(Listeners.createShearOnZListener());
      projXY.addActionListener(Listeners.createProjXYListener());
      projXZ.addActionListener(Listeners.createProjXZListener());
      projYZ.addActionListener(Listeners.createProjYZListener());
      refX.addActionListener(Listeners.createRefXListener());
      refY.addActionListener(Listeners.createRefYListener());
      refZ.addActionListener(Listeners.createRefZListener());
      sphere.addActionListener(Listeners.createSphereListener());
      cube.addActionListener(Listeners.createCubeListener());
      pyramid.addActionListener(Listeners.createPyramidListener());
      // Create a panel to hold the buttons and arrange them in a grid layout
      JPanel panel = new JPanel(new GridLayout(6, 3));
      panel.add(tsa);
      panel.add(toggleShapes);
      panel.add(scale);
      panel.add(translate);
      panel.add(shearX);
      panel.add(shearY);
      panel.add(shearZ);
      panel.add(projXY);
      panel.add(projXZ);
      panel.add(projYZ);
      panel.add(refX);
      panel.add(refY);
      panel.add(refZ);
      panel.add(rotate);
      panel.add(sphere);
      panel.add(cube);
      panel.add(pyramid);
      panel.add(collapse);
      return panel;
   }

   /**
    * Creates and returns a JPanel containing input fields and buttons for vector insertion.
    * 
    * @return JPanel containing components for inserting vectors.
    */
   private static JPanel createInsertPanel() {
       // Create input fields for vector components
       _x = new JTextField(2);
       _y = new JTextField(2);
       _z = new JTextField(2);

       // Create buttons for inserting and clearing vectors
       JButton insert = new JButton("Insert"),
            clear = new JButton("Clear");

       // Create a panel and arrange input fields and buttons using FlowLayout
       JPanel panel = new JPanel(new FlowLayout());
       panel.add(_x);
       panel.add(_y);
       panel.add(_z);
       panel.add(insert);
       panel.add(clear);

       // Add action listeners to the buttons
       insert.addActionListener(Listeners.createInsertVectorListener());
       clear.addActionListener(e -> vectors.clear());

       return panel;
   }

   /**
    * Updates the JList to display the current list of vectors.
    */
   @SuppressWarnings("unchecked")
   public static void appendVectors() {
       // Create a new DefaultListModel and populate it with vectors
       DefaultListModel model = new DefaultListModel<>();
       model.addAll(vectors);
       
       // Set the model of the JList to the newly created model
       list.setModel(model);
   }

   /**
    * Updates the JList to display the current list of shapes.
    */
   @SuppressWarnings("unchecked")
   public static void appendShapes() {
       // Create a new DefaultListModel and populate it with shapes
       DefaultListModel model = new DefaultListModel<>();
       model.addAll(shapes);

       // Set the model of the JList to the newly created model
       list.setModel(model);
   }
}
