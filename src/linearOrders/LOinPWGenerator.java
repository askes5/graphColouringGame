package linearOrders;

import BoundedGraph.PathDecomposition;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * finds linear orders in graphs of bounded path width
 *
 * @author Matthew Askes
 */
public class LOinPWGenerator {
    
    /**
     * Calculates and returns a linear order in a graph of bounded path width k.
     * @param pathDecomposition a path decomposition of the graph with width k
     * @return a linear order on the vertex set of the graph
     */
    public static List<Node> calculateListOrder(PathDecomposition pathDecomposition) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        List<Node> linearOrder = new ArrayList<>();
    
        List<Set<Node>> decomList = pathDecomposition.getList();
        
        //for each partition
        for (int i = 0; i < decomList.size(); i++) {
            Set<Node> partition = decomList.get(i);
            
            // tempQueue <- Queue union (partition - queue)
            ArrayDeque<Node> tempQueue = new ArrayDeque<>(queue);
            tempQueue.addAll(partition.stream()
                                     .filter(x -> !queue.contains(x))
                                     .collect(Collectors.toSet()) //everything in the partition not in the queue
            );
            queue.clear();
            
            for (Node node : tempQueue) {
                
                // add if the node does not appear in a latter partition in the decomposition
                if (nodeInDecomposition(node, decomList.subList(i + 1, decomList.size()))) {
                    queue.addLast(node);
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
    
    public static Comparator<String> calculateComparator(PathDecomposition pathDecomposition) {
    
        List<String> listOrder = new ArrayList<>();
        for (Node node : calculateListOrder(pathDecomposition)) {
            String id = node.getId();
            listOrder.add(id);
        }
    
        return LOinTWGenerator.getStringComparator(listOrder);
    
    }
}
