package game;

import BoundedGraph.Ktree;
import BoundedGraph.PWGraph;
import BoundedGraph.PathDecomposition;
import linearOrders.LOinPWGenerator;
import linearOrders.LOinTWGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import strategies.ActivationStrategy;
import strategies.Stragety;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Matthew Askes
 */
@SuppressWarnings("FieldCanBeLocal")
public class ColouringGame extends JPanel {
    
    private Graph graph;
    private Map<String,Integer> nodeColours = new HashMap<>();
    private final Set<String> nodeSet = new HashSet<>();
    private List<String> nodesPickedOrder = new ArrayList<>();
    private Viewer viewer;
    private Node selectedNode = null;
    private int selectedColour = -1;
    private Stragety stragety;
    private Map<Integer, Color> colorMap = new HashMap<>();
    private JTextArea textOutputArea = new JTextArea();;
    private boolean isPlayersTurn = false;
    protected String styleSheet =
            "node {" +
                    "fill-color: black;" +
                    "size: 15px; " +
                    "text-background-mode: rounded-box; " +
                    "text-background-color: red;" +
                    "}" +
                    "node.marked {" +
                    "	fill-color: red;" +
                    "}";
    private int numOfColours;
    
    
    /**
     * Makes a random ktree of a given size and width, and uses the activation strategy
     * @return The game
     */
    public static ColouringGame newRandomKtreeGame(int size, int treeWidth, int numOfColours){
        Ktree ktree = new Ktree(size, treeWidth);
        return new ColouringGame(ktree.getGraph().getEdgeSet(), new ActivationStrategy(LOinTWGenerator.calculateComparator(ktree.getDecomposition())), numOfColours);
    }
    
    /**
     * Makes a random pathwidth graph of a given size and width, and uses the activation strategy
     * @return The game
     */
    public static ColouringGame newRandomPWGraphGame(int size, int pathWidth, int numOfColours){
        PWGraph pwGraph = new PWGraph(size, pathWidth);
        //todo fix path width LO generator
        return new ColouringGame(pwGraph.getGraph().getEdgeSet(), new ActivationStrategy(LOinPWGenerator.calculateComparator((PathDecomposition) pwGraph.getDecomposition())), numOfColours);
    }
    
    /**
     * make a new game based on a given edgeset and stragety
     */
    public ColouringGame(Collection<Edge> edgeSet, Stragety stragety, int numOfColours) {
        super(new GridBagLayout());
        this.numOfColours = numOfColours;
        newGameGraph(edgeSet, stragety);//reset the game graph
    
        //initialize colours
        for (int i = 0; i < numOfColours; i++) {
            colorMap.put(i,new Color((int)(Math.random() * 0x1000000)));
        }
        
        SwingUtilities.invokeLater(this::createAndShowGUI); //display gui
        playAsComputer(); //start game
    }
    
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("game.ColouringGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        GridBagConstraints c = new GridBagConstraints();
        
        //add left menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        //add colourpick label
        JLabel label = new JLabel("Select colour");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(0,20,0,20));
        menuPanel.add(label);
        //add colour picker to menu
        ColourPicker colourPicker = new ColourPicker(this);
        menuPanel.add(colourPicker);
        menuPanel.add(Box.createVerticalGlue());
        //add new game button
        JButton restartButton = new JButton("Restart Game");
        restartButton.addActionListener((e) -> {
            frame.dispose(); //dispose of current game
            new ColouringGame(graph.getEdgeSet(),new ActivationStrategy((ActivationStrategy) stragety),numOfColours); //create a new game
        });
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createRigidArea(new Dimension(0,20)));//add space above restart button
        menuPanel.add(restartButton);
        
        //add menu to content pane
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        this.add(menuPanel);
        
        //add graph view
        JPanel panel = new JPanel(new GridLayout()){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1240, 720);
            }
        };

        this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        ViewPanel viewPanel = viewer.addDefaultView(false);
//        viewPanel.setPreferredSize(new Dimension(10, 10));
        panel.add(viewPanel);
        viewPanel.addMouseListener((ClickedListener) this::mouseClicked);
    
        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=1.0;
//        c.weighty=1.0;
        c.gridx = 1;
        c.gridy = 0;
        this.add(panel,c);
    
        //add textoutput
        textOutputArea.setLineWrap(true);
        textOutputArea.setWrapStyleWord(true);
        textOutputArea.setEnabled(false);
        textOutputArea.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 14));
        JScrollPane scrollPane = new JScrollPane(textOutputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        scrollPane.setPreferredSize(new Dimension(viewPanel.getPreferredSize().width,100));
        scrollPane.getViewport().getView().setForeground(Color.RED);
        
        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weighty=0;
        c.gridx = 1;
        c.gridy = 1;
        this.add(scrollPane,c);

        //Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    private void newGameGraph(Collection<Edge> edgeSet, Stragety stragety) {
        this.stragety = stragety;
        graph = new SingleGraph("Colouring Game"); //initialize graph
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addAttribute("ui.stylesheet", styleSheet);
    
        updateTextOutput("Instructions: The goal is to colour nodes in such a way that the graph cannot be coloured using" +
                                 " the available colours. The computer will attempt to thwart your attempts.");
        
        edgeSet.forEach(edge -> graph.addEdge(edge.getId(), edge.getNode0().getId(), edge.getNode1().getId())); //add all edges to graph
        
        //label each node
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
            nodeSet.add(node.getId());//populate the node set
        }
    }
    
    private void playAsComputer(){
        //Alice moves
        stragety.nextMove(this);
        if (gameOver()) return;
        //Bob moves
        isPlayersTurn = true;
        updateTextOutput("Your turn");
    }
    
    private synchronized void playAsBob() {
        //todo add ability to play as Alice
            //wait until a valid node is selected
        if (selectedNode != null && isAllowedColouring(selectedNode.getId(), selectedColour)) {
            //colour the node
            setNodeColour(selectedNode.getId(),selectedColour);

            isPlayersTurn = false;
            
            if(gameOver()) return;
    
            playAsComputer();
        }
        selectedNode = null;
        
    }
    
    /**
     * checks is the game is over. If the game is over then display the winner and end.
     * @return true iff the game has been won.
     */
    private boolean gameOver(){
        //check Alice has won
        boolean aliceHasWon = true;
        for (Node node : graph) {
            if (!nodeColours.containsKey(node.getId())) { //if all nodes are coloured then Alice has won
                aliceHasWon = false;
                break;
            }
        }
        if (aliceHasWon) {
            System.out.println("Alice has won");
                    JOptionPane.showMessageDialog(this, "You have lost!");
    
            updateTextOutput("You have lost");
            return  true;
        }
        
        //check bob has won
        for (Node node : graph) {
            if (!nodeColours.containsKey(node.getId())) { //if any node cannot be coloured then Bob has won
                
                //check if node can be coloured
                
                Set<Integer> colours = new HashSet<>(); //create a list of all colours
                for (int i = 0; i < numOfColours; i++) {
                    colours.add(i);
                }
    
                //remove all neighbour colours
                node.getNeighborNodeIterator().forEachRemaining(neighbour -> colours.remove(nodeColours.getOrDefault(neighbour.getId(),-1)));
                
                if (colours.isEmpty()){
                    System.out.println("Bob has won");
                    JOptionPane.showMessageDialog(this, "You have won!");
                    updateTextOutput("You have won");
                    return true;
                }
                
            }
        }
        return false;
    }
    
    private boolean isValidColour(int colour) {
        return colour >= 0 && colour < numOfColours;
    }
    
    public void updateTextOutput(String message){
        message = "> " + message;
        if (!textOutputArea.getText().equals("")){
            message = "\n"+message;
        }
        textOutputArea.setText(textOutputArea.getText() +message);
    }
    
    /**
     * colours the given node as defined in nodeColours
     */
    private void colourNode(Node node, int i) {
        int r = colorMap.get(i).getRed();
        int g = colorMap.get(i).getGreen();
        int b = colorMap.get(i).getBlue();
        String colour = String.format("#%02x%02x%02x", r, g, b);
        node.addAttribute("ui.style", "fill-color: " + colour + ";");
        nodesPickedOrder.add(node.getId());
    }
    
    public List<String> getNodesPickedOrder() {
        return new ArrayList<>(nodesPickedOrder);
    }
    
    public Map<Integer, Color> getColorMap() {
        return new HashMap<>(colorMap);
    }
    
    public int getNumOfColours() {
        return numOfColours;
    }
    
    public Set<String> getNodeSet() {
        return nodeSet;
    }
    
    public void setSelectedColour(int selectedColour) {
        this.selectedColour = selectedColour;
    }
    
    public Set<String> getColouredNodes() {
        return new HashSet<>(nodeColours.keySet());
    }
    
    public Viewer getViewer() {
        return viewer;
    }
    
    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }
    
    public void setSelectedNode(String id){
        this.selectedNode = graph.getNode(id);
    }
    
    public Graph getGraph() {
        return graph;
    }
    
    public void setNodeColour(String id, int i) throws IllegalArgumentException {
        //cannot colour a node that is already coloured.
        if (nodeColours.containsKey(id)) {
            throw new IllegalArgumentException("node " + selectedNode.toString() + " is already coloured");
        }
        
        //must be a proper colouring
        if (!isAllowedColouring(id, i)) throw new IllegalArgumentException("neighbours cannot be the same colours. Attempted to colour node " + id + " " + i);
        
        nodeColours.put(id,i);
        colourNode(graph.getNode(id),i);
    }
    
    /**
     * checks if a given node can be coloured a given colour
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isAllowedColouring(String id, int i){
        //cannot colour a node that is already coloured.
        if (nodeColours.containsKey(id)) {
            return false;
        }
        AtomicBoolean toReturn = new AtomicBoolean(true);
        graph.getNode(id).getNeighborNodeIterator().forEachRemaining(node -> {
            if (nodeColours.containsKey(node.getId())
                        && i == (nodeColours.get(node.getId()))) {
                toReturn.set(false);
            }
        });
        
        return toReturn.get();
    }
    
    /**
     * mouse listener to select a node by mouse click.
     * @param e the mouse event
     */
    private synchronized void mouseClicked(MouseEvent e) {
        Element element = viewer.getDefaultView().findNodeOrSpriteAt(e.getX(), e.getY());
        if (element instanceof Node) {
            ColouringGame.this.setSelectedNode(element.getId());
//            notifyAll(); //wake up
            playAsBob();
        }
    }
    
    public static void main(String[] args) {
	    System.setProperty("sun.java2d.uiScale", "1.0"); //stop dpi scaling as it breaks mouse events in graph stream
        ColouringGame.newRandomKtreeGame(20,3,8);
    
//        int pw = 2;
//        ColouringGame.newRandomPWGraphGame(15,pw,2*pw +2);
    }
   
}


