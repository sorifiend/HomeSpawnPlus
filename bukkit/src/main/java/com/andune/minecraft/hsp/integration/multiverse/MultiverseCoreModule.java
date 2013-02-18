/**
 * 
 */
package com.andune.minecraft.hsp.integration.multiverse;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import com.andune.minecraft.commonlib.Logger;
import com.andune.minecraft.commonlib.LoggerFactory;

import com.andune.minecraft.hsp.Initializable;
import com.andune.minecraft.hsp.config.ConfigCore;

/**
 * @author andune
 *
 */
@Singleton
public class MultiverseCoreModule implements MultiverseCore, Initializable {
    private static final Logger log = LoggerFactory.getLogger(MultiverseCoreModule.class);

    private final Plugin plugin;
    private final ConfigCore configCore;
    private MultiverseListener multiverseListener;
    private MultiverseSafeTeleporter teleporter;
    private String currentTeleporter;

    @Inject
    public MultiverseCoreModule(ConfigCore configCore, Plugin plugin) {
        this.configCore = configCore;
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        if( configCore.isMultiverseEnabled() ) {    // enabled in config?
            Plugin p = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
            if( p != null )                         // plugin exists?
                return p.isEnabled();               // plugin is enabled?
        }

        // if we get here, this module is not enabled
        return false;
    }

    @Override
    public String getVersion() {
        Plugin p = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if( p != null )
            return p.getDescription().getVersion();
        else
            return null;
    }
    
    /**
     * Package use only.
     * 
     * @return
     */
    protected MultiverseListener getMultiverseListener() {
        return multiverseListener;
    }

    @Override
    public void init() throws Exception {
        if( !isEnabled() )
            return;
        
        Plugin p = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if( p != null ) {
            if( p.getDescription().getVersion().startsWith("2.4") ) {
                com.onarandombox.MultiverseCore.MultiverseCore multiverse = (com.onarandombox.MultiverseCore.MultiverseCore) p;
            
                if( multiverse != null ) {
                    log.debug("Hooking Multiverse");
                    teleporter = new MultiverseSafeTeleporter(multiverse, this);
                    teleporter.install();
                    this.multiverseListener = new MultiverseListener();

                    registerListener();
                }
            }
        }
    }

    @Override
    public void shutdown() throws Exception {
        if( teleporter != null ) {
            teleporter.uninstall();
            teleporter = null;
        }
    }

    @Override
    public int getInitPriority() {
        return 9;
    }

    @Override
    public String getCurrentTeleporter() {
        return currentTeleporter;
    }

    @Override
    public void setCurrentTeleporter(String name) {
        currentTeleporter = name;
    }

    private void registerListener() {
        Plugin p = plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if( p != null ) {
            plugin.getServer().getPluginManager().registerEvent(com.onarandombox.MultiverseCore.event.MVTeleportEvent.class,
                    multiverseListener,
                    EventPriority.NORMAL,
                    new EventExecutor() {
                        public void execute(Listener listener, Event event) throws EventException {
                            try {
                                multiverseListener.onMultiverseTeleport((com.onarandombox.MultiverseCore.event.MVTeleportEvent) event);
                            } catch (Throwable t) {
                                throw new EventException(t);
                            }
                        }
                    },
                    plugin);
        }
    }
}