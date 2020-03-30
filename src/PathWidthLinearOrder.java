import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of
 *
 * @author Matthew Askes
 */
public class PathWidthLinearOrder  {
    
    /**
     * Calculates and returns a linear order in a graph of bounded path width.
     * Requires:
     * @param graph a graph of bounded path width w
     * @param pathDecomposition a path decomposition of the graph with width w
     * @return a linear order on teh vertex set og the graph
     */
    public static List<Node> calculateOrder(Graph graph, List<Set<Node>> pathDecomposition) {
        ArrayDeque<Node> qeque = new ArrayDeque<>();
        List<Node> linearOrder = new ArrayList<>();
    
        //for each partition
        for (int i = 0; i < pathDecomposition.size(); i++) {
            Set<Node> partition = pathDecomposition.get(i);
            
            // tempQueque <- Queue union (partition - queue)
            ArrayDeque<Node> tempQeque = new ArrayDeque<>(qeque);
            tempQeque.addAll(partition.stream()
                                     .filter(x -> !qeque.contains(x)).collect(Collectors.toSet()) //everything in the partition not in the queue
            );
            qeque.clear();
            
            for (Node node : tempQeque) {
                
                // add if the node does not appear in a latter partition in the decomposition
                if (nodeInDecomposition(node, pathDecomposition.subList(i + 1, pathDecomposition.size()))) {
                    qeque.addLast(node);
                } else {
                    linearOrder.add(node);
                }
                
            }
        }
        
        return linearOrder;
    }
    
    /**
     * checks if a given node is in a path decomposition
     * @param pathDecomposition a decomposition of a graph
     * @param node the node
     * @return true iff the node is in the decomposition
     */
    private static boolean nodeInDecomposition(Node node, List<Set<Node>> pathDecomposition){
        for (Set<Node> partition : pathDecomposition) {
            if (partition.contains(node)) return true;
        }
        return false;
    }
    
}
