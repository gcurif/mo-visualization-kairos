/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kairoVisualization;

import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mo.analysis.NotesAnalysisPlugin.logger;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;



@Extension(
        xtends = {
            @Extends(
                    extensionPointId = "mo.visualization.VisualizationProvider"
            )
        }
)

public class KairoFacialAnalisisVisPlugin implements VisualizationProvider{
    
    
    private final static String PLUGIN_NAME = "Kairos facial Analysis Visualization";
    
    List<Configuration> configs;
    private static final Logger logger = Logger.getLogger(KairoFacialAnalisisVisPlugin.class.getCanonicalName());

        
    
    public KairoFacialAnalisisVisPlugin() {
        configs = new ArrayList<>();
    }


    
    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization organization) {
        
        
        KairosFacialAnalysisVisConfigDialog d = new KairosFacialAnalysisVisConfigDialog();
        boolean accepted = d.showDialog();

        System.out.println("*********************");
        System.out.println(d.getConfigurationName());
        System.out.println("*********************");

        
        if (accepted) {
            KairosFacialVisConfig c = new KairosFacialVisConfig(d.getConfigurationName());
            configs.add(c);
            
            return c;

        }
        return null;
        }
    

    @Override
    public List<Configuration> getConfigurations() {
        return this.configs;
    }

    @Override
    public StagePlugin fromFile(File file) {
        
        File ol =  new File(file.getParentFile().getParentFile().getPath());        
        if (file.isFile()) {
            try {

                KairoFacialAnalisisVisPlugin mc = new KairoFacialAnalisisVisPlugin();
                XElement root = XIO.readUTF(new FileInputStream(file));
                XElement[] pathsX = root.getElements("path");
                for (XElement pathX : pathsX) {
                    String path = pathX.getString(); 
                     KairosFacialVisConfig c = new KairosFacialVisConfig();
                                              
                  //  Configuration config = c.fromFile(new File(file.getParentFile(), path));
                      Configuration config = c.fromFile(new File(file.getParentFile(), path));
                                      
                    if (config != null) {
                        mc.configs.add(config);
                    }
                }
                return mc;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } 
        }
        return null;
    }

    @Override
    public File toFile(File parent) {
        File file = new File(parent, "kairosVisualization-visualization.xml");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        XElement root = new XElement("visualization");
        for (Configuration config : configs) {
            File p = new File(parent, "kairosVisualization-analysis");
            p.mkdirs();
            File f = config.toFile(p);

            XElement path = new XElement("path");
            Path parentPath = parent.toPath();
            Path configPath = f.toPath();
            path.setString(parentPath.relativize(configPath).toString());
            root.addElement(path);
        }
        try {
            XIO.writeUTF(root, new FileOutputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return file; 
    }
    
}
