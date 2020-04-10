package strategies;

import game.ColouringGame;
import org.graphstream.graph.Node;

/**
 * @author Matthew Askes
 */
public interface Stragety {
    
    /**
     * Finds and colours the next node in the graph
     */
    void nextMove(ColouringGame game);
    
}

/**
 * Pick random node and colour it with first fit. This is sufficient as if the node cannot be coloured then Bob has won and the game is over.
 */
class randomStrategy implements Stragety {
    
    
    @Override
    public void nextMove(ColouringGame game) {
        
        int numNodes =  game.getGraph().getNodeCount();
        
        Node next;
        do {
            next = game.getGraph().getNode((int) (Math.random() * numNodes));
        } while (game.getColouredNodes().contains(next.getId()));
        int colour = 0;
        while (!game.isAllowedColouring(next.getId(), colour)){
            colour++;
        }
        game.setNodeColour(next.getId(),colour);
    }
}