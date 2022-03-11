/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
