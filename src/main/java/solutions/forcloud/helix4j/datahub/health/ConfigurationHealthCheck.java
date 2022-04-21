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
package solutions.forcloud.helix4j.datahub.health;

import com.codahale.metrics.health.HealthCheck;
import solutions.forcloud.helix4j.datahub.DataHubConfig;

/**
 *
 * @author mpujic
 */
public class ConfigurationHealthCheck extends HealthCheck {
    
    private final DataHubConfig configuration;

    public ConfigurationHealthCheck(DataHubConfig configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Result check() throws Exception {
/*
        final Integer defaultSsn = configuration.getDefaultSsn();
        if (!(defaultSsn > 0)) {
            return Result.unhealthy("Invalid default SSN (" + defaultSsn + ")!");
        }
        final String datagridName = configuration.getDatagridName();
        if (datagridName.isEmpty()) {
            return Result.unhealthy("Invalid Data-grid Name (" + datagridName + ")!");
        }
        final String datagridLookupGroups = configuration.getDatagridLookupGroups();
        if (datagridLookupGroups.isEmpty()) {
            return Result.unhealthy("Invalid Data-grid Lookup Groups (" + datagridLookupGroups + ")!");
        }
        final String datagridLookupLocators = configuration.getDatagridLookupLocators();
        if (datagridLookupLocators.isEmpty()) {
            return Result.unhealthy("Invalid Data-grid Lookup Locators (" + datagridLookupLocators + ")!");
        }
*/
        return Result.healthy();
    }
}
