import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import java.awt.MouseInfo;

/* 
 * Codici dei tasti del mouse:
 * 0 - Nessun tasto premuto
 * 1 - Tasto sinistro
 * 2 - Tasto rotella
 * 3 - Tasto destro
 */

public class MouseInput implements MouseInputListener, MouseWheelListener {

    protected static int xMouse = 0;
    protected static int yMouse = 0;
    protected static boolean positive = true;
    protected static int multiplier = 1;
    protected static int mouseCircleRadius = GamePanel.dotDiameter;

    @Override
    public void mousePressed(MouseEvent e) {
        // ? Considero il tasto sinistro
        if (e.getButton() == 1) {
            GamePanel.particles.add(new Particle(xMouse, yMouse, positive, multiplier));
        }

        if(e.getButton() == 3){
            positive = !positive;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rot = e.getWheelRotation();

        //? Il multiplier deve essere compreso tra 1 e 8
        if(multiplier <= 1 && rot >= 1 || multiplier >= 30 && rot <= -1){
            return;
        }
        multiplier -= e.getWheelRotation();
        mouseCircleRadius = GamePanel.dotDiameter * multiplier;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xMouse = (int) MouseInfo.getPointerInfo().getLocation().getX() - Game.getxLoc() - GamePanel.dotDiameter / 2;
        yMouse = (int) MouseInfo.getPointerInfo().getLocation().getY() - Game.getyLoc() - GamePanel.dotDiameter / 2;
    }

    public static int getMouseCircleRadius() {
        return mouseCircleRadius;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
