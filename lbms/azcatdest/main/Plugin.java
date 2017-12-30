/*
 * Created on Nov 16, 2005
 */
package lbms.azcatdest.main;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import lbms.azcatdest.gui.View;

import org.eclipse.swt.widgets.Display;
import com.biglybt.pif.PluginConfig;
import com.biglybt.pif.PluginInterface;
import com.biglybt.pif.download.Download;
import com.biglybt.pif.download.DownloadManager;
import com.biglybt.pif.download.DownloadManagerListener;
//import com.biglybt.pif.torrent.TorrentAttribute;
import com.biglybt.pif.ui.UIInstance;
import com.biglybt.pif.ui.UIManager;
import com.biglybt.pif.ui.UIManagerListener;
import com.biglybt.pif.ui.model.BasicPluginConfigModel;
import com.biglybt.ui.swt.pif.UISWTInstance;
import com.biglybt.ui.swt.pif.UISWTViewEventListener;


public class Plugin implements com.biglybt.pif.Plugin {

    PluginInterface pluginInterface;
    private static PluginInterface pi;
    private static Display display;
    private static Properties properties;
    private static File confFile;
	
    
    //new API startup code
    UISWTInstance swtInstance = null;
    UISWTViewEventListener myView = null;
	
	@Override
	public void initialize(final PluginInterface pluginInterface) {
        
		this.pluginInterface = pluginInterface;

        pi = pluginInterface;
        
        UIManager   ui_manager = pluginInterface.getUIManager();
        BasicPluginConfigModel config_model = ui_manager.createBasicPluginConfigModel( "plugins", "plugin.azcatdest");
        
        //settings on main options panel
        //config_model.addBooleanParameter2("userspace_military_time","userspace.military.time",false);
        config_model.addBooleanParameter2("azcatdest_auto_open","azcatdest.auto.open",false);
        
        pluginInterface.getUIManager().addUIListener(new UIManagerListener() {
            @Override
            public void UIAttached(UIInstance instance) {
              if (instance instanceof UISWTInstance) {
                swtInstance = (UISWTInstance)instance;
                display = swtInstance.getDisplay();
                myView = new View(pluginInterface);
                swtInstance.addView(UISWTInstance.VIEW_MAIN, View.VIEWID, myView);
                if(isPluginAutoOpen()){
                    swtInstance.openMainView(View.VIEWID,myView,null);
                }
              }
            }

            @Override
            public void UIDetached(UIInstance instance) {
              if (instance instanceof UISWTInstance) {
                swtInstance = null;
              }
            }
          });
        confFile = new File(Plugin.getPluginInterface().getPluginDirectoryName(),"azcatdest.settings");
        properties = new Properties();
        if (confFile.exists() && confFile.canRead()) {
        	FileInputStream fin = null;
        	try {
				fin = new FileInputStream(confFile);
				properties.loadFromXML(fin);
			}  catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPropertiesFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally {
				if (fin!=null) try { fin.close(); } catch (IOException e) {}}
        }
        
        
        DownloadManager dm = pluginInterface.getDownloadManager();
        //final TorrentAttribute ta = pluginInterface.getTorrentManager().getAttribute(TorrentAttribute.TA_CATEGORY);
        
        dm.addListener(
        		new DownloadManagerListener()
        		{                    
        			@Override
			        public void downloadAdded(Download dl) {
        				if (!dl.isComplete())	//add only if the download isn't already complete
        					dl.addListener(AzCatDestDownloadListener.getInstance()); //attach DownloadListener                        
        			}
        			
        			@Override
			        public void downloadRemoved(Download arg0) {
        				// Do nothing                        
        			}                    
        		});
        
    
                    
        /*Download[] dls = dm.getDownloads();        
        for (Download dl:dls) {        	
        	dl.addListener(AzCatDestDownloadListener.getInstance()); //attach DownloadListener
        }*/
	}
    
    
    
    /**
     * Gets the pluginInterface from  Plugin.java
     * @return pluginInterface
     */
    public static PluginInterface getPluginInterface(){
        return pi;
    }
    
    /**
     * Gets the Display from  Plugin.java from the UISWTInstance
     * @return display
     */
    public static Display getDisplay(){
        return display;
    }
    
    /**
     * Returns the user set status of whether or not the plugin should autoOpen
     * @return boolean autoOpen
     */
    public static boolean isPluginAutoOpen(){
        PluginConfig config_getter = getPluginInterface().getPluginconfig();
        return config_getter.getPluginBooleanParameter("azcatdest_auto_open",false);
    }



	/**
	 * @return Returns the properties.
	 */
	public static Properties getProperties() {
		return properties;
	}
	
	public static void saveConfig() {
		if (properties!=null) {
			FileOutputStream fos = null;		
			try {
				if (!confFile.exists()) confFile.createNewFile();
				fos = new FileOutputStream(confFile);
				properties.storeToXML(fos, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
//EOF
}
	
	
	
		

	
	









