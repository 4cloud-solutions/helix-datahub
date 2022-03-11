/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.resources;

import com.codahale.metrics.annotation.Timed;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
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
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;

/**
 *
 * @author mpujic
 */
@Timed
@Path("/signin")
public class SignInResource  {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInResource.class);
    
    private final Integer sessionsSecondsToLive;

    public SignInResource(DataHubConfig configuration) {
        this.sessionsSecondsToLive = configuration.getSessionTimeToLiveSeconds();
        LOGGER.info("Instantiated! sessionsSecondsToLive={}", sessionsSecondsToLive);
    }
    
    /*
     * Sign-in - Get the Client Session
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClientSession signIn(
            @QueryParam("userid") String userId,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("signIn(userId={}) called!", userId);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        if (userId == null) {
            throw new BadRequestException(); // --------------->
        }
        
        ClientSession session = SessionManager.authenticateAndCreateSession(
                                        userId, 
                                        Utilities.getAuthTokensFromHttpHeaders(httpHeaders), 
                                        sessionsSecondsToLive);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }

        LOGGER.debug("signIn(userId={}) returned '{}'", userId, session);
        session.setAuthToken("HIDDEN");
        return session;
    }
    
    /*
     * Respond to CORS OPTIONS pre-flight request
    */
    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public Response allowCors(
            @Context UriInfo uriInfo,
            @Context HttpHeaders httpHeaders) {        
        LOGGER.debug("allowCors() called! URI info: {}, HTTP request headers: {}", uriInfo.getPath(), httpHeaders.getRequestHeaders());
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Allow-Headers", "Authorization,*")
                .build();
    }
           
}
