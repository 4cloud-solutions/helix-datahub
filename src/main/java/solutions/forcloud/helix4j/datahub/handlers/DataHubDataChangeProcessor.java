/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.handlers;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.modules.connectionmanagement.ClientConnectionDescriptor;
import solutions.forcloud.helix4j.modules.datamanagement.DataUnit;
import solutions.forcloud.helix4j.modules.datamanagement.DataChangeProcessorIF;


/**
 *
 * @author milos
 */
public class DataHubDataChangeProcessor implements DataChangeProcessorIF {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubDataChangeProcessor.class);
    
    @Override
    public void processDataChange(DataUnit dataChange, Set<ClientConnectionDescriptor> subscribingLocalConnections) {
        LOGGER.debug("processDataChange() Processing data change: data={}, subscribingConnections={}!", 
                     dataChange, subscribingLocalConnections);
        // Do application specific stuff
        // None for Data-Hub
    }
    
}
