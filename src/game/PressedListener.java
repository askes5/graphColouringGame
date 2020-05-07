package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Matthew Askes
 */
public interface PressedListener extends MouseListener
{
    @Override
    default void mouseEntered(MouseEvent e) {}
    
    @Override
    default void mouseExited(MouseEvent e) {}
    
    @Override
    default void mouseClicked(MouseEvent e) {}
    
    @Override
    default void mouseReleased(MouseEvent e) {}
}
