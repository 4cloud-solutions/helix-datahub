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
import solutions.forcloud.helix4j.datamodel.DataUnit;
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.modules.subscriptionmanagement.SubscriptionManager;
import solutions.forcloud.helix4j.modules.usermanagement.User;
import solutions.forcloud.helix4j.modules.usermanagement.UserManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.datahub.api.DataSubscriber;
import solutions.forcloud.helix4j.framework.HelixFramework;

/**
 *
 * @author mpujic
 * 
 * The path is preceded by elements defined in the config.yml file:
 *   server.applicationContextPath + server.rootPath
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
            DataSubscriber subscriber,
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
        
        // Add subscription and get the inital data
        DataUnit dataUnit = SubscriptionManager.addSubscription(userId, producerId, csid);
                
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
            dataUnit = HelixFramework.getDataManager().getProducerData(userId, producerId);
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
