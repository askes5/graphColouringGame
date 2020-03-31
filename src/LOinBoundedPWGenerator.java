import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * finds linear orders in graphs of bounded path width
 *
 * @author Matthew Askes
 */
public class LOinBoundedPWGenerator {
    
    /**
     * Calculates and returns a linear order in a graph of bounded path width w.
     * @param pathDecomposition a path decomposition of the graph with width w
     * @return a linear order on the vertex set of the graph
     */
    public static List<Node> calculateListOrder(List<Set<Node>> pathDecomposition) {
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
    
    public static Comparator<String> calculateComparator(List<Set<Node>> pathDecomposition) {
        
        List<Node> listOrder = calculateListOrder(pathDecomposition);
    
        return (o1, o2) -> {
            if (o1.equals(o2)) return 0;
            for (Node node : listOrder) {
                if (node.getId().equals(o1)) return -1;
                if (node.getId().equals(o2)) return 1;
            }
            return 0;
        };
        
    }
}
