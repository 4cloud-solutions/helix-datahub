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
    
    public void updateFromEnvironment() {
        helixConfig.updateFromEnvironment();
        
        String key = "DATAHUB_SESSION_TIME_TO_LIVE_SECONDS";
        String value = System.getenv(key);
        if ((value != null) && !value.isEmpty()) {
            sessionTimeToLiveSeconds = Integer.valueOf(value);
        }
    }   

// To help find usage of the Lombok generated getters
    private void dummy() {
        this.getHelixConfig();
        this.getSessionTimeToLiveSeconds();
    }
       
}
