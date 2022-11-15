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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 * @author milos
 */
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataUnit {

    @JsonProperty
    private DataProducer producer = null;
    
    @JsonProperty
    private DataTuple dataTuple = null;
    
    @JsonProperty
    private String targetedConnectionCsid = null;
    
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    // Should be private so it cannot be explicitly used outside the class
    public DataUnit() {
    }
    
    public DataUnit(String userId, String producerId, String data) {
        this.producer = new DataProducer(userId, producerId);
        this.dataTuple = new DataTuple(data);
    }
    
    public DataUnit(String userId, String producerId, DataTuple dataTuple) {
        this.producer = new DataProducer(userId, producerId);
        this.dataTuple = dataTuple;
    }
    
    public DataProducer getProducer() {
        return producer;
    }    

    public void setProducer(DataProducer producer) {
        this.producer = producer;
    }    

    public DataTuple getDataTuple() {
        return dataTuple;
    }    

    public void setDataTuple(DataTuple dataTuple) {
        this.dataTuple = dataTuple;
    }
    
    public String getTargetedConnectionCsid() {
        return targetedConnectionCsid;
    }    

    public void setTargetedConnectionCsid(String targetedConnectionCsid) {
        this.targetedConnectionCsid = targetedConnectionCsid;
    }    

    // Convenience function
    @JsonIgnore
    public String getUserId() {
        return producer.getUserId();
    }    

    // Convenience function
    @JsonIgnore
    public String getProducerId() {
        return producer.getProducerId();
    }
    
    @JsonIgnore
    public Long getTimestamp() {
        return dataTuple.getTimestamp();
    }    

    @JsonIgnore
    public String getData() {
        return dataTuple.getData();
    }    

    public String toString1() {
        return String.format("%s > %s%s", 
                (producer == null) ?  "NULL-PRODUCER" : producer.toString1(), 
                (dataTuple == null) ? "null" : dataTuple.toString1(),
                (targetedConnectionCsid == null) ? "" : (" @" + targetedConnectionCsid));
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", DataUnit.class.getSimpleName(), toString1());
    }
    
}
