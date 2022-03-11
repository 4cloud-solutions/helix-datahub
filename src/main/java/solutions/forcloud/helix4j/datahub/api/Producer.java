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
 * @author mpujic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Producer {

    @JsonProperty
    private String userId;
    
    @JsonProperty
    private String producerId;
    
    // Getters, Setters, toString() and hashCode() are auto-generated 
    // by Lombok based on the @Data annotation
    
    public String toString1() {
        return String.format("%s:%s", userId, producerId);
    }
    
}
