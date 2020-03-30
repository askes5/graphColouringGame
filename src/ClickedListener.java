import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Matthew Askes
 */
interface ClickedListener extends MouseListener
{
    @Override
    public default void mouseEntered(MouseEvent e) {}
    
    @Override
    public default void mouseExited(MouseEvent e) {}
    
    @Override
    public default void mousePressed(MouseEvent e) {}
    
    @Override
    public default void mouseReleased(MouseEvent e) {}
}
