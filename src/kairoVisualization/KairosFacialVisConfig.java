/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kairoVisualization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mo.organization.Configuration;
import mo.visualization.Playable;
import mo.visualization.VisualizableConfiguration;


public class KairosFacialVisConfig implements VisualizableConfiguration {

    String name;
    String id;
    private final String[] creators = {"mo.kairosanalysisplugin.KairosAnalyser.complete"};
    private List<File> files;
        private boolean stopped;
        private KairosFaPlayer player;

        
    public KairosFacialVisConfig(){
    
    }
    
    KairosFacialVisConfig(String configName) {
        this.name=configName;
        this.id=configName;
        files = new ArrayList<>();
        //player = new FaPlayer();
    }
    
    

    @Override
    public List<String> getCompatibleCreators() {
        return Arrays.asList(creators);
    }

    @Override
    public void addFile(File file) {
        if (!files.contains(file)) {
            files.add(file);
        }    
    }

    @Override
    public void removeFile(File file) {
        File toRemove = null;
        for (File f : files) {
            if (f.equals(file)) {
                toRemove = f;
            }
        }
        
        if (toRemove != null) {
            files.remove(toRemove);
        }
    }

    private void ensurePlayerCreated() {
        if (player == null && !files.isEmpty()) {
            player = new KairosFaPlayer(files.get(0));
        }
    }
    
    @Override
    public Playable getPlayer() {
        ensurePlayerCreated();
        return player;
    }

    @Override
    public String getId() {
        return this.id;    }

    @Override
    public File toFile(File parent) {
        
        File f = new File(parent, "kairosVisualization_"+id+".xml");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(KairosFacialVisConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return f;

    }

    @Override
    public Configuration fromFile(File file) {
        String fileName = file.getName();
        String user, key , name;


        if (fileName.contains("_") && fileName.contains(".")) {
            name = fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("."));
            KairosFacialVisConfig config = new KairosFacialVisConfig(name);
            config.id = name;
            return config;
        }        
 
        
        return null;
          
    }
    
}
