/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.resources;

import solutions.forcloud.helix4j.websockets.BaseWebsocketEndpointConfig;

/**
 *
 * @author milos
 */
public class CsidWebsocketEndpointConfig extends BaseWebsocketEndpointConfig  {

    @Override
    public String extractCsid(String path) {
        // Websocket resource path is "/data-hub/application/csid/{csid}/websocket"
        String csid = path.split("/")[4];
        return csid;
    }

}