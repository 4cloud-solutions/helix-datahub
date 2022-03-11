/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.resources;

import com.codahale.metrics.annotation.Timed;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.datagridclient.DatagridClient;
import solutions.forcloud.helix4j.modules.authorization.AccessTypesEnum;
import solutions.forcloud.helix4j.modules.datamanagement.DataUnit;
import solutions.forcloud.helix4j.modules.datamanagement.DataManager;
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.modules.subscriptionmanagement.SubscriptionManager;
import solutions.forcloud.helix4j.modules.usermanagement.User;
import solutions.forcloud.helix4j.modules.usermanagement.UserManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.datahub.api.Subscriber;

/**
 *
 * @author mpujic
 */
@Timed
@Path("/csid/{csid}/users/{userid}/producers/{producerid}/subscriptions")
public class CsidUsersProducersSubscriptionsResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CsidUsersProducersSubscriptionsResource.class);
    
    public static final String RESOURCE_NAME = "USER_PRODUCER_SUBSCRIPTIONS";
    
    public CsidUsersProducersSubscriptionsResource(DataHubConfig configuration) {
        LOGGER.info("Instantiated!");
    }
    
    /*
     * Add a subscription for producer data
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DataUnit addSubscription(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            Subscriber subscriber,
            @Context HttpHeaders httpHeaders) {

        LOGGER.debug("addSubscription(csid='{}', userId='{}', producerId='{}', subscriberId='{}') called!", 
                csid, userId, producerId, subscriber.getSubscriberId());
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.WRITE, userId, subscriber);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        SubscriptionManager.addSubscription(userId, producerId, csid);
        
        // Get the inital data
        DataUnit dataUnit = DataManager.getProducerData(userId, producerId);
        if (dataUnit == null) {
            dataUnit = new DataUnit(userId, producerId, null);
        }
        
        LOGGER.debug("addSubscription(csid='{}', userId='{}', producerId='{}') returned '{}'!", 
                csid, userId, producerId, dataUnit);
        return dataUnit;
    }        
    
    /*
     * Get all subscriptions for the producer data of the specified user
     * Only the owner and superadmin have full access!
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getSubscriptions(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("getSubscriptions(csid='{}', userId='{}', producerId='{}') called!", 
                csid, userId, producerId);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authenticateAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders));
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        User resourceAccessor = UserManager.getUser(session.getUserId());
        boolean isOwnerOrSuperadmin = (resourceAccessor.isSuperAdmin()) || (resourceAccessor.is(userId));

        Set<String> subscriptions = SubscriptionManager.getPerProducerSubscriptionsSet(userId, producerId);        
        List<String> response = new ArrayList<>();
        for (String aCsid : subscriptions) {
            if (isOwnerOrSuperadmin || (csid.equals(aCsid))) {
                response.add(aCsid);
            }
        }
        
        LOGGER.debug("getSubscriptions(csid='{}', userId='{}', producerId='{}') returned '{}'!", 
                csid, userId, producerId, response);
        return response;
    }
    
    /*
     * Remove a subscriptions for producer data
    */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public DataUnit removeSubscription(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("removeSubscription(csid='{}', userId='{}', producerId='{}') called!", 
                csid, userId, producerId);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.WRITE, userId);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        DataUnit dataUnit = null;
        boolean success = false;
        if (SubscriptionManager.containsSubscription(userId, producerId, csid)) {
            SubscriptionManager.removeSubscription(userId, producerId, csid);
            // Get the current data
            dataUnit = DataManager.getProducerData(userId, producerId);
            if (dataUnit == null) {
                dataUnit = new DataUnit(userId, producerId, null);
            }
            success = true;
        }        
        
        LOGGER.debug("removeSubscription(csid='{}', userId='{}', producerId='{}') success='{}' returned '{}'!", 
                csid, userId, producerId, success, dataUnit);
        if (!success) {
            throw new NotFoundException();
        }        
        return dataUnit;
    }   
    
    /*
     * Respond to CORS OPTIONS pre-flight request
    */
    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public Response allowCors(
            @PathParam("csid") String csid, 
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) {        
        LOGGER.debug("allowCors() called! csid={}, URI info: {}, HTTP request headers: {}", csid, uriInfo.getPath(), httpHeaders.getRequestHeaders());
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Headers", "Authorization,*")
                .build();
    }
           
}
