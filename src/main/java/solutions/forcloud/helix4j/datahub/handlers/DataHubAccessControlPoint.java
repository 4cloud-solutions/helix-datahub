/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
