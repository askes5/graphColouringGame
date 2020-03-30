import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.event.MouseEvent;
import java.util.*;

/**
 * @author Matthew Askes
 */
public class ColouringGame {
    
    
    private Graph graph;
    private Map<Node,Integer> nodeColours = new HashMap<>();
    private Viewer viewer;
    private Node selectedNode = null;
    private Stragety stragety = new randomStrategy();
    
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
            
            while (selectedNode == null) {
                try {
                    wait(); //wait until a node selected
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            setNodeColour(selectedNode,0);
    
        }
        
    }
    
    /**
     * colours the given node as defined in nodeColours
     */
    private void colourNode(Node node, int i) {
        //todo add more colours and colour such taht nodes have correct colours
        node.addAttribute("ui.style", "fill-color: red;");
    }
    
    public List<Node> getColouredNodes() {
        return new ArrayList<>(nodeColours.keySet());
    }
    
    public Viewer getViewer() {
        return viewer;
    }
    
    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }
    
    public void setSelectedNode(Node selectedNode) {
        //todo prevent coloured nodes from being selected
        this.selectedNode = selectedNode;
    }
    
    public Graph getGraph() {
        return graph;
    }
    
    public void setNodeColour(Node node, int i){
        nodeColours.put(node,i);
        colourNode(node,i);
    }
    
    private synchronized void mouseClicked(MouseEvent e) {
        Element element = viewer.getDefaultView().findNodeOrSpriteAt(e.getX(), e.getY());
        if (element instanceof Node) {
            ColouringGame.this.setSelectedNode((Node) element);
            notifyAll(); //wake up
        }
    }
}

class randomStrategy implements Stragety {
    
    @Override
    public Node nextMove(ColouringGame game) {
        
        int numNodes =  game.getGraph().getNodeCount();
        
        Node next = game.getGraph().getNode((int) (Math.random()*numNodes));
        
        game.setNodeColour(next,0);
        return null;
    }
}
