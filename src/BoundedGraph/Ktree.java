package BoundedGraph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.*;

/**
 * pairs a k tree with its decomposition.
 * A BoundedGraph.Ktree is defined as follows: starting with a (k + 1)-vertex complete graph and then repeatedly adding vertices in such a way that each added vertex v has exactly k neighbors U such that, together, the k + 1 vertices formed by v and U form a clique
 * @author Matthew Askes
 */
public class Ktree extends BoundedGraph{
    private final int size;
    private final int treeWidth;
    private Graph graph;
    private TreeDecomposition decomposition;
    private int nodeCounter = 0;
    static int treeCounter = 0;
    
    //starting with a (k + 1)-vertex complete graph and then repeatedly adding vertices in such a way that each added vertex v has exactly k neighbors U such that, together, the k + 1 vertices formed by v and U form a clique
    
    /**
     * create a new random BoundedGraph.Ktree of given size and width
     * @param size
     * @param treeWidth
     */
    public Ktree(int size, int treeWidth) {
        this.size = size;
        this.treeWidth = treeWidth;
        graph = new SingleGraph("kTree_"+ treeCounter++);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setStrict(false);
        graph.setAutoCreate(true);
    
        decomposition = new TreeDecomposition(graph);
    
        //starting with a (k + 1)-vertex complete graph
        Set<Node> clique = addNewClique(treeWidth+1);
        makeClique(clique);
        decomposition.getTree().addNode(clique.toString());
        decomposition.getSetMap().put(clique.toString(),clique);
        
        //repeatedly add vertices such that each added vertex v has exactly k neighbors U such that, together, the k + 1 vertices formed by v and U form a clique
        for (int i = size-clique.size(); i > 0; i--) {
        
            //pick random node, A, in decomposition tree
            Graph DcomTree =  decomposition.getTree();
            Node node = DcomTree.getNode((int) (Math.random()*DcomTree.getNodeCount()));
            Set<Node> decomSet = decomposition.getSetMap().get(node.getId());
    
            //pick random neighbours in decomNode
            List<Node> neighbours = new LinkedList<>(decomSet);
//            Collections.shuffle(neighbours);
//            Set<Node> randomSet = new HashSet<>(neighbours.subList(0, treeWidth));
            neighbours.remove((int)(Math.random()*neighbours.size()));
            
            //connect v to A with treewidth edges
            Node newNode = graph.addNode(String.valueOf(getNodeCount()));
            Set<Node> newPartiton = new HashSet<>(); //new set in the decomposition
            newPartiton.add(newNode);
            newPartiton.addAll(neighbours);
            makeClique(newPartiton);
            //create new node in decomposition
            DcomTree.addEdge(newPartiton.toString()+node.getId(),node.getId(),
                             DcomTree.addNode(newPartiton.toString()).getId());
            decomposition.getSetMap().put(newPartiton.toString(),newPartiton);
            
        }
        
        
        //Show underlying tree structure
        for (Node node : decomposition.getTree()) {//label each node
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = decomposition.getTree().display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
    }
    
    @Override
    public Graph getGraph() {
        return graph;
    }
    
    @Override
    public TreeDecomposition getDecomposition() {
        return decomposition;
    }
    
    @Override
    public int getBound() {
        return treeWidth;
    }
    
    protected int getNodeCount() {
        return nodeCounter++;
    }
}
