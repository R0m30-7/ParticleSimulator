import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    Random rand = new Random();

    protected static int panelWidth = 1300; // ?Larghezza schermo
    protected static int panelHeight = 630; // ?Altezza schermo

    static final int dotDiameter = 7; // ? Il diametro del cerchio che rappresenta i pallini, in pixel

    int mouseX = 0;
    int mouseY = 0;

    protected static int FPSToDisplay = 0;  //? Visualizzo gli FPS a schermo
    private static long lastSec = System.nanoTime();    //? Salva il secondo precedente

    private int K = 100000;    //? Costante quella della formula fisica
    private int radius = dotDiameter * 10;    //? Il raggio di azione di una particella, da moltiplicare per la carica della particella
    protected static double friction = 0.97;  //? Moltiplicata all'accelerazione
    private double theta = 0;
    private double acceleration = 0;

    protected static ArrayList<Particle> particles = new ArrayList<>();

    int cicli = 0;

    public GamePanel() {
        setPanelSize();

        this.setBackground(Color.DARK_GRAY);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(panelWidth, panelHeight);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) { // Scrivo in questo void le cose che voglio disegnare
        super.paintComponent(g);

        DrawParticles(g, particles);

        // ? Disegno un cerchio bianco intorno al mouse
        if(MouseInput.positive){
            g.setColor(Color.RED);
        } else{
            g.setColor(Color.BLUE);
        }
        mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX() - Game.getxLoc();
        mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY() - Game.getyLoc();
        g.drawOval(mouseX - MouseInput.getMouseCircleRadius()/2, mouseY - MouseInput.getMouseCircleRadius()/2, MouseInput.getMouseCircleRadius(), MouseInput.getMouseCircleRadius());

        for(int i = 0; i < particles.size(); i++){
            CalculateAccParticle(i);
            particles.get(i).UpdatePos(theta, acceleration);
            g.setColor(Color.WHITE);
            //g.drawString((float) particles.get(i).xAccel + " || " + (float) particles.get(i).yAccel, (int) particles.get(i).getX() - 10, (int) particles.get(i).getY() - 10); //Disegna le acc in x e y della particella
            //g.drawOval((int) particles.get(i).getX(), (int) particles.get(i).getY(), 2, 2);   //Disegna un puntino al centro della particella
        }

        //? Disegno le scritte
        WriteTextOnScreen(g);

        if (System.nanoTime() - lastSec > 1000000000) { // ? Entro in questo if una volta al secondo
            lastSec = System.nanoTime();
            FPSToDisplay = cicli;

            panelWidth = (int) GameWindow.getjFrameSize().getWidth() - 16;
            panelHeight = (int) GameWindow.getjFrameSize().getHeight() - 39;

            cicli = 0;
        }

        cicli++;
    }

    private void CalculateAccParticle(int index) {
        Particle particella = particles.get(index);
        acceleration = 0;
        double force = 0;
        double distanza = 0;

        for(int i = 0; i < particles.size(); i++){
            distanza = DistanzaFra(new Punto(particella.getX(), particella.getY()), new Punto(particles.get(i).getX(), particles.get(i).getY()));

            if(i != index && distanza < radius * particles.get(i).getCharge() / 2){
                if(distanza > (dotDiameter * (particella.getCharge() + particles.get(i).getCharge())) / 2){
                    force = K * particles.get(i).getCharge() * particella.getCharge() / Math.pow(distanza, 2);
                } else{
                    //TODO Se una particella tocca un'altra particella, non si muove a prescindere, neanche se una terza applica una forza su di essa
                    //particella.setxAccel(0);
                    //particella.setyAccel(0);
                }
                
                if(particella.getPositive() == particles.get(i).getPositive()){
                    force = -force;
                }
                acceleration = force;
                theta = Math.atan2(particles.get(i).getY() - particella.getY(), particles.get(i).getX() - particella.getX());
            }
        }
    }

    private void DrawParticles(Graphics g, List<Particle> particles) {
        Particle particella;

        for (int i = 0; i < particles.size(); i++) {
            particella = particles.get(i);
            if(particella.getPositive()){
                g.setColor(Color.RED);
            } else{
                g.setColor(Color.BLUE);
            }
            
            int width = dotDiameter * particella.getCharge();
            int height = dotDiameter * particella.getCharge();

            g.fillOval((int) particella.getX() - width / 2, (int) particella.getY() - height / 2, width, height);

            g.setColor(Color.WHITE);
            g.drawOval((int) particella.getX() - radius * particella.getCharge() / 2, (int) particella.getY() - radius * particella.getCharge() / 2, radius * particella.getCharge(), radius * particella.getCharge());
        }
    }

    private void WriteTextOnScreen(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString("Dimension: " + panelWidth + " x " + panelHeight, 3, 15);
        g.drawString("FPS: " + FPSToDisplay, panelWidth - 45, 15);
        g.drawString(String.valueOf(MouseInput.multiplier), MouseInput.xMouse - 4, MouseInput.yMouse - 3);
    }

    public static double DistanzaFra(Punto a, Punto b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
}