package BoundedGraph;

import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Askes
 */
public class PathDecomposition implements Decomposition {
    
    private List<Set<Node>> decomposition;
    
    public PathDecomposition(List<Set<Node>> decomposition) {
        this.decomposition = decomposition;
    }
    
    /**
     * new empty decomposition
     */
    public PathDecomposition() {
        decomposition = new ArrayList<>();
    }
    
    public List<Set<Node>> getList() {
        return decomposition;
    }
}
