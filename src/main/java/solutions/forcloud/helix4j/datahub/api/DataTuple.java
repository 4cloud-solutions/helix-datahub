/*
 * Copyright (C) 2022 4cloud Solutions.
 *
 * This file is not part of an Open-Source software.
 * You may not use this file unless you are granted an explicit,
 * "named" license by 4cloud Solutions.
 * To obtain the License, contact
 *
 *         4cloud-solutions@protonmail.com
 *
 * The above copyright notice and the notice below shall be included in
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
public class DataTuple {

    @JsonProperty
    private Long timestamp = null;
    
    @JsonProperty
    private String data = null;
    
    
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    // Should be private so it cannot be explicitly used outside the class
    public DataTuple() {
    }
    
    public DataTuple(String data) {
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }    

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }    

    public String getData() {
        return data;
    }    

    public void setData(String data) {
        this.data = data;
    }    

    public String toString1() {
        return String.format("%s:%s", timestamp, data);
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", DataTuple.class.getSimpleName(), toString1());
    }
    
}
