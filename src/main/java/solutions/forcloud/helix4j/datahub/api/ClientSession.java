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
 * @author milos
 */
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
    

    public ClientSession() {
    }
    
    public ClientSession(String csid, 
                         String userId,
                         String authToken,
                         Long creationTimestamp,
                         Long expiryTimestamp) {
        this.csid = csid;
        this.userId = userId;
        this.authToken = authToken;
        this.creationTimestamp = creationTimestamp;
        this.expiryTimestamp = expiryTimestamp;
    }
    
    public String getCsid() {
        return csid;
    }    

    public void setCsid(String csid) {
        this.csid = csid;
    }    

    public String getUserId() {
        return userId;
    }    

    public void setUserId(String userId) {
        this.userId = userId;
    }    

    public String getAuthToken() {
        return authToken;
    }    

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }    

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }    

    public void setCreationTimestamp(Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }    

    public Long getExpiryTimestamp() {
        return expiryTimestamp;
    }    

    public void setExpiryTimestamp(Long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }    

}
