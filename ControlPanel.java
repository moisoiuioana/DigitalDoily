import javax.naming.LimitExceededException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Creating a control panel which holds all buttons that control the actions on the drawing and gallery panels
@SuppressWarnings({"unchecked", "unsafe"})
public class ControlPanel extends JPanel{

    private DrawPanel drawPanel;
    private GalleryPanel galleryPanel;
    private Double sectors;
    private Color penColor;
    private BasicStroke stroke;
    private java.util.List redoReady;
    private boolean toggle;
    private boolean reflection;
    private BufferedImage image;
    private boolean eraserOn;

    // Constructor
    public ControlPanel(){
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        createButtons();
        // Set a preferredSize for the panel so all buttons are shown
        setPreferredSize(new Dimension(800, 50));
        sectors = 6.00;
        // Default stroke
        stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // Hold all removed points in case if user chooses to redo
        redoReady = new ArrayList<List<MyPoints>>();
        // Show sector lines initially
        toggle = true;
        // Default reflection mode of point
        reflection = false;
        // Default pen color
        penColor = Color.black;
        // Disable eraser initially so the pen can be used upon opening the application
        eraserOn = false;
    }

    private void createButtons(){

        // Create buttons and add appropriate Listeners to each button, some JLabels were included to complete context meaning of some buttons

        // Choose pen
        JButton pen = new JButton("Pen");
        add(pen);
        AddPenListener addPenListener = new AddPenListener();
        pen.addActionListener(addPenListener);

        // Choose eraser
        JButton eraser  = new JButton("Eraser");
        add(eraser);
        AddEraserListener addEraserListener = new AddEraserListener();
        eraser.addActionListener(addEraserListener);

        // Choose pen color
        JButton penColor = new JButton("Change Pen Color");
        add(penColor);
        AddPenColorListener addPenColorListener = new AddPenColorListener();
        penColor.addActionListener(addPenColorListener);

        // Choose pen size
        JLabel penSizeLabel = new JLabel("Pen Size");
        add(penSizeLabel);

        SpinnerNumberModel model1 = new SpinnerNumberModel(5.0, 1.0, 20.0, 1.0);
        JSpinner penSize = new JSpinner(model1);
        add(penSize);
        AddPenSizeListener addPenSizeListener = new AddPenSizeListener();
        penSize.addChangeListener(addPenSizeListener);

        // Choose clear
        JButton clear = new JButton("Clear");
        add(clear);
        AddClearListener addClearListener = new AddClearListener();
        clear.addActionListener(addClearListener);

        // Choose undo
        JButton undo = new JButton("Undo");
        add(undo);
        AddUndoListener addUndoListener = new AddUndoListener();
        undo.addActionListener(addUndoListener);

        // Choose redo
        JButton redo = new JButton(("Redo"));
        add(redo);
        AddRedoListener addRedoListener = new AddRedoListener();
        redo.addActionListener(addRedoListener);

        // Choose number of sectors
        JLabel sectorLabel = new JLabel("Number of Sectors");
        add(sectorLabel);

        SpinnerNumberModel model2 = new SpinnerNumberModel(6.0, 1.0, 100.0, 1.0);
        JSpinner numberOfSectors = new JSpinner(model2);
        add(numberOfSectors);
        AddSectorListener addSectorListener = new AddSectorListener();
        numberOfSectors.addChangeListener(addSectorListener);

        // Choose to show or hide sector lines
        JButton toggleSectorLines = new JButton("Toggle Sector Lines");
        add(toggleSectorLines);
        AddToggleListener addToggleListener = new AddToggleListener();
        toggleSectorLines.addActionListener(addToggleListener);

        // Choose to reflect drawn points
        JCheckBox reflectionMode = new JCheckBox("Reflection Mode");
        add(reflectionMode);
        AddReflectionListener addReflectionListener = new AddReflectionListener();
        reflectionMode.addItemListener(addReflectionListener);

        // Choose to save the drawing
        JButton save = new JButton("Save");
        add(save);
        AddSaveListener addSaveListener = new AddSaveListener();
        save.addActionListener(addSaveListener);

    }

    // Getters and setters

    public void setDrawPanel(DrawPanel drawPanel){
        this.drawPanel = drawPanel;
    }

    public void setGalleryPanel(GalleryPanel galleryPanel) { this.galleryPanel = galleryPanel; }

    public Double getSectors(){
        return sectors;
    }

    public BasicStroke getStroke() { return stroke; }

    public Color getPenColor() { return  penColor; }

    public boolean getToggle() { return toggle; }

    public boolean getReflectionMode() { return reflection; }

    public boolean getEraserMode() { return eraserOn; }

    // Create classes for each implemented Listener

    // If user chooses to draw with pen turn off eraser
    private class AddPenListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            eraserOn=false;
        }
    }

    // If user chooses to use the eraser turn on the eraser
    private class AddEraserListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            eraserOn=true;
        }
    }

    // Change the color of the pen
    private class AddPenColorListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            // Original color of the pen is black
            penColor = Color.BLACK;
            // Assign the selected color to the pen color
            penColor = JColorChooser.showDialog( drawPanel, "Choose Pen Color", penColor);

            // If no new color is selected then set it to the original color
            if(penColor == null){
                penColor = Color.BLACK;
            }
        }
    }

    // Change the pen size
    private class AddPenSizeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent event){
            JSpinner source = (JSpinner) event.getSource();
            Number number = (Number)source.getValue();
            stroke = new BasicStroke(number.floatValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }
    }

    // Clear drawing
    private class AddClearListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            // Remove all points to the array holding all drawn points
            drawPanel.getPointsArray().clear();
            drawPanel.drawPanelRepaint();
        }
    }

    // Undo a draw
    @SuppressWarnings("unchecked")
    private class AddUndoListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            // If nothing was drawn previously catch exception
            try {
                // Add the point(s) drawn to the redoReady so they are ready for a possible redo of the action
                redoReady.add(drawPanel.getPointsArray().get(drawPanel.getPointsSize() - 1));
                // Remove the point(s) from the points array so it/they disappears(s) from the drawing panel
                drawPanel.getPointsArray().remove(drawPanel.getPointsArray().size() - 1);
                drawPanel.drawPanelRepaint();
            }catch(ArrayIndexOutOfBoundsException exception){
                System.out.println(exception + ": Nothing has been drawn");
            }
        }
    }

    private class AddRedoListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            // If nothing has been undone catch exception
            try {
                // Add the last point(s) undone to the points array ready for drawing on drawing panel
                drawPanel.getPointsArray().add(redoReady.get(redoReady.size() - 1));
                // Remove the point(s) from the redoReady so the next draw action can be retrieved
                redoReady.remove(redoReady.get(redoReady.size() - 1));
                drawPanel.drawPanelRepaint();
            }catch (ArrayIndexOutOfBoundsException exception){
                System.out.println(exception +": Nothing has been undone");
            }
        }
    }

    // Choose number of sectors
    private class AddSectorListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent event){
            JSpinner source = (JSpinner) event.getSource();
            sectors = (Double) source.getValue();
            drawPanel.drawPanelRepaint();
        }
    }

    // Choose whether to show sector lines on the drawing panel
    private class AddToggleListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            toggle = !toggle;
            drawPanel.drawPanelRepaint();
        }
    }

    // Choose whether to reflect points
    private class AddReflectionListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            reflection = e.getStateChange() == ItemEvent.SELECTED;
            drawPanel.drawPanelRepaint();
        }
    }

    // Create a buffered image of the drawing panel
    private BufferedImage getScreenShot(JPanel panel){
        BufferedImage bufferedImage = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        panel.paint(bufferedImage.getGraphics());
        return bufferedImage;
    }

    // Resize a Buffered image to a set size
    public static BufferedImage resize (BufferedImage image, int newW, int newH) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return img;
    }

    // Save image of the drawing panel to the gallery panel
    private class AddSaveListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            image = getScreenShot(drawPanel);
            // If more than 12 images have been saved catch exception
            try {
                galleryPanel.addBufferedImage(resize(image, 100, 100));
            }catch(LimitExceededException exception){
                System.out.println(exception);
            }
        }
    }
}


