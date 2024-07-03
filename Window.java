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
import javax.swing.SwingUtilities;

@SuppressWarnings("rawtypes")
class Window extends JFrame {
    public static JTextField _x, _y, _z;
    public static JSplitPane splitPane;
    public static JList list;
    public static Vector vectors = new Vector<>(), shapes = new Vector<>();

    public static void initGUI() {
        Window wd = new Window();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createMenuPanel(), createCanvasPanel());
        splitPane.setOneTouchExpandable(true);
        wd.setLayout(new BorderLayout());
        wd.setDefaultCloseOperation(EXIT_ON_CLOSE);
        wd.add(splitPane, BorderLayout.CENTER);
        wd.add(createInsertPanel(), BorderLayout.SOUTH);
        wd.setSize(800, 600);
        wd.setVisible(true);
    }

    private static JPanel createCanvasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(Listeners.initializeCanvas());
        return panel;
    }

    private static JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        list = new JList<>();
        list.addListSelectionListener(Listeners.createListSelectionListener());
        panel.add(createInputPanel(), BorderLayout.SOUTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createInputPanel() {
        JButton collapse = new JButton("Collapse"), scale = new JButton("Scale"),
                translate = new JButton("Translate"), rotate = new JButton("Rotate"),
                toggleShapes = new JButton("Toggle Mode"), shearZ = new JButton("Shear (Z)"),
                shearX = new JButton("Shear (X)"),shearY = new JButton("Shear (Y)"),
                projXY = new JButton("Projection XY"), projXZ = new JButton("Projection XZ"),
                projYZ = new JButton("Projection YZ"), refX = new JButton("Reflection X"), 
                refY = new JButton("Reflection Y"), refZ = new JButton("Reflection Z"),
                sphere = new JButton("Sphere"), cube = new JButton("Cube"), pyramid = new JButton("Pyramid");

        collapse.addActionListener(e -> splitPane.setDividerLocation(0));
        rotate.addActionListener(Listeners.createRotateListener());
        scale.addActionListener(Listeners.createScaleListener());
        translate.addActionListener(Listeners.createTranslateListener());
        toggleShapes.addActionListener(Listeners.createToggleShapesListener());
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

        JPanel panel = new JPanel(new GridLayout(6, 3));
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

    private static JPanel createInsertPanel() {
        _x = new JTextField(2);
        _y = new JTextField(2);
        _z = new JTextField(2);

        JButton insert = new JButton("Insert"),
                clear = new JButton("Clear");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(_x);
        panel.add(_y);
        panel.add(_z);
        panel.add(insert);
        panel.add(clear);
        insert.addActionListener(Listeners.createInsertVectorListener());
        clear.addActionListener(e -> vectors.clear());

        return panel;
    }

    @SuppressWarnings("unchecked")
    public static void appendVectors() {
        DefaultListModel model = new DefaultListModel<>();
        model.addAll(vectors);
        list.setModel(model);
    }

    @SuppressWarnings("unchecked")
    public static void appendShapes() {
        DefaultListModel model = new DefaultListModel<>();
        model.addAll(shapes);
        list.setModel(model);
    }
}
