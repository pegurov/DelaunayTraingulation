/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pashathebeast
 */
public class StaticMethods {
    
    public static void Triangulate(List<Vertex> iVertices, ArrayList<ArrayList<Integer>> adjacencyList){
        
        if (iVertices.size()>3)
        {
            List<Vertex> leftHalf = iVertices.subList(0, (int)(iVertices.size() / 2.0));
            List<Vertex> rightHalf = iVertices.subList((int)(iVertices.size() / 2.0), iVertices.size());
            Triangulate(leftHalf, adjacencyList);
            Triangulate(rightHalf, adjacencyList);
            
            
            // now we merge the halves that were triangulated
            
            //<editor-fold defaultstate="collapsed" desc="find left and right convex points">
            
            Vertex leftHalfLeftmost   = leftHalf.get(0);
            Vertex rightHalfRightmost = rightHalf.get(0);
            for (Vertex v : leftHalf) {
                if (v.getX() == leftHalfLeftmost.getX()) {
                    if (v.getY() > leftHalfLeftmost.getY()) {
                        leftHalfLeftmost = v;
                    }
                }
                else{
                    if (v.getX() < leftHalfLeftmost.getX()) {
                        leftHalfLeftmost = v;
                    }
                }
             }
             for (Vertex v : rightHalf) {
                if (v.getX() == rightHalfRightmost.getX()) {
                    if (v.getY() < rightHalfRightmost.getY()) {
                        rightHalfRightmost = v;
                    }
                }
                else{
                    if (v.getX() > rightHalfRightmost.getX()) {
                        rightHalfRightmost = v;
                    }
                }
             }
             
             
             
             // start with the right part
             double leastAngle, currentAngle, distance;
             Vertex currentPoint, nextPoint, nextConvexPoint, leftConvexPoint, rightConvexPoint;
             currentPoint = rightHalfRightmost;
             leastAngle = Math.PI*2;
             boolean directionPositive = false;
             nextConvexPoint = null;
             nextPoint = null;
             
             while(directionPositive == false){    
                 // finding the next convex point
                 for (int aIndex : adjacencyList.get(currentPoint.getIndex())) {       
                     for (int i = 0; i < iVertices.size(); i++) {
                         if (iVertices.get(i).getIndex() == aIndex) {
                             nextPoint = iVertices.get(i);   
                         }
                     }
                     distance = Math.sqrt((nextPoint.getY() - currentPoint.getY())*(nextPoint.getY() - currentPoint.getY()) + (nextPoint.getX() - currentPoint.getX())*(nextPoint.getX() - currentPoint.getX()));
                     currentAngle =  Math.acos((nextPoint.getY() - currentPoint.getY()) / distance );                     
                     if (currentPoint.getX() < nextPoint.getX()) { 
                            currentAngle = 2*Math.PI - currentAngle;
                     }
                     if (currentAngle <= leastAngle) {
                         leastAngle = currentAngle;
                         nextConvexPoint = nextPoint;
                     }   
                 }//found the next convex point.
                 
                 // now we have to check whether any of the directions with the left part are positive
                 directionPositive = false;
                 for (Vertex v : leftHalf) {
                     if (Direction(currentPoint, nextConvexPoint, v) > 0) {
                         directionPositive = true;
                         break;
                     }
                 }
                 if (!directionPositive) {
                     currentPoint = nextConvexPoint;
                     leastAngle = Math.PI*2;
                     nextConvexPoint = null;
                 }  
             }
             // so now we have what we looked for in currentPoint!! congrats
             rightConvexPoint = currentPoint;
             
             // So now the left part
             
             currentPoint = leftHalfLeftmost;
             leastAngle = Math.PI*2;
             directionPositive = true;
             nextConvexPoint = null;
             nextPoint = null;
             
             while(directionPositive == true){    
                 // finding the next convex point
                 for (int aIndex : adjacencyList.get(currentPoint.getIndex())) {       
                     for (int i = 0; i < iVertices.size(); i++) {
                         if (iVertices.get(i).getIndex() == aIndex) {
                             nextPoint = iVertices.get(i);   
                         }
                     }                      
                     distance = Math.sqrt((nextPoint.getY() - currentPoint.getY())*(nextPoint.getY() - currentPoint.getY()) + (nextPoint.getX() - currentPoint.getX())*(nextPoint.getX() - currentPoint.getX()));
                     currentAngle =  Math.acos((nextPoint.getY() - currentPoint.getY()) / distance );                     
                     if (currentPoint.getX() > nextPoint.getX()) { 
                            currentAngle = 2*Math.PI - currentAngle;
                     }
                     if (currentAngle <= leastAngle) {
                         leastAngle = currentAngle;
                         nextConvexPoint = nextPoint;
                     }   
                 }//found the next convex point.
                 
                 // now we have to check whether any of the directions with the right part are negative
                 directionPositive = true;
                 for (Vertex v : rightHalf) {
                     if (Direction(currentPoint, nextConvexPoint, v) < 0) {
                         directionPositive = false;
                         break;
                     }
                 }
                 if (directionPositive) {
                     currentPoint = nextConvexPoint;
                     leastAngle = Math.PI*2;
                     nextConvexPoint = null;
                 }  
             }
             // so now we have what we looked for in currentPoint!! congrats
             leftConvexPoint = currentPoint;  
             //</editor-fold> 
             
             // adding the new diagonal for the base LR edge
             adjacencyList.get(leftConvexPoint.getIndex()).add(rightConvexPoint.getIndex());
             adjacencyList.get(rightConvexPoint.getIndex()).add(leftConvexPoint.getIndex());
             
             Vertex potentialCandidate = new Vertex(0,0,0);
             Vertex nextPotentialCandidate = new Vertex(0,0,0);
             Vertex rightHalfCandidate = new Vertex(0,0,0);
             Vertex leftHalfCandidate = new Vertex(0,0,0);
             Vertex leftHalfBase = leftConvexPoint;
             Vertex rightHalfBase = rightConvexPoint;
             nextPoint = null;
             double a, b, c, angle, angle1, angle2 = 0;
             a = 0; b = 0; c = 0;
             ArrayList<Vertex> leftPotentialPoints = new ArrayList<>();
             ArrayList<Vertex> rightPotentialPoints = new ArrayList<>();
             //boolean criterion1 = false;
             //boolean criterion2;
             
             while ((rightHalfCandidate != null) || (leftHalfCandidate != null))
             { 
                 // null out both candidates so that we will quit when none of them are submitted
                 rightHalfCandidate = null;
                 leftHalfCandidate = null;
                 
                 leftPotentialPoints.clear();
                 rightPotentialPoints.clear();
                 nextPoint = null;
                 potentialCandidate = null;
                 nextPotentialCandidate = null;
                 //criterion2 = false;
                 
                 
                 //<editor-fold defaultstate="collapsed" desc="try to find right half candidate">
                 for (int aIndex : adjacencyList.get(rightHalfBase.getIndex()))
                 {
                     for (int i = 0; i < iVertices.size(); i++) {
                         if ((iVertices.get(i).getIndex() == aIndex) && (iVertices.get(i).getIndex() != leftHalfBase.getIndex())) {
                             nextPoint = iVertices.get(i);   
                         }
                     }
                     if(nextPoint != null){
                         
                         a = Math.sqrt(Math.pow((rightHalfBase.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfBase.getY() - leftHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((nextPoint.getX() - rightHalfBase.getX()), 2) + Math.pow((nextPoint.getY() - rightHalfBase.getY()), 2));
                         c = Math.sqrt(Math.pow((leftHalfBase.getX() - nextPoint.getX()), 2) + Math.pow((leftHalfBase.getY() - nextPoint.getY()), 2));
                         angle = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         
                         nextPoint.setA(angle);
                         nextPoint.setPolarLength(b);
                         if (Direction(leftHalfBase, rightHalfBase, nextPoint)>0) {
                            rightPotentialPoints.add(nextPoint);
                         }
                         nextPoint = null;
                     }
                 }
                 Collections.sort(rightPotentialPoints, Vertex.COMPARE_BY_POLAR_ANGLE);
                 
                 for (int i = 0; i < rightPotentialPoints.size(); i++) {
                     potentialCandidate = rightPotentialPoints.get(i);
                     
                     if (i != rightPotentialPoints.size()-1) {
                         nextPotentialCandidate = rightPotentialPoints.get(i+1);
                         // find the 2nd criterion
                         
                         c = Math.sqrt(Math.pow((rightHalfBase.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfBase.getY() - leftHalfBase.getY()), 2));
                         a = Math.sqrt(Math.pow((potentialCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((potentialCandidate.getY() - rightHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((potentialCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((potentialCandidate.getY() - leftHalfBase.getY()), 2));
                         angle1 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         a = Math.sqrt(Math.pow((nextPotentialCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((nextPotentialCandidate.getY() - rightHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((nextPotentialCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((nextPotentialCandidate.getY() - leftHalfBase.getY()), 2));
                         angle2 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         
                         //if () { criterion2 = true; }
                         //else { criterion2 = false; }
                         if (angle1<angle2) {
                             // if the second criterion does not hold then the edge from potentialCandidate to rightHalfBase is deleted
   
                             for (int j = 0; j < adjacencyList.get(potentialCandidate.getIndex()).size(); j++) {
                                 if(adjacencyList.get(potentialCandidate.getIndex()).get(j) == rightHalfBase.getIndex())
                                 {
                                     adjacencyList.get(potentialCandidate.getIndex()).remove(j);
                                     System.out.print("removed");
                                 }
                             }
                             for (int j = 0; j < adjacencyList.get(rightHalfBase.getIndex()).size(); j++) {
                                 if(adjacencyList.get(rightHalfBase.getIndex()).get(j) == potentialCandidate.getIndex())
                                 {
                                     adjacencyList.get(rightHalfBase.getIndex()).remove(j);
                                     System.out.print("removed");
                                 }
                             }
                             
                         }
                         else { // if it does hold
                             rightHalfCandidate = potentialCandidate;
                             break; // break out of for loop so that it doesnt go to else where it will set rightHalfCandidate as the last element of collection
                         }

                     }
                     else{ // if the potential point is the last one in the collection then it does not have the next one, so we submit it
                         rightHalfCandidate = potentialCandidate;
                     }
                     

                 }
                 //</editor-fold>
                
                 //<editor-fold defaultstate="collapsed" desc="try to find left half candidate">
                 
                 potentialCandidate = null;
                 nextPotentialCandidate = null;
                 nextPoint = null;
                 //criterion2 = false;
                 
                 for (int aIndex : adjacencyList.get(leftHalfBase.getIndex()))
                 {
                     for (int i = 0; i < iVertices.size(); i++) {
                         if ((iVertices.get(i).getIndex() == aIndex) && (iVertices.get(i).getIndex() != rightHalfBase.getIndex())) {
                             nextPoint = iVertices.get(i);   
                         }
                     }
                     if(nextPoint!= null){
                         
                         a = Math.sqrt(Math.pow((rightHalfBase.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfBase.getY() - leftHalfBase.getY()), 2));
                         c = Math.sqrt(Math.pow((nextPoint.getX() - rightHalfBase.getX()), 2) + Math.pow((nextPoint.getY() - rightHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((leftHalfBase.getX() - nextPoint.getX()), 2) + Math.pow((leftHalfBase.getY() - nextPoint.getY()), 2));
                         angle = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         
                         nextPoint.setA(angle);
                         nextPoint.setPolarLength(b);
                         if (Direction(rightHalfBase, leftHalfBase, nextPoint)<0) {
                            leftPotentialPoints.add(nextPoint);
                         }
                         nextPoint = null;
                     }
                 }
                 Collections.sort(leftPotentialPoints, Vertex.COMPARE_BY_POLAR_ANGLE);
                 
                 for (int i = 0; i < leftPotentialPoints.size(); i++) {
                     potentialCandidate = leftPotentialPoints.get(i);
                     
                     if (i != leftPotentialPoints.size()-1) {
                         nextPotentialCandidate = leftPotentialPoints.get(i+1);
                         // find the 2nd criterion
                                    
                         c = Math.sqrt(Math.pow((rightHalfBase.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfBase.getY() - leftHalfBase.getY()), 2));
                         a = Math.sqrt(Math.pow((potentialCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((potentialCandidate.getY() - rightHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((potentialCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((potentialCandidate.getY() - leftHalfBase.getY()), 2));
                         angle1 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         a = Math.sqrt(Math.pow((nextPotentialCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((nextPotentialCandidate.getY() - rightHalfBase.getY()), 2));
                         b = Math.sqrt(Math.pow((nextPotentialCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((nextPotentialCandidate.getY() - leftHalfBase.getY()), 2));
                         angle2 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                         
                         
                         if (angle1<angle2) {
                             // if the second criterion does not hold then the edge from potentialCandidate to leftHalfBase is deleted
                             for (int j = 0; j < adjacencyList.get(potentialCandidate.getIndex()).size(); j++) {
                                 if(adjacencyList.get(potentialCandidate.getIndex()).get(j) == leftHalfBase.getIndex())
                                 {
                                     adjacencyList.get(potentialCandidate.getIndex()).remove(j);
                                     System.out.print("removed");
                                 }
                             }
                             for (int j = 0; j < adjacencyList.get(leftHalfBase.getIndex()).size(); j++) {
                                 if(adjacencyList.get(leftHalfBase.getIndex()).get(j) == potentialCandidate.getIndex())
                                 {
                                     adjacencyList.get(leftHalfBase.getIndex()).remove(j);
                                     System.out.print("removed");
                                 }
                             }
                         }
                         else { // if it does hold
                             leftHalfCandidate = potentialCandidate;
                             break; // break out of for loop so that it doesnt go to else where it will set leftHalfCandidate as the last element of collection
                         }

                     }
                     else{ // if the potential point is the last one in the collection then it does not have the next one, so we submit it
                         leftHalfCandidate = potentialCandidate;
                     }  
                 }
                 //</editor-fold>
                 
                 // check which of them to take if there are two of them
                 //add a diagonal and change leftHalfBase and rightHalfBase
                 if (rightHalfCandidate != null && leftHalfCandidate != null) {
                     c = Math.sqrt(Math.pow((rightHalfBase.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfBase.getY() - leftHalfBase.getY()), 2));
                     b = Math.sqrt(Math.pow((leftHalfCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((leftHalfCandidate.getY() - rightHalfBase.getY()), 2));
                     a = Math.sqrt(Math.pow((leftHalfCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((leftHalfCandidate.getY() - leftHalfBase.getY()), 2));

                     angle1 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));
                     b = Math.sqrt(Math.pow((rightHalfCandidate.getX() - rightHalfBase.getX()), 2) + Math.pow((rightHalfCandidate.getY() - rightHalfBase.getY()), 2));
                     a = Math.sqrt(Math.pow((rightHalfCandidate.getX() - leftHalfBase.getX()), 2) + Math.pow((rightHalfCandidate.getY() - leftHalfBase.getY()), 2));

                     angle2 = Math.acos(((a*a) + (b*b) - (c*c)) / (2*a*b));  

                     if (angle1>angle2) { //rightHalfCandidate is not inside leftHalfCandidate circumcircle
                         adjacencyList.get(rightHalfBase.getIndex()).add(leftHalfCandidate.getIndex());
                         adjacencyList.get(leftHalfCandidate.getIndex()).add(rightHalfBase.getIndex());
                         leftHalfBase = leftHalfCandidate;
                     }
                     else{
                         adjacencyList.get(leftHalfBase.getIndex()).add(rightHalfCandidate.getIndex());
                         adjacencyList.get(rightHalfCandidate.getIndex()).add(leftHalfBase.getIndex());
                         rightHalfBase = rightHalfCandidate;
                     }
                     
                 }
                 else{
                     // take one if there is just one
                     if (rightHalfCandidate != null) {
                         adjacencyList.get(leftHalfBase.getIndex()).add(rightHalfCandidate.getIndex());
                         adjacencyList.get(rightHalfCandidate.getIndex()).add(leftHalfBase.getIndex());
                         rightHalfBase = rightHalfCandidate;
                     }
                     if (leftHalfCandidate != null) {
                         adjacencyList.get(rightHalfBase.getIndex()).add(leftHalfCandidate.getIndex());
                         adjacencyList.get(leftHalfCandidate.getIndex()).add(rightHalfBase.getIndex());
                         leftHalfBase = leftHalfCandidate;
                     }
                }
                
                 
             }  // end of while loop          
        
        }
            
        //<editor-fold defaultstate="collapsed" desc="add diagonals for 2 and 3 points">
        else{
            if (iVertices.size() == 2) {
                adjacencyList.get(iVertices.get(0).getIndex()).add(iVertices.get(1).getIndex());
                adjacencyList.get(iVertices.get(1).getIndex()).add(iVertices.get(0).getIndex());
            }
            
            if (iVertices.size() == 3){
                adjacencyList.get(iVertices.get(0).getIndex()).add(iVertices.get(1).getIndex());
                adjacencyList.get(iVertices.get(1).getIndex()).add(iVertices.get(2).getIndex());
                adjacencyList.get(iVertices.get(2).getIndex()).add(iVertices.get(0).getIndex());
                adjacencyList.get(iVertices.get(1).getIndex()).add(iVertices.get(0).getIndex());
                adjacencyList.get(iVertices.get(2).getIndex()).add(iVertices.get(1).getIndex());
                adjacencyList.get(iVertices.get(0).getIndex()).add(iVertices.get(2).getIndex());
            }
        }
        //</editor-fold>
    
    } // end of Triangulate method
    
    public static int Direction( Vertex x, Vertex y, Vertex z){
        Vertex a = new Vertex(z.getX() - x.getX(), z.getY() - x.getY());
        Vertex b = new Vertex(y.getX() - x.getX(), y.getY() - x.getY());
        return (a.getX() * b.getY() - a.getY() * b.getX());
    }
    
    
    

}
