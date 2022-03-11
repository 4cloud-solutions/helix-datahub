/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.resources;

import com.codahale.metrics.annotation.Timed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
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
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.modules.usermanagement.User;
import solutions.forcloud.helix4j.modules.usermanagement.UserManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;

/**
 *
 * @author mpujic
 */
@Timed
@Path("/csid/{csid}/users")
public class CsidUsersResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CsidUsersResource.class);

    public static final String RESOURCE_NAME = "USERS";
    
    public CsidUsersResource(DataHubConfig configuration) {
        LOGGER.info("Instantiated!");
    }
    
    /*
     * Get all users
     *     Only a SUPER-ADMIN can make this API call
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(
            @PathParam("csid") String csid, 
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("getUsers(csid={}) called!", csid);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.READ);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        List<User> userList = new ArrayList<>();
        Map<String, User> userMap = UserManager.userMap().getAll();
        for (String userId : userMap.keySet()) {
            userList.add(userMap.get(userId));
        }
        
        LOGGER.debug("getUsers(csid={}) returned {}", csid, userList);
        return userList;
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
