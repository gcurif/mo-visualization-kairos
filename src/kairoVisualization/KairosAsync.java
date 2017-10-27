/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kairoVisualization;


import facialAnalysisCore.FacialAnalyser;
import facialAnalysisCore.FacialAnalysis;
import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author gustavo
 */
public class KairosAsync implements Callable<FacialAnalysis> {

    private FacialAnalyser analyser;
    private String method;
    private File file;
    private FacialAnalysis analysis;
    
    public KairosAsync(FacialAnalyser analyser, String method){
        
       this.analyser= analyser;
       this.method = method;
       
    }
    
    public KairosAsync(FacialAnalyser analyser, String method , FacialAnalysis analysis){
        
       this.analyser= analyser;
       this.method = method;
       this.analysis = analysis;
    }
    
    
     public KairosAsync(FacialAnalyser analyser, String method, String id){
        
       this.analyser= analyser;
       this.method = method;
       
    }
    
     public KairosAsync(FacialAnalyser analyser, String method, File file){
        
       this.analyser= analyser;
       this.method = method;
       this.file = file;
       
    }
    
    @Override
    public FacialAnalysis call() throws Exception {
        
        if(this.method==null){return null;}
        
        if(this.method.equals("uploadVideo")){
            return analyser.uploadVideo(file);
        }
        
        if(this.method.equals("update")){
            return this.analyser.update(this.analysis);
        }
        return null;
    }
    
}
