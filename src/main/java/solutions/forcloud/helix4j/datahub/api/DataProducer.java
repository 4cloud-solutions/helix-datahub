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


/**
 *
 * @author mpujic
 */
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProducer {

    @JsonProperty
    private String userId;
    
    @JsonProperty
    private String producerId;
    
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    public DataProducer() {        
    }
    
    public DataProducer(String userId, String producerId) {
        this.userId = userId;
        this.producerId = producerId;        
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getProducerId() {
        return producerId;
    }
    
    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }
    
    public String toString1() {
        return String.format("%s:%s", userId, producerId);
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", DataProducer.class.getSimpleName(), toString1());
    }
    
}
