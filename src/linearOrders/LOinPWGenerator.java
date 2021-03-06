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
        List<Node> linearOrder = new ArrayList<>();
    
        List<Set<Node>> decomList = pathDecomposition.getList();
        
        //for each partition
        for (Set<Node> partition : decomList) {
            // tempQueue <- Queue union (partition - queue)
            linearOrder.addAll(partition.stream()
                                       .filter(x -> !linearOrder.contains(x))
                                       .collect(Collectors.toSet()) //everything in the partition not in the linear order
            );
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
