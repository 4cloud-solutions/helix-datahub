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
import solutions.forcloud.helix4j.modules.datamanagement.DataManager;
import solutions.forcloud.helix4j.modules.datamanagement.DataUnit;
import solutions.forcloud.helix4j.modules.sessionmanagement.ClientSession;
import solutions.forcloud.helix4j.modules.sessionmanagement.SessionManager;
import solutions.forcloud.helix4j.utils.Utilities;
import solutions.forcloud.helix4j.datahub.DataHubConfig;

/**
 *
 * @author mpujic
 * 
 * The path is preceded by elements defined in the config.yml file:
 *   server.applicationContextPath + server.rootPath
 */
@Timed
@Path("/csid/{csid}/users/{userid}/data")
public class CsidUsersDataResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CsidUsersDataResource.class);

    public static final String RESOURCE_NAME = "USER_DATA";
    
    public CsidUsersDataResource(DataHubConfig configuration) {
        LOGGER.info("Instantiated!");
    }
    
    /*
     * Get the data of all producers of a specified user
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataUnit> getUserData(
            @PathParam("csid") String csid, 
            @PathParam("userid") String userId,
            @Context HttpHeaders httpHeaders) {
        
        LOGGER.debug("getUserData(csid='{}', userId='{}') called!", csid, userId);
        
        if (!DatagridClient.isDatagridUp()) {
            throw new ServiceUnavailableException(); // --------------->
        }
        
        ClientSession session = SessionManager.authnAuthzAndGetSession(csid, 
                            Utilities.getAuthTokensFromHttpHeaders(httpHeaders),
                            RESOURCE_NAME, AccessTypesEnum.READ, userId);
        if (session == null) {
            throw new ForbiddenException(); // --------------->
        }
        
        List<DataUnit> response = new ArrayList<>();
        Map<String, DataUnit> userDataMap = DataManager.getUserDataMap(userId);
        for (String producerId : userDataMap.keySet()) {       
            response.add(userDataMap.get(producerId));
        }

        LOGGER.debug("getUserData(csid='{}', userId='{}') returned '{}'!", 
                csid, userId, response);
        return response;
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
