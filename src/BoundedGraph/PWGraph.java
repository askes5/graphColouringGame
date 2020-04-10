package BoundedGraph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Defines a graph of a given pathwidth along with its decomposition
 * @author Matthew Askes
 */
public class PWGraph extends BoundedGraph{
    private final int size;
    private final int pathWidth;
    private Graph graph;
    private TreeDecomposition decomposition;
    private int nodeCounter = 0;
    static int pathcounter = 0;
    
    /**
     * create a new random graph of bounded tree of given size and width
     * @param size
     * @param pathWidth
     */
    public PWGraph(int size, int pathWidth) {
        this.size = size;
        this.pathWidth = pathWidth;
        graph = new SingleGraph("path_"+ pathcounter++);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        
        decomposition = new TreeDecomposition(graph);
        
        //starting with a (k + 1)-vertex complete graph
        Set<Node> clique = addNewClique(pathWidth-1);
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
//            Set<Node> randomSet = new HashSet<>(neighbours.subList(0, pathWidth));
            neighbours.remove((int)(Math.random()*neighbours.size()));
            
            //connect v to A with treewidth edges
            Node newNode = graph.addNode(String.valueOf(getNodeCount()));
            Set<Node> newPartiton = new HashSet<>(); //new set in the decomposition
            newPartiton.add(newNode);
            newPartiton.addAll(neighbours);
            makeClique(newPartiton);
            //create new node in decomposition
            DcomTree.addEdge(newPartiton.toString()+node.getId(),node.getId(), DcomTree.addNode(newPartiton.toString()).getId());
            decomposition.getSetMap().put(newPartiton.toString(),newPartiton);
        }
    }
    
    public int getNodeCount() {
        return nodeCounter++;
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
        return pathWidth;
    }
}
