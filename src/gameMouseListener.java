import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.View;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Matthew Askes
 */
public class gameMouseListener implements MouseListener {
    private GraphicElement element;
    private ColouringGame game;
    private View view;
    private boolean isPlayersTurn;
    
    public gameMouseListener(ColouringGame game) {
        this.game = game;
        this.view = game.getViewer().getDefaultView();
        this.isPlayersTurn = game.isPlayersTurn();
    }
    
    @Override
    public synchronized void mouseClicked(MouseEvent e) {
        element = view.findNodeOrSpriteAt(e.getX(), e.getY());
        if(element instanceof Node){
            
            game.setSelectedNode( element.getId());
            notifyAll();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    
    }
}
