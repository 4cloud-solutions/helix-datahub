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
package solutions.forcloud.helix4j.datahub.handlers;

import solutions.forcloud.helix4j.modules.authorization.AccessControlPoint;
import solutions.forcloud.helix4j.modules.authorization.AccessTypesEnum;
import solutions.forcloud.helix4j.modules.usermanagement.User;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersDataResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersSubscriptionsResource;

/**
 *
 * @author mpujic
 */
public class DataHubAccessControlPoint extends AccessControlPoint {
    
    @Override
    public boolean isResourceAccessAuthorized(
            String resourceName, AccessTypesEnum accessType, User resourceAccessor, 
            String resourceOwnerId, Object auxData) {

        // The superadmin can access any resource
        if (resourceAccessor.isSuperAdmin()) {
            return true; // ---------------->            
        }
        
        // The owner can access own resources
        if (resourceAccessor.is(resourceOwnerId)) {
            return true; // ---------------->            
        }        
        
        if (CsidUsersProducersSubscriptionsResource.RESOURCE_NAME.equals(resourceName)) {
            // Subscriber subscriber = (Subscriber) auxData;
            // For now, allow subcriptions to producer data to everyone
            return true; // ---------------->
        } else if (CsidUsersProducersDataResource.RESOURCE_NAME.equals(resourceName)) {
            // For now, allow getting (but not publishing) producer data to everyone
            return (accessType == AccessTypesEnum.READ); // ---------------->
        }
        
        // Otherwise, access not authorized
        return false;
    }
    
}
