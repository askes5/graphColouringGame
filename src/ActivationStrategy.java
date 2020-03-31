import org.graphstream.graph.Node;

import java.util.*;

/**
 * Uses the activation strategy to find the next vertex to colour.
 * @author Matthew Askes
 */
public class ActivationStrategy extends Stragety {
    
    private List<String> linearOrder;
    private Set<String> markedNodes;
    private Set<String> unMarkedNodes;
    
    
    /**
     * The constructor.
     * @param game The game from which the nodes will be choosen and coloured
     * @param linearOrder A linear order on the vertex get of the graph in the game for the activation strategy.
     */
    public ActivationStrategy(ColouringGame game, List<String> linearOrder) {
        super(game);
        
        this.linearOrder = new ArrayList<>(linearOrder);
        
        markedNodes = new HashSet<>();
        unMarkedNodes = new HashSet<>(game.getNodeSet());
    
        for (String node : game.getNodeSet()) {
            if (!linearOrder.contains(node)) throw new IllegalArgumentException("The linear order doesn't contain all nodes");
        }
        
    }
    
    @Override
    public Node nextMove() {
    
        List<String> nodesOrder = game.getNodesPickedOrder();
        String choosenNode;
        
        
//        x <-  b
        //get the last node coloured by bob
        String lastNode = nodesOrder.get(nodesOrder.size()-1);
        String prevNode = lastNode;
        
        if (prevNode == null) { //if no nodes are coloured then colour the least node
            choosenNode = linearOrder.get(0);
        } else {
//         while x !in a do
            while (!markedNodes.contains(prevNode)) {
//              A := A union {x}
                markedNodes.add(prevNode);
//              s(x) = min{ u in N(x) : u < x} union (U union {b})
                String next;
    
                for (String s : linearOrder) {
                    if (game.getGraph().getNode(s).hasEdgeBetween(prevNode) && (unMarkedNodes.contains(s) || s.equals(lastNode))) {
                        next = s;
                        break;
                    }
                }
//              x <- s(x)
//          if x != b then
//              choose x
//          else
//              y <- minL U
//              if y != A then
//                  A <-  A [ fyg
//         choose y
            }
        }
        return null;
    }
}
