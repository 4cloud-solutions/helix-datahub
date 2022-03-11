/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import solutions.forcloud.helix4j.HelixConfig;

/**
 *
 * @author milos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataHubConfig extends Configuration {

    @JsonProperty
    private HelixConfig helixConfig;
    
    @JsonProperty
    @Min(60)    
    private Integer sessionTimeToLiveSeconds = 900;   // seconds

    // Getters, Setters, toString() and hashCode() are auto-generated 
    // by Lombok based on the @Data annotation
    
    // To help find usage of the Lombok generated getters
    private void dummy() {
        this.getHelixConfig();
        this.getSessionTimeToLiveSeconds();
    }
       
}
