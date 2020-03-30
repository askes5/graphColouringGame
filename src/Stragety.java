import org.graphstream.graph.Node;

/**
 * @author Matthew Askes
 */
public interface Stragety {
    
    /**
     * Finds and colours the next node in the graph
     * @return the node coloured
     */
    Node nextMove(ColouringGame game);
    
}
