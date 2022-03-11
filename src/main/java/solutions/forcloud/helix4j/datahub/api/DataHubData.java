/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;


/**
 *
 * @author mpujic
 */
@Data
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataHubData {

    @JsonProperty
    private String data;
    
    
    // Getters, Setters, toString() and hashCode() are auto-generated 
    // by Lombok based on the @Data annotation
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    public DataHubData() {
    }
    
    public DataHubData(String data) {
        this.data = data;
    }
    
    public DataHubData fromFlat(String jsonStr) {
        DataHubData object = null;
        if (jsonStr != null) {
            try {
                object = new ObjectMapper().readValue(jsonStr, DataHubData.class);
            } catch (JsonProcessingException ex) {
                // LOGGER.error(null, ex);
            }
        }
        return object;
    }

    public String toFlat() {
        String jsonStr = null;
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            // LOGGER.error(null, ex);
        }
        return jsonStr;
    }
}
