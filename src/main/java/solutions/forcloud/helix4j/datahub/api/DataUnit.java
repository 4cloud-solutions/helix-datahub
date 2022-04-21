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
