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
 * @author milos
 */
@Data
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataUnit {

    @JsonProperty
    private String userId = null;
    
    @JsonProperty
    private String producerId = null;
    
    @JsonProperty
    private Long timestamp = null;
    
    // Producer data
    @JsonProperty
    private String data = null;
    
    @JsonProperty
    private String targetedConnectionCsid = null;
    
    // Getters, Setters, toString() and hashCode() are auto-generated 
    // by Lombok based on the @Data annotation
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    // Should be private so it cannot be explicitly used outside the class
    public DataUnit() {
    }
    
    public DataUnit(String userId, String producerId, String data) {
        this.userId = userId;
        this.producerId = producerId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    @JsonProperty
    public void resetData(String data) {
        this.data = data;
        this.timestamp = System.currentTimeMillis();        
    }

    public DataUnit fromFlat(String jsonStr) {
        DataUnit object = null;
        if (jsonStr != null) {
            try {
                object = new ObjectMapper().readValue(jsonStr, DataUnit.class);
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

    public String toString1() {
        return String.format("%s:%s:%s%s > %s", userId, producerId, timestamp,
                (targetedConnectionCsid == null) ? "" : (":" + targetedConnectionCsid), 
                data);
    }
    
}
