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
