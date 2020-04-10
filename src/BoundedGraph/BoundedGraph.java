package BoundedGraph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * A graph of bounded size and its decomposition. For example bounded treewidth, pathwidth, bandwith, etc.
 * @author Matthew Askes
 */
public abstract class BoundedGraph {
    
    /**
     * Get the graph
     * @return the graph
     */
    public abstract Graph getGraph();
    
    /**
     * get the decomposition of this graph
     * @return the decomposition of this graph
     */
    public abstract TreeDecomposition getDecomposition();
    
    /**
     * get the bound for this graph
     * @returnthe bound for this graph
     */
    public abstract int getBound();
    
    //used to ensure nodes have unique names
    protected abstract int getNodeCount();
    
    /**
     * turns the set of nodes into a clique
     * @param nodes the nodes to make into a clique
     */
    protected void makeClique(Set<Node> nodes){
        //todo improve
        for (Node s : nodes) {
            for (Node t : nodes) {
                if (!t.equals(s) && !t.hasEdgeBetween(s)) {
                    getGraph().addEdge(s.getId()+ t.getId(), s, t);
                }
            }
        }
    }
    
    /**
     * Makes a new clique of given size. The new clique is defined in this graph. G' <- G union K_n
     * @param size
     * @return
     */
    protected Set<Node> addNewClique(int size){
        Set<Node> clique = new HashSet<>();
        for (int i = 0; i < size; i++) {
            clique.add(getGraph().addNode(String.valueOf(getNodeCount())));
        }
        makeClique(clique);
        return clique;
    }
    
}
