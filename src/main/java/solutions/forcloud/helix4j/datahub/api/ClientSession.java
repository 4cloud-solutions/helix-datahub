/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author milos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientSession {

    // Client Session ID (csid)
    @JsonProperty
    private String csid = "0";    

    // A session belongs to a user
    @JsonProperty
    private String userId = "anonymous@invalid";    

    // The auth token asserting the user's identity
    @JsonProperty
    private String authToken = null;    

    @JsonProperty
    private Long creationTimestamp = System.currentTimeMillis();
    
    @JsonProperty
    private Long expiryTimestamp = Long.MAX_VALUE;
    
    // Getters, Setters, toString() and hashCode() are auto-generated 
    // by Lombok based on the @Data annotation
    
}
