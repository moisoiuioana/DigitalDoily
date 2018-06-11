import javax.naming.LimitExceededException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// Create a gallery panel to hold all saved images of the drawing panel
public class GalleryPanel extends JPanel {

    private JButton remove;
    private JPanel displayDrawings;
    private java.util.List<JLabel> images;
    private JLabel removeImage;
    private boolean selected;
    // Index of the image to be removed
    private int index;
    // Keep track of the number of images added
    private int count;

    public GalleryPanel(){
        super();
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        setPreferredSize(new Dimension(400, 800));
        this.setLayout(new BorderLayout());

        // Choose to remove image
        remove = new JButton("Remove");
        add(remove, BorderLayout.NORTH);
        // Add appropriate listener
        AddRemoveListener addRemoveListener = new AddRemoveListener();
        remove.addActionListener(addRemoveListener);

        // Create a new panel to hold a maximum of 12 images
        displayDrawings = new JPanel();
        displayDrawings.setLayout(new GridLayout(4,3));
        displayDrawings.setVisible(true);
        add(displayDrawings, BorderLayout.CENTER);

        images = new ArrayList<>();
        removeImage = new JLabel();
        selected = false;
        // Count how many images were saved
        count = 0;
    }


    // Add an image to the gallery panel and throw exception if more than 12 were added
    public void addBufferedImage (BufferedImage image) throws LimitExceededException{
        if(count == 12){
            throw new LimitExceededException();
        }
        // Add image
        JLabel tmp = new JLabel(new ImageIcon((image)));
        images.add(tmp);
        // Add an appropriate listener to the image so it can be selected for it to be removed
        AddSelectImageListener addSelectImageListener = new AddSelectImageListener();
        tmp.addMouseListener(addSelectImageListener);
        displayDrawings.add(tmp);
        // Show the new selected image
        displayDrawings.revalidate();
        displayDrawings.repaint();
        count++;
    }

    // Remove the image from the gallery panel
    public void removeBufferedImage (JLabel image){
        images.remove(image);
        displayDrawings.remove(index);
        displayDrawings.revalidate();
        displayDrawings.repaint();
    }


    // Remove an image
    private class AddRemoveListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selected){
                removeBufferedImage(removeImage);
                selected = false;
                count--;
            }
        }
    }

    // Select an image
    private class AddSelectImageListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e){
            if(e.getSource() instanceof JLabel) {
                removeImage = (JLabel) e.getSource();
                removeImage.setBorder(new LineBorder(Color.ORANGE,1));
                index = images.indexOf(removeImage);
                selected = true;
            }
        }

        @Override
        public void mousePressed(MouseEvent e){
            if(e.getSource() instanceof JLabel) {
                removeImage = (JLabel) e.getSource();
                removeImage.setBorder(new LineBorder(Color.ORANGE,1));
                index = images.indexOf(removeImage);
                selected = true;
            }
        }

    }
}
