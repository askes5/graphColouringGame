import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Matthew Askes
 */
public class ColouringGame {
    
    
    private Graph graph;
    private Map<String,Integer> nodeColours = new HashMap<>();
    private Viewer viewer;
    private Node selectedNode = null;
    private Stragety stragety = new randomStrategy();
    private Map<Integer, Color> colorMap = new HashMap<>();
    
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
    
    public ColouringGame(Collection<Edge> edgeSet, int numOfColours) {
        this.numOfColours = numOfColours;
        graph = new SingleGraph("Colouring Game"); //initlize graph
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addAttribute("ui.stylesheet", styleSheet);
        edgeSet.forEach(edge -> graph.addEdge(edge.getId(), edge.getNode0().getId(), edge.getNode1().getId())); //add all edges to graph
        
        viewer = graph.display();
        viewer.getDefaultView().addMouseListener((ClickedListener) this::mouseClicked);
        
        //initialize colours
        for (int i = 0; i < numOfColours; i++) {
            colorMap.put(i,new Color((int)(Math.random() * 0x1000000)));
        }
        
        
        //label each node
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
        
        playAsBob();
        
    }
    
    private synchronized void playAsBob() {
        //todo add ability to play as Alice
        while (true) {
            
            //Alice moves
            
            stragety.nextMove(this);
            
            //Bob moves
            
            selectedNode = null;
            
            while (selectedNode == null || !isAllowedColouring(selectedNode.getId(), 0)) { //wait until a valid colour is picked
                try {
                    wait(); //wait until a node is selected
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            setNodeColour(selectedNode.getId(),0);
        }
        
    }
    
    /**
     * colours the given node as defined in nodeColours
     */
    private void colourNode(Node node, int i) {
        //todo add more colours and colour such taht nodes have correct colours
        int r = colorMap.get(i).getRed();
        int g = colorMap.get(i).getGreen();
        int b = colorMap.get(i).getBlue();
        String colour = String.format("#%02x%02x%02x", r, g, b);
        node.addAttribute("ui.style", "fill-color: " + colour + ";");
    }
    
    public List<String> getColouredNodes() {
        return new ArrayList<>(nodeColours.keySet());
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
    public boolean isAllowedColouring(String id, int i){
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
            ColouringGame.this.setSelectedNode(((Node) element).getId());
            notifyAll(); //wake up
        }
    }
}

class randomStrategy implements Stragety {
    
    @Override
    public Node nextMove(ColouringGame game) {
        
        int numNodes =  game.getGraph().getNodeCount();
        
        Node next;
        do {
            next = game.getGraph().getNode((int) (Math.random() * numNodes));
        } while (game.getColouredNodes().contains(next.getId()));
        int colour = 0;
        while (!game.isAllowedColouring(next.getId(), colour)){
            colour++;
        }
        game.setNodeColour(next.getId(),colour);
        return next;
    }
}
