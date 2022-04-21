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
