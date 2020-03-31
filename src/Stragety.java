import org.graphstream.graph.Node;

/**
 * @author Matthew Askes
 */
public abstract class Stragety {
    
    // the game on which this stragety operates.
    ColouringGame game;
    
    public Stragety(ColouringGame game) {
        this.game = game;
    }
    
    /**
     * Finds and colours the next node in the graph
     * @return the node coloured
     */
    abstract Node nextMove();
    
}

/**
 * Pick random node and colour it with first fit. This is sufficent as if the node cannot be coloured then Bob has won and the game is over.
 */
class randomStrategy extends Stragety {
    
    public randomStrategy(ColouringGame game) {
        super(game);
    }
    
    @Override
    public Node nextMove() {
        
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
        return next;
    }
}