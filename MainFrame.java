import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame{

    // The main frame contains a panel where the drawing occurs, the control panel with all control buttons and the gallery panel with all saved images
    private JPanel pane;
    private DrawPanel drawPanel;
    private ControlPanel controlPanel;
    private GalleryPanel galleryPanel;
    public MainFrame(){
        // Sett the title of the frame
        super("Digital Doily");
        // Setting the size of the frame
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        // Add a new main panel with BorderLayout to the frame
        pane = new JPanel();
        pane.setLayout(new BorderLayout());

        // Add all panels to the main panel
        drawPanel = new DrawPanel();
        controlPanel = new ControlPanel();
        galleryPanel = new GalleryPanel();
        // Make all panels communicate
        drawPanel.setControlPanel(controlPanel);
        controlPanel.setDrawPanel(drawPanel);
        controlPanel.setGalleryPanel(galleryPanel);

        pane.add(drawPanel, BorderLayout.CENTER);
        pane.add(controlPanel, BorderLayout.SOUTH);
        pane.add(galleryPanel, BorderLayout.EAST);


        // Make the main frame fit all the added components
        pack();
        // Set the frame visible
        setVisible(true);
        // Set the content pane to the main panel so all my panels will be visible
        setContentPane(pane);
        //
        setResizable(false);
        // Stop the program from running when closing the main frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
