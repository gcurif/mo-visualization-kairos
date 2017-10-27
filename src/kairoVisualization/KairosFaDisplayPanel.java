/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kairoVisualization;

import facialAnalysisCore.FacialAnalysis;
import facialAnalysisCore.Instant;
import facialAnalysisCore.Person;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author gustavo
 */
public class KairosFaDisplayPanel extends javax.swing.JPanel {

   private Person source;
   private JFreeChart grafic;
   private ValueMarker marker;
   private long offset;
    
    public JPanel getPanel(){
        return this;
    }
    

    
    public KairosFaDisplayPanel(Person source, long offset) {
        initComponents();
        this.offset = offset;
        //this.offset=0;
        
        if(source != null){
            this.grafic=this.makeGraficPerson(source);
            ChartPanel panel = new ChartPanel(this.grafic); 
            this.setLayout(new java.awt.BorderLayout());
            this.add(panel);   
            this.validate();
            
            //crando marcador
           this.marker= new ValueMarker(0);
            marker.setStroke(new BasicStroke(1.0f));
            marker.setPaint(Color.black);
           this.grafic.getXYPlot().addDomainMarker(marker);

        }else{
            //nada
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void display(Instant instant){
        this.marker.setValue(instant.getTime());
    }
    
     public void clear(){
        this.marker.setValue(0);
    }
     
    public void seekMarker(long value){
        this.marker.setValue(value-offset);
    }
    
    public void remove(){
    }
    
   public JFreeChart makeGraficPerson(Person p){
        
           JFreeChart grafic = null  ;
           XYSeries series0 = new XYSeries(p.getEmotion(0).getName());
           XYSeries series1 = new XYSeries(p.getEmotion(1).getName());
           XYSeries series2 = new XYSeries(p.getEmotion(2).getName());
           XYSeries series3 = new XYSeries(p.getEmotion(3).getName());
           XYSeries series4 = new XYSeries(p.getEmotion(4).getName());
           XYSeries series5 = new XYSeries(p.getEmotion(5).getName());
           
           for(int i=0; i<p.getEmotion(0).getInstants().size();i++){   
                series0.add(p.getEmotion(0).getInstants().get(i).getTime()-offset,p.getEmotion(0).getInstants().get(i).getValue());
                series1.add(p.getEmotion(1).getInstants().get(i).getTime()-offset,p.getEmotion(1).getInstants().get(i).getValue());
                series2.add(p.getEmotion(2).getInstants().get(i).getTime()-offset,p.getEmotion(2).getInstants().get(i).getValue());
                series3.add(p.getEmotion(3).getInstants().get(i).getTime()-offset,p.getEmotion(3).getInstants().get(i).getValue());
                series4.add(p.getEmotion(4).getInstants().get(i).getTime()-offset,p.getEmotion(4).getInstants().get(i).getValue());
                series5.add(p.getEmotion(5).getInstants().get(i).getTime()-offset,p.getEmotion(5).getInstants().get(i).getValue());
           }
           
           XYSeriesCollection dataset = new XYSeriesCollection();
           
           dataset.addSeries(series0);           
           dataset.addSeries(series1);
           dataset.addSeries(series2);
           dataset.addSeries(series3);
           dataset.addSeries(series4);
           dataset.addSeries(series5);
           
           
           
           grafic = ChartFactory.createXYLineChart("Emotions","","Nivel", dataset);

           grafic.setAntiAlias(true);
           grafic.setBackgroundPaint(Color.WHITE);
           grafic.getPlot().setOutlineVisible(false);
           grafic.getPlot().setBackgroundPaint(Color.WHITE);
           grafic.setBorderVisible(false);
                           grafic.getXYPlot().setDomainGridlinesVisible(false);
                grafic.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                grafic.getXYPlot().getRangeAxis().setAxisLineVisible(false);
                grafic.setTitle("");
               
                 
          // grafic.getXYPlot().s
                
           return grafic;       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
