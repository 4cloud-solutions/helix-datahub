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


/**
 *
 * @author mpujic
 */
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataHubData {

    @JsonProperty
    private String data;
    
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    public DataHubData() {
    }
    
    public DataHubData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }    

    public void setData(String data) {
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
