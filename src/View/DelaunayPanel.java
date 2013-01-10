/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;
import Controller.*;
import Model.*;
import java.awt.*;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author pashathebeast
 */
public class DelaunayPanel extends javax.swing.JPanel {
    
    private ArrayList<Vertex> vertices;
    private boolean allowToAddVertex;
    private ArrayList<ArrayList<Integer>> adjacencyList;
    
    public DelaunayPanel() {
        initComponents();
        vertices = new ArrayList<>();
        allowToAddVertex = true;
        adjacencyList = new ArrayList<>();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);   
        if (vertices == null) { return; }
        
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(1));
        for (Vertex v: vertices) {
            g2D.drawOval(v.getX(), v.getY(), 1, 1);
            //g2D.drawString(String.format("%d", v.getIndex()), v.getX()-5, v.getY()-5);
        }
        
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(0.5f));
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                   g2D.drawLine(vertices.get(i).getX(), vertices.get(i).getY(), vertices.get(adjacencyList.get(i).get(j)).getX(), vertices.get(adjacencyList.get(i).get(j)).getY());
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonTriangulate = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        buttonRandom = new javax.swing.JButton();
        textFieldRandom = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        buttonTriangulate.setText("Triangulate");
        buttonTriangulate.setName("buttonTriangulate"); // NOI18N
        buttonTriangulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTriangulateActionPerformed(evt);
            }
        });

        buttonClear.setLabel("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        buttonRandom.setText("Random");
        buttonRandom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRandomActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(buttonTriangulate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 147, Short.MAX_VALUE)
                .add(textFieldRandom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonRandom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonClear)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(359, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonClear)
                    .add(buttonTriangulate)
                    .add(buttonRandom)
                    .add(textFieldRandom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        buttonTriangulate.getAccessibleContext().setAccessibleName("buttonTrianguate");
        buttonClear.getAccessibleContext().setAccessibleName("buttonClear");
    }// </editor-fold>//GEN-END:initComponents

    private void buttonTriangulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTriangulateActionPerformed
        for (ArrayList<Integer> al : adjacencyList) {
            al.clear();
        }
        Collections.sort(vertices, Vertex.COMPARE_BY_X);
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).setIndex(i);
        }
        StaticMethods.Triangulate(vertices, adjacencyList);
        this.repaint();
    }//GEN-LAST:event_buttonTriangulateActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        vertices.clear();
        adjacencyList.clear();
        this.repaint();
    }//GEN-LAST:event_buttonClearActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        for (int i = 0; i < vertices.size(); i++) {
            if ((evt.getX() == vertices.get(i).getX()) && (evt.getY() == vertices.get(i).getY())) {
                allowToAddVertex = false;
            }
        }
        if (allowToAddVertex) {
            vertices.add(new Vertex(evt.getX(), evt.getY(), vertices.size()));
            adjacencyList.add(new ArrayList<Integer>());
            for (ArrayList<Integer> al : adjacencyList) {
                al.clear();
            }
            Collections.sort(vertices, Vertex.COMPARE_BY_X);
            for (int i = 0; i < vertices.size(); i++) {
                vertices.get(i).setIndex(i);
            }
            StaticMethods.Triangulate(vertices, adjacencyList);
            this.repaint();
        }
        
        
        allowToAddVertex = true;
        
    }//GEN-LAST:event_formMousePressed

    private void buttonRandomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRandomActionPerformed
        vertices.clear();
        adjacencyList.clear();
        Random r = new Random();
        int numberOfPointsToAdd = 0;
        try{  
              numberOfPointsToAdd =  Integer.parseInt(textFieldRandom.getText());
        } 
        catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        int x = 0;
        int y = 0;       
        while (vertices.size()<numberOfPointsToAdd) {
            x = 20 + r.nextInt(this.getWidth() - 40);
            y = 30 + r.nextInt(this.getHeight() - 70);
            for (int j = 0; j < vertices.size(); j++) {
                if (vertices.get(j).getX() == x && vertices.get(j).getY() == y) {
                    allowToAddVertex = false;
                    return;
                }
            }
            if (allowToAddVertex) {
                vertices.add(new Vertex(x,y,vertices.size()));
                adjacencyList.add(new ArrayList<Integer>());
            }
            allowToAddVertex = true;
        }
        
        for (ArrayList<Integer> al : adjacencyList) {
                al.clear();
            }
            Collections.sort(vertices, Vertex.COMPARE_BY_X);
            for (int i = 0; i < vertices.size(); i++) {
                vertices.get(i).setIndex(i);
            }
            StaticMethods.Triangulate(vertices, adjacencyList);
        
        this.repaint();
    }//GEN-LAST:event_buttonRandomActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonRandom;
    private javax.swing.JButton buttonTriangulate;
    private javax.swing.JTextField textFieldRandom;
    // End of variables declaration//GEN-END:variables
}
