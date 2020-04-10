package strategies;

import game.ColouringGame;

import java.util.*;

/**
 * Uses the activation strategy to find the next vertex to colour.
 * @author Matthew Askes
 */
public class ActivationStrategy implements Stragety {
    
    private Comparator<String> linearOrder;
    private Set<String> markedNodes;
    private List<String> orderedNodes = null;
    
    /**
     * The constructor.
     * @param linearOrder A linear order on the vertex set of the graph in the game for the activation strategy.
     */
    public ActivationStrategy( Comparator<String> linearOrder) {
        
        this.linearOrder = linearOrder;
        
        markedNodes = new HashSet<>();
        
//        for (String node : game.getNodeSet()) {
//            if (!linearOrder.(node)) throw new IllegalArgumentException("The linear order doesn't contain all nodes");
//        }
    
    }
    
    @Override
    public void nextMove(ColouringGame game) {
    
        //calculate the order of the nodes
        if (orderedNodes == null){
            orderedNodes = new ArrayList<>(game.getNodeSet());
            orderedNodes.sort(linearOrder);
        }
        
        //the set of uncoloured nodes
        Set<String> unColouredNodes = new HashSet<>(game.getNodeSet());
        unColouredNodes.removeAll(game.getColouredNodes());
        
        List<String> nodesOrder = game.getNodesPickedOrder();
        String chosenNode;
    
        //if no nodes are coloured then colour the least node
        if (nodesOrder.size() <= 0) {
            chosenNode = orderedNodes.get(0);
            markedNodes.add(chosenNode);
        } else {
        //        x <-  b
            //get the last node coloured by bob
            String lastNode = nodesOrder.get(nodesOrder.size()-1);
            String nodeX = lastNode;
//         while x !in a do
            while (!markedNodes.contains(nodeX)) {
//              A := A union {x}
                markedNodes.add(nodeX);
                
//              s(x) = min{ N+(x) intersection (U union {b})}
//              x <- s(x)
                for (String s : orderedNodes) {
                    if (linearOrder.compare(s, nodeX) <= 0 && //is before prev in linear order or is nodeX
                                game.getGraph().getNode(s).hasEdgeBetween(nodeX) && // is a neighbour of prev
                                (unColouredNodes.contains(s) || s.equals(lastNode))) { // is a uncoloured node or last coloured node
                        nodeX = s;
                        break;
                    }
                }
            }
            
//              if x != b then
                if (!nodeX.equals(lastNode)) {
//                  choose x
                    chosenNode = nodeX;
                } else {
//              else
//                  y <- minL U
                    String nodeY = null;
                    for (String s : orderedNodes) {
                        if (unColouredNodes.contains(s)) {//find smallest unmarked node
                            nodeY = s;
                            break;
                        }
                    }
//                  if y != A then
//                      A <-  A [ fyg
                    markedNodes.add(nodeY);
//                  choose y
                    chosenNode = nodeY;
                }
        }
        
        // colour chosen node
        int colour = 0;
        while (!game.isAllowedColouring(chosenNode, colour)){
            colour++;
        }
        game.setNodeColour(chosenNode,colour);
    
        game.getGraph().getNode(chosenNode);
    }
    
    public List<String> getOrderedNodes() {
        return new ArrayList<>(orderedNodes);
    }
}
