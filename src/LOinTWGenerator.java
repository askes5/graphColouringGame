import org.graphstream.graph.Element;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * finds linear orders in graphs of bounded tree width
 *
 * @author Matthew Askes
 */
public class LOinTWGenerator {
    
    /**
     * Calculates and returns a linear order in a graph of bounded tree width k.
     *
     * @param decomposition a tree decomposition of the graph with width k
     * @return a linear order on the vertex set of the graph
     */
    public static List<String> calculateListOrder(TreeDecomposition decomposition) {
        Node root = decomposition.getTree().getNode((int) (Math.random()*decomposition.getTree().getNodeCount()));
        ArrayDeque<Node> queue = new ArrayDeque<>();
        List<String> linearOrder = new ArrayList<>();
        Set<Node> visited = new HashSet<>();


//      $Q$.enqueue($r$)
        queue.add(root);
//      mark $r$ as visited
        visited.add(root);
//
//      \While{$Q$ is not empty}
        while (!queue.isEmpty()) {
//           $v <- Q$.dequeue()
            Node next = queue.remove();
//           $L <- L cup \{V\ L}$
            // add all elements in next not already in L
            linearOrder.addAll(decomposition.getSetMap().get(next.getId())//gets all nodes at a vertex in the underlying tree
                                       .stream()
                                       .filter(o -> !linearOrder.contains(o.getId())) //filter out nodes already in the linear order
                                        .map(Element::getId) //convert to ids
                                       .collect(Collectors.toSet())); //collect as a set
//          \ForAll {$U in N(V)$ s.t. $U$ is unvisited}
            next.getNeighborNodeIterator().forEachRemaining(node -> {
                if (!visited.contains(node)) {
//               $Q$.enqueue($U$)
                    queue.add(node);
//               mark $U$ as visited
                    visited.add(node);
                }
            });
//          \EndFor
//      \EndWhile
        }
//      return L
        
        return linearOrder;
    }
    
    /**
     * checks if a given node is in a tree decomposition
     *
     * @param decomposition a decomposition of a graph
     * @return true iff the node is in the decomposition
     */
//    private static boolean nodeInDecomposition(Node node, List<Set<Node>> treeDecomposition) {
//        for (Set<Node> partition : treeDecomposition) {
//            if (partition.contains(node)) return true;
//        }
//        return false;
//    }
    
    public static Comparator<String> calculateComparator(TreeDecomposition decomposition) {

        List<String> listOrder = calculateListOrder(decomposition);
    
        return getStringComparator(listOrder);
    
    }
    
    public static Comparator<String> getStringComparator(List<String> listOrder) {
        return (o1, o2) -> {
            if (o1.equals(o2)) return 0;
            for (String node : listOrder) {
                if (node.equals(o1)) return -1;
                if (node.equals(o2)) return 1;
            }
            return 0;
        };
    }
}
