/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.HelixConfig;
import solutions.forcloud.helix4j.datagridclient.DatagridClient;
import solutions.forcloud.helix4j.modules.connectionmanagement.ConnectionManager;
import solutions.forcloud.helix4j.modules.datamanagement.DataManager;
import solutions.forcloud.helix4j.modules.replication.ReplicationFramework;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.modules.subscriptionmanagement.SubscriptionManager;
import solutions.forcloud.helix4j.modules.usermanagement.UserManager;
import solutions.forcloud.helix4j.datahub.handlers.DataHubAccessControlPoint;
import solutions.forcloud.helix4j.datahub.handlers.DataHubDataChangeProcessor;
import solutions.forcloud.helix4j.datahub.handlers.DataHubSessionExpiryProcessor;

/**
 *
 * @author mpujic
 */
public class DataHubFramework {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubFramework.class);
    
    private static HelixConfig helixConfig = null;
    private static Thread powerUpThread = null;
    
    public static void powerUp(DataHubConfig configuration) {
        LOGGER.info("powerUp() called!");
        
        helixConfig = configuration.getHelixConfig();
        
        // Launch a new thread to bring up the framework
        powerUpThread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    DataHubFramework.run();
                }
            }
        );
        powerUpThread.start(); // Calls run() in a new thread
        
        LOGGER.info("powerUp() exited!");
    }
    
    private static void run() {
        LOGGER.info("run() called!");
        
        // Enable datagrid access
        // This will block until the datagrid is available
        DatagridClient.powerUp(helixConfig);
        String datagridInfo = (LOGGER.isDebugEnabled()) ? ("\n" + DatagridClient.printDatagridInformation()) : "";
        LOGGER.info("run() --------- DATAGRID UP! ---------{}", datagridInfo);
        
        // This will block until the datagrid is available
        ReplicationFramework.powerUp(helixConfig);
        LOGGER.info("run() --------- REPLICATION FRAMEWORK UP! ---------");

        // This may block until the datagrid is available
        UserManager.configure(helixConfig);
        LOGGER.info("run() --------- USER MANAGER UP!");
        
        // Strat session management (e.g. this starts the session expiry handler)
        // This may block until the datagrid is available
        SessionManager.powerUp(helixConfig, 
                new DataHubSessionExpiryProcessor("DATA_HUB_" + helixConfig.getNodeId() + "_SESSION_EXPIRY_PROCESSOR"),
                new DataHubAccessControlPoint());
        LOGGER.info("run() --------- SESSION MANAGER UP! ---------");

        ConnectionManager.configure(helixConfig);
        // Clenup connections upon startup
        // This will block until the datagrid is available
        // ConnectionManager0.clenupConnectionsUponNodeRestart();
        LOGGER.info("run() --------- CONNECTION MANAGER UP! ---------");
        
        // This will block until the datagrid is available
        DataManager.powerUp(helixConfig, new DataHubDataChangeProcessor());
        LOGGER.info("run() --------- DATA MANAGER UP!");
        
        SubscriptionManager.configure(helixConfig);
        LOGGER.info("run() --------- SUBSCRIPTION MANAGER UP! ---------");
               
        LOGGER.info("run() --------- DONE! ---------");
    }
    
    public static void powerDown() {
        LOGGER.info("powerDown() called!");

        if (powerUpThread != null) {
            if (powerUpThread.isAlive()) {
                powerUpThread.interrupt();
            }
        }
        
        // Stop the data management (e.g. this stops the Data Change Channel subscriber)
        DataManager.powerDown();
        
        // Clenup connections before going away
        // ConnectionManager0.clenupConnectionsUponNodeRestart();
        
        // Stop session management (e.g. this stops the session expiry handler)
        SessionManager.powerDown();

        ReplicationFramework.powerDown();
        
        DatagridClient.powerDown(); 
        
        LOGGER.info("powerDown() exited!");
    }

}
