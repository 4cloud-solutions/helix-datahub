/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.datamodel.ProcessorIF;
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;

/**
 *
 * @author milos
 */
public class DataHubSessionExpiryProcessor implements ProcessorIF<ClientSession> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubSessionExpiryProcessor.class);
    
    private final String processorName;
    
    
    public DataHubSessionExpiryProcessor(String processorName) {
        this.processorName = processorName;
    }
    
    @Override
    public void process(ClientSession expiredSession) {
        LOGGER.debug("process() {} processing expiry for session {}!", processorName, expiredSession);
        // Do application specific stuff
        // None for Data-Hub
    }
    
}
