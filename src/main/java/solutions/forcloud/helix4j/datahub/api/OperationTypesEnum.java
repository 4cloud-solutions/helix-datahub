/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub.api;

/**
 *
 * @author milos
 */
public enum OperationTypesEnum {
    
    SIGNIN,
    SIGNOUT, 
    SESSIONREFRESH,
    CONNECT,
    CONNECT_ACK,
    DISCONNECT,
    PUBLISH,
    UNPUBLISH,
    REMOVE,
    GETDATA,
    SUBSCRIBE,
    UNSUBSCRIBE,    
    NOTIFY;
    
}
