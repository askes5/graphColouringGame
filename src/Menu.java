import game.ColouringGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Matthew Askes
 */
public class Menu {

    public Menu() {
        createAndShowGUI();
    }
    
    enum GraphTypes{
        KTREE,
        KPATH
    }
    
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Graph Colouring Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBounds(61, 11, 81, 140);
    
        JLabel title = new JLabel("Graph Coluring Game");
        title.setFont(new Font("",Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        contentPane.add(title);
        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));
    
        JLabel sizeLabel = new JLabel("Number of nodes");
        sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField sizeInput = new JTextField(10);
        contentPane.add(sizeLabel);
        contentPane.add(sizeInput);
    
        
        JLabel widthLabel = new JLabel("Graph Width");
        widthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField widthInput = new JTextField(10);
        contentPane.add(widthLabel);
        contentPane.add(widthInput);
    
        JLabel colourLabel = new JLabel("Number of Colours");
        colourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField colourInput = new JTextField(10);
        contentPane.add(colourLabel);
        contentPane.add(colourInput);
    
        JLabel graphLabel = new JLabel("Graph type");
        graphLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JComboBox<GraphTypes> graphTypesBox = new JComboBox<>(GraphTypes.values());
        graphTypesBox.setSelectedIndex(0);
        contentPane.add(graphLabel);
        contentPane.add(graphTypesBox);
    
        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton start = new JButton("Start");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> {
            int size = getIntegerFromTextBox(sizeInput);
            int width =getIntegerFromTextBox(widthInput);
            int numColours = getIntegerFromTextBox(colourInput);
            
            if (size >0 && width>0 && numColours > 0){
                frame.dispose();
                if (GraphTypes.KTREE.equals(graphTypesBox.getSelectedItem())) {
                    ColouringGame.newRandomKtreeGame(size, width, numColours);
                } else if (GraphTypes.KPATH.equals(graphTypesBox.getSelectedItem())){
                   ColouringGame.newRandomPWGraphGame(size,width,numColours);
                }
            }
            
        });
        
        contentPane.add(start);
        
        contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        frame.getContentPane().add(contentPane);
    
        frame.getRootPane().setDefaultButton(start);//enter activates the start button
        
        //Display the window.
//        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public static int getIntegerFromTextBox(JTextField textBox) {
        int integer;
        
        if (textBox.getText().trim().equals("")) {
            integer = 0;
        } else {
            try {
                integer = Integer.parseInt(textBox.getText());
            } catch (Exception ignored){
                return -1;
            }
        }
        
        return integer;
    }
    
    
    public static void main(String[] args) {
	    System.setProperty("sun.java2d.uiScale", "1.0"); //stop dpi scaling as it breaks mouse events in graph stream
        javax.swing.SwingUtilities.invokeLater(Menu::new);
    }
}
