import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines a tree decomposition of a given graph
 * @author Matthew Askes
 */
public class TreeDecomposition {
    /**
     * the shape of the decomposition
     */
    private Graph tree;
    
    /**
     * defines the nodes in each of the nodes in the tree
     */
    private Map<String, Set<Node>> setMap;
    
    /**
     * The graph on which this is a decomposition.
     */
    private Graph graph;
    
    public TreeDecomposition(Graph tree, Map<String, Set<Node>> setMap, Graph graph) {
        this.tree = tree;
        this.setMap = setMap;
        this.graph = graph;
    }
    
    /**
     * create an empty decomposition
     * @param graph
     */
    public TreeDecomposition(Graph graph) {
        tree = new SingleGraph("tree");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        setMap = new HashMap<>();
        this.graph = graph;
    }
    
    public Graph getTree() {
        return tree;
    }
    
    public Map<String, Set<Node>> getSetMap() {
        return setMap;
    }
    
    /**
     * gets the graph on which this decomposition is defined
     * @return
     */
    public Graph getGraph() {
        return graph;
    }
}
