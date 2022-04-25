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
import io.dropwizard.jersey.PATCH;
import javax.ws.rs.DELETE;
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
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;
import solutions.forcloud.helix4j.datahub.api.Result;

/**
 *
 * @author mpujic
 * 
 * The path is preceded by elements defined in the config.yml file:
 *   server.applicationContextPath + server.rootPath
 */
@Timed
@Path("/csid/{csid}")
public class CsidResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CsidResource.class);
    
    private final Integer sessionsSecondsToLive;

    public CsidResource(DataHubConfig configuration) {
        this.sessionsSecondsToLive = configuration.getSessionTimeToLiveSeconds();
        LOGGER.info("Instantiated! sessionsSecondsToLive={}", sessionsSecondsToLive);
    }
    
    /*
     * Get a specific session
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClientSession getSession(
            @PathParam("csid") String csid,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("getSession(csid='{}') called!", csid);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authenticateAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders));
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        LOGGER.debug("getSession(csid='{}') returned {}", csid, session);
        session.setAuthToken("HIDDEN");
        return session;
    }
    
    /*
     * Refresh a specific session (so it does not expire)
    */
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public ClientSession refreshSession(
            @PathParam("csid") String csid, 
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("refreshSession(csid='{}') called!", csid);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authenticateAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders));
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        session = SessionManager.refreshSession(csid, sessionsSecondsToLive);
        
        LOGGER.debug("refreshSession(csid='{}') returned {}", csid, session);
        session.setAuthToken("HIDDEN");
        return session;
    }
    
    /*
     * Delete a specific session
    */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteSession(
            @PathParam("csid") String csid, 
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("deleteSession(csid='{}') called!", csid);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authenticateAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders));
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        // Delete the externalized data
        SessionManager.deleteSession(csid);
        
        LOGGER.debug("deleteSession(csid='{}') success!", csid);
        return new Result(true);
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
