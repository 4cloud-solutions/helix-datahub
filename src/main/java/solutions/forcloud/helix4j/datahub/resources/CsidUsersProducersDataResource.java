/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.resources;

import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import solutions.forcloud.helix4j.modules.datamanagement.DataManager;
import solutions.forcloud.helix4j.modules.datamanagement.DataUnit;
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.modules.subscriptionmanagement.SubscriptionManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.datahub.api.DataHubData;

/**
 *
 * @author mpujic
 */
@Timed
@Path("/csid/{csid}/users/{userid}/producers/{producerid}/data")
public class CsidUsersProducersDataResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CsidUsersProducersDataResource.class);

    public static final String RESOURCE_NAME = "USER_PRODUCER_DATA";
    
    public CsidUsersProducersDataResource(DataHubConfig configuration) {
        LOGGER.info("Instantiated!");
    }
    
    /*
     * Add/Replace the producer data of the specified user
    */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DataUnit addUserProducerData(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            @Context HttpHeaders httpHeaders, 
            DataHubData receivedData) {

        LOGGER.debug("addUserProducerData(csid='{}', userId='{}', producerId='{}', receivedData='{}') called!", 
                csid, userId, producerId, receivedData);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.WRITE, userId);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        DataUnit dataUnit = DataManager.addUpdateProducerData(userId, producerId, receivedData.toFlat());

        LOGGER.debug("addUserProducerData(csid='{}', userId='{}', producerId='{}', receivedData='...') stored '{}'!", 
                csid, userId, producerId, dataUnit);

        // Find how manny subscribers there are for this producer
        Long numberOfSubscribers = SubscriptionManager.getPerProducerSubscriptionsNumber(userId, producerId);
        dataUnit.setNumberOfSubscribers(numberOfSubscribers);
        
        return dataUnit;
    }        
    
    /*
     * Get the producer data of the specified user
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DataUnit getUserProducerData(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("getUserProducerData(csid='{}', userId='{}', producerId='{}') called!", 
                csid, userId, producerId);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.READ, userId);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        DataUnit dataUnit = DataManager.getProducerData(userId, producerId);        
        if (dataUnit == null) {
            dataUnit = new DataUnit(userId, producerId, null);
        }
        // Find how manny subscribers there are for this producer
        Long numberOfSubscribers = SubscriptionManager.getPerProducerSubscriptionsNumber(userId, producerId);
        dataUnit.setNumberOfSubscribers(numberOfSubscribers);
        
        LOGGER.debug("getUserProducerData(csid='{}', userId='{}', producerId='{}') returned '{}'!", 
                csid, userId, producerId, dataUnit);
        return dataUnit;
    }
    
    /*
     * Delete the producer data of the specified user
    */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public DataUnit deleteUserProducerData(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @PathParam("producerid") String producerId,
            @QueryParam("remove") @DefaultValue("false") Boolean removeFlag,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("deleteUserProducerData(csid='{}', userId='{}', producerId='{}', removeFlag={}) called!", 
                csid, userId, producerId, removeFlag);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.WRITE, userId);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        boolean success = false;
        DataUnit dataUnit = null;
        if (DataManager.containsProducerData(userId, producerId)) {
            if (removeFlag) {
                dataUnit = DataManager.removeProducerData(userId, producerId);
            } else {
                dataUnit = DataManager.clearProducerData(userId, producerId);
            }
            success = true;
            // Find how manny subscribers there are for this producer
            Long numberOfSubscribers = SubscriptionManager.getPerProducerSubscriptionsNumber(userId, producerId);
            dataUnit.setNumberOfSubscribers(numberOfSubscribers);            
        }
        
        if (!success) {
            throw new NotFoundException();
        }
        
        LOGGER.debug("deleteUserProducerData(csid='{}', userId='{}', producerId='{}') returned='{}'!", 
                csid, userId, producerId, dataUnit);
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
