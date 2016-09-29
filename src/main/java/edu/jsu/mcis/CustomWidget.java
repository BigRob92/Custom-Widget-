package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    
    
    private final Color HEXA_SELECTED_COLOR = Color.green;
    private final Color DEFAULT_COLOR = Color.white;
    private final Color OCTA_SELECTED_COLOR = Color.red;
    
    private boolean selected;
    private Point[] hexaVertices;
    private Point[] octaVertices; 
    
  

    
    public CustomWidget() {
        observers = new ArrayList<>();
        selected = false;
        Dimension dim = getPreferredSize();
        
        hexaVertices = new Point[6];
        for(int i = 0; i < hexaVertices.length; i++) { hexaVertices[i] = new Point(); }
        
        octaVertices = new Point[8];
        for(int i = 0; i < octaVertices.length; i++) { octaVertices[i] = new Point(); }
       
        calculateVertices(dim.width, dim.height);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(selected);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(int width, int height) {
       
        int side = Math.min(width, height) / 2;
        
        for(int i = 0; i < hexaVertices.length; i++) {
            double rad = (i * (Math.PI / (hexaVertices.length / 2)));
             hexaVertices[i].setLocation(width/3 +(Math.cos(rad)* (side/2)), height/2 + (Math.sin(rad) * (side/2))); 
                            
        }
        for(int i = 0; i < octaVertices.length; i++) {
            double rad = (i * (Math.PI / (octaVertices.length / 2))) +(Math.PI * 0.125);
             octaVertices[i].setLocation(width/3 +(Math.cos(rad)* (side/2)), height/2 + (Math.sin(rad) * (side/2))); 
                            
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        calculateVertices(getWidth(), getHeight());
        Shape[] shape = getShapes();
        g2d.setColor(Color.black);
        g2d.draw(shape[0]);
        g2d.draw(shape[1]);
        if(selected == true) {
            g2d.setColor(HEXA_SELECTED_COLOR);
            g2d.fill(shape[0]);
            g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shape[1]);
        }
        else if (!selected){
            g2d.setColor(DEFAULT_COLOR);
            g2d.fill(shape[0]);
            g2d.setColor(OCTA_SELECTED_COLOR);
            g2d.fill(shape[1]);
            
        }
    }

    public void mouseClicked(MouseEvent event) {
        Shape[] shape = getShapes();
        if(shape[0].contains(event.getX(), event.getY())) {
            selected = !selected;
            notifyObservers();
        }
        repaint(shape[0].getBounds()); 
        
        if(shape[1].contains(event.getX(), event.getY())) {
            selected = true;
            notifyObservers();
        }
        repaint(shape[1].getBounds());
    }
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    
    public Shape [] getShapes() {
        Shape shape[] = new Shape[2];
        int[] x = new int[hexaVertices.length];
        int[] y = new int[hexaVertices.length];
        for(int i = 0; i < hexaVertices.length; i++) {
            x[i] = hexaVertices[i].x;
            y[i] = hexaVertices[i].y;
        }
        shape[0] = new Polygon(x, y, hexaVertices.length);
        
         x = new int[octaVertices.length];
         y = new int[octaVertices.length];
        for(int j = 0; j < octaVertices.length; j++) {
            x[j] = octaVertices[j].x;
            y[j] = octaVertices[j].y;
        }
        shape[1] = new Polygon(x, y, octaVertices.length);
        return shape;
    
    
    }
    public boolean isSelected() { return selected; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}
