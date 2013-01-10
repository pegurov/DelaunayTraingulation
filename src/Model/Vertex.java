/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Comparator;

/**
 *
 * @author pashathebeast
 */
public class Vertex {
    private int x;          // x - coordinate
    private int y;          // y - coordinate
    private int index;      // index in the original polygon
    private double a;       // polar angle in respect to some other point
    private double l;       // polar length in respect to some other point
    
    
    public double getPolarLength()                  { return l; }
    public void setPolarLength(double length)       { this.l = length; }
    
    public double getA()                            { return a; }
    public void setA(double angle)                  { this.a = angle; }
    
    public int getX()                               { return x; }
    public void setX(int x)                         { this.x = x; }
    
    public int getY()                               { return y; }
    public void setY(int y)                         { this.y = y; }
    
    public int getIndex()                           { return index; }
    public void setIndex(int index)                 { this.index = index; }
     
    public Vertex(){
        this.x = 0;
        this.y = 0; 
    }
    
    public Vertex(int x, int y){
        this.x = x;
        this.y = y;
    }
        
    public Vertex(int x, int y, int index){
        this.x = x;
        this.y = y;
        this.index = index;
    }
    
    // the Y comparator
    public static Comparator<Vertex> COMPARE_BY_Y = new Comparator<Vertex>(){
        @Override
        public int compare(Vertex one, Vertex other) {
            // if the Y coordinates are equal, then retrn the diff btw the X coordinates
            if (one.getY() - other.getY() == 0){
                return (one.getX() - other.getX());
            }
            else{  // if Y's are not equal return the diff btw them
                return (one.getY() - other.getY());
            }
        } 
    }; 
    
    // the X comparator
    public static Comparator<Vertex> COMPARE_BY_X = new Comparator<Vertex>(){
        @Override
        public int compare(Vertex one, Vertex other) {
            // if the Y coordinates are equal, then retrn the diff btw the X coordinates
            if (one.getX() - other.getX() == 0){
                return (one.getY() - other.getY());
            }
            else{  // if Y's are not equal return the diff btw them
                return (one.getX() - other.getX());
            }
        } 
    }; 
    
    // the polar angle comparator
    public static Comparator<Vertex> COMPARE_BY_POLAR_ANGLE = new Comparator<Vertex>(){
        @Override
        public int compare(Vertex one, Vertex other) {
            // if polar cangles are equal, then retrn the diff btw the polar lengths coordinates
            if (one.getA() == other.getA()){
                if (one.getPolarLength()<other.getPolarLength()) {return -1;}
                if (one.getPolarLength()>other.getPolarLength()) {return 1;}
                return 0;
            }
            else{  // if polar angles are not equal return the diff btw them
                if (one.getA()<other.getA()) {return -1;}
                if (one.getA()>other.getA()) {return 1;}
                return 0;
            }
        } 
    }; 
    
}
