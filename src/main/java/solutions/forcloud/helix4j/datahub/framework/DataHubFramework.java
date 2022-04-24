/*
 * The MIT License
 *
 * Copyright (C) 2022 4cloud Solutions.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package solutions.forcloud.helix4j.datahub.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.HelixConfig;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.datahub.handlers.DataHubAccessControlPoint;
import solutions.forcloud.helix4j.datahub.handlers.DataHubDataChangeProcessor;
import solutions.forcloud.helix4j.datahub.handlers.DataHubSessionExpiryProcessor;
import solutions.forcloud.helix4j.framework.HelixFullFramework;

/**
 *
 * @author mpujic
 */
public class DataHubFramework {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubFramework.class);
    
    private static DataHubConfig dataHubConfig = null;
    
    public static void powerUp(DataHubConfig configuration) {
        LOGGER.info("powerUp() called!");
        
        dataHubConfig = configuration;
        HelixConfig helixConfig = configuration.getHelixConfig();
        DataHubSessionExpiryProcessor dataHubSessionExpiryProcessor = 
                new DataHubSessionExpiryProcessor("DATA_HUB_" + helixConfig.getNodeId() + "_SESSION_EXPIRY_PROCESSOR");
        DataHubAccessControlPoint dataHubAccessControPoint = 
                new DataHubAccessControlPoint();
        DataHubDataChangeProcessor dataHubDataChangeProcessor = 
                new DataHubDataChangeProcessor();
        
        HelixFullFramework.powerUp(helixConfig, 
                               dataHubAccessControPoint, 
                               dataHubSessionExpiryProcessor, 
                               dataHubDataChangeProcessor);
        
        LOGGER.info("powerUp() exited!");
    }
    
    public static DataHubConfig getDataHubConfig() {
        return dataHubConfig;
    }
    
    public static void powerDown() {
        LOGGER.info("powerDown() called!");

        HelixFullFramework.powerDown();
        
        LOGGER.info("powerDown() exited!");
    }

}
