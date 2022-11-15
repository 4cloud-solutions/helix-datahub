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
import java.util.List;

/**
 *
 * @author milos
 */
// Ignore null fields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataUnits {

    @JsonProperty
    private DataProducer producer = null;
    
    @JsonProperty
    List<DataTuple> dataTuples = null;
    
        
    // No-arguments constructor is required for JSON --> Object mapping by Jackson
    // Should be private so it cannot be explicitly used outside the class
    public DataUnits() {
    }
    
    public DataUnits(DataProducer producer, List<DataTuple> dataTuples) {
        this.producer = producer;
        this.dataTuples = dataTuples;
    }
    
    public DataProducer getProducer() {
        return producer;
    }    

    public void setProducer(DataProducer producer) {
        this.producer = producer;
    }    

    public List<DataTuple> getDataTuples() {
        return dataTuples;
    }    

    public void setDataTuples(List<DataTuple> dataTuples) {
        this.dataTuples = dataTuples;
    }    

}
