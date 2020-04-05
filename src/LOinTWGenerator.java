import org.graphstream.graph.Graph;
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
     * @param setMap a tree decomposition of the graph with width k
     * @param tree   a tree that defines the shape of the tree decomposition
     * @return a linear order on the vertex set of the graph
     */
    public static List<Node> calculateListOrder(Map<String, Set<Node>> setMap, Graph tree) {
        Node root = tree.getNode(0);
        ArrayDeque<Node> queue = new ArrayDeque<>();
        List<Node> linearOrder = new ArrayList<>();
        Set<Node> visited = new HashSet<>();


//      $Q$.enqueue($r$)
        queue.add(root);
//      mark $r$ as visited
        visited.add(root);
//
//      \While{$Q$ is not empty}
        while (!queue.isEmpty()) {
//           $v \gets Q$.dequeue()
            Node next = queue.remove();
//           $L \gets L \cup \{V\setminus L\}$
            linearOrder.addAll(setMap.get(next.getId())
                                       .stream()
                                       .filter(o -> !linearOrder.contains(o))
                                       .collect(Collectors.toSet())); // add all elements in next not already in L
//          \ForAll {$U\in N(V)$ s.t. $U$ is unvisited}
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
//      \State \textbf{return}  $L$
        
        return linearOrder;
    }
    
    /**
     * checks if a given node is in a tree decomposition
     *
     * @param treeDecomposition a decomposition of a graph
     * @param node              the node
     * @return true iff the node is in the decomposition
     */
    private static boolean nodeInDecomposition(Node node, List<Set<Node>> treeDecomposition) {
        for (Set<Node> partition : treeDecomposition) {
            if (partition.contains(node)) return true;
        }
        return false;
    }
    
    public static Comparator<String> calculateComparator(Map<String, Set<Node>> setMap, Graph tree) {

        List<Node> listOrder = calculateListOrder(setMap, tree);
    
        return getStringComparator(listOrder);
    
    }
    
    public static Comparator<String> getStringComparator(List<Node> listOrder) {
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
