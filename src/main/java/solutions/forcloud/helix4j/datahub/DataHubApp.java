/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solutions.forcloud.helix4j.datahub;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.forcloud.helix4j.datahub.framework.DataHubFramework;
import solutions.forcloud.helix4j.datahub.health.ConfigurationHealthCheck;
import solutions.forcloud.helix4j.datahub.resources.CsidConnectionsResource;
import solutions.forcloud.helix4j.datahub.resources.HealthCheckResource;
import solutions.forcloud.helix4j.datahub.resources.CsidResource;
import solutions.forcloud.helix4j.datahub.resources.CsidSessionsResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersDataResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersDataResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersSubscriptionsResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersResource;
import solutions.forcloud.helix4j.datahub.resources.SignInResource;
import solutions.forcloud.helix4j.datahub.resources.CsidWebsocketResource;

/**
 * 
 * I To start Kafka server:
 *      1. # Start the ZooKeeper service in a shell
 *          cd /home/milos/Programs/Kafka
 *          bin/zookeeper-server-start.sh config/zookeeper.properties
 *              zookeeper port: 2181             
 *      2. # Start the Kafka broker service in a shell
 *          cd /home/milos/Programs/Kafka
 *          bin/kafka-server-start.sh config/server.properties
 *              broker port: 9092
 *      3. # From another shell
 *          cd /home/milos/Programs/Kafka
 *      3.1 # Create a topic (e.g. "test")
 *          bin/kafka-topics.sh --create --topic ".datagrid.replication.topic.AtoB" --replication-factor 1 --partitions 1 --bootstrap-server localhost:9092
 *          bin/kafka-topics.sh --create --topic ".datagrid.replication.topic.BtoA" --replication-factor 1 --partitions 1 --bootstrap-server localhost:9092
 *      3.2 # List all topics
 *          bin/kafka-topics.sh --list --bootstrap-server localhost:9092
 * 
 * II To start Redis server:
 *
 *      cd /home/milos/Programs/Redis
 *      ./redis-server ./conf/redis.conf ------ runs on port 6379
 *      ./redis-server ./conf/redis1.conf ----- runs on port 6380
 * 
 *    To start a Redis CLI client:
 * 
 *      cd /home/milos/Programs/Redis
 *      ./redis-cli -h 127.0.0.1 -p 6379 | 6380
 *      127.0.0.1:pppp$

 * III To run application:
 *     1. cd ~/workspaces/workspace-cloud/helix-datahub
 *     2. mvn package
 *     3. java -jar target/helix-datahub-1.0-SNAPSHOT.jar server config.yml
 *          config.yml -------- the app server runs on port 9090 - single site (Redis), no Kafka, 1st app server
 *          config1.yml ------- the app server runs on port 9091 - single site (Redis), no Kafka, 2nd app server
 *          configA.yml ------- the app server runs on port 9090 - 1st site (Redis), with Kafka, 1st app server
 *          configA1.yml ------ the app server runs on port 9091 - ist site (Redis), with Kafka, 2nd app server
 *          configB.yml ------- the app server runs on port 9096 - 2nd site (Redis), with Kafka, 1st app server
 *     4. Try the following URLs:

Data-Hub API:
==================

http://localhost:9090/data-hub/admin

http://localhost:9090/data-hub/application/healthcheck

http://localhost:9090/data-hub/application/assets/index.html (A Browser-based REST client)

Sign-In i.e. create a session:
curl -X GET    "http://localhost:9090/data-hub/application/signin?userid=u1" -H  "accept: application/json"
Get session details:
curl -X GET    "http://localhost:9090/data-hub/application/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59" -H  "accept: application/json"
Refresh the session:
curl -X PATCH  "http://localhost:9090/data-hub/application/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59" -H  "accept: application/json"
Delete the session:
curl -X DELETE "http://localhost:9090/data-hub/application/csid/43506746-9135-4f0c-a79a-d63e8d715aab" -H  "accept: application/json"
Get all existing sessions (superadmin):
curl -X GET    "http://localhost:9090/data-hub/application/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/sessions" -H  "accept: application/json"
Get all existing connections (superadmin):
curl -X GET    "http://localhost:9090/data-hub/application/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/connections" -H  "accept: application/json"
Get all existing users (superadmin):
curl -X GET    "http://localhost:9090/data-hub/application/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/users" -H  "accept: application/json"
Add producer data:
curl -X PUT    "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"data\":\"/u1/p1/TEST_DATA\"}"
Get producer data:
curl -X GET    "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json"
Delete producer data:
curl -X DELETE "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json"
Get user data (all producers of a user):
curl -X GET    "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/data" -H  "accept: application/json"
Subscribe:
curl -X POST   "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"subscriberId\":\"s1\"}"
Check subscription:
curl -X GET    "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json"
Unsubscribe:
curl -X DELETE "http://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json"
 
Websocket:        ws://localhost:9090/data-hub/application/csid/4e103a5b-f42a-4088-a43e-22e634f99189/websocket

Important Redis objects:
========================
- Replication queue: rpush /datagrid/replication/queue message
                     blpop /datagrid/replication/queue 120 (timeout)
- Session Expiry Queue: /datagrid/sessions/expiryqueue
- Session Expiry Channel: /datagrid/sessions/expirychannel
- Data Change Channel: publish /datagrid/data/datachangechannel message
                       subscribe /datagrid/data/datachangechannel
- User Map: hgetall /datagrid/users
- Session Map: hgetall /datagrid/sessions/map
- Session Sorted Set: zrange /datagrid/sessions/expiryset 0 -1 withscores
- User Connection Map: hgetall /datagrid/users/u1/connections
- Node Connection Map: hgetall /datagrid/nodes/A_NODE-0/connections
- User Data Map: hgetAll /datagrid/users/u1/data 
- Producer Subscription Set: smembers /datagrid/users/u1/producers/p1/subscriptions

Kafka Topics:
=============
- .datagrid.replication.topic.AtoB
- .datagrid.replication.topic.BtoA
bin/kafka-console-consumer.sh --topic ".datagrid.replication.topic.AtoB" --from-beginning --property print.key=true --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic ".datagrid.replication.topic.BtoA" --from-beginning --property print.key=true --bootstrap-server localhost:9092

 * 
 * @author milos
 */
public class DataHubApp extends Application<DataHubConfig> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHubApp.class);   

    private static final String APPLICATION_NAME = DataHubApp.class.getSimpleName();
    
    public static void main(final String[] args) throws Exception {
        new DataHubApp().run(args);
    }

    @Override
    public String getName() {
        return APPLICATION_NAME;
    }

    @Override
    public void initialize(final Bootstrap<DataHubConfig> bootstrap) {
        LOGGER.info("initialize() called!");
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", "index.html"));
        bootstrap.addBundle(new WebsocketBundle(CsidWebsocketResource.class));
    }

    @Override
    public void run(DataHubConfig configuration, Environment environment) throws Exception {
        
        // Update configuration based on the environmental variables
        configuration.updateFromEnvironment();
        
        LOGGER.info("run() called! Application Name='{}', Environment='{}', Configuration='{}'",
                getName(), environment.getName(), configuration);
        
        // Enable the data collator framework:
        //   datagrid access, replication...
        DataHubFramework.powerUp(configuration);        
        
        /*
         * Register health-check handlers
         */
        
        final ConfigurationHealthCheck healthCheck = new ConfigurationHealthCheck(configuration);        
        environment.healthChecks().register("configuration", healthCheck);
        
        /*
         * Enable CORS headers
         */
        
        final FilterRegistration.Dynamic corsFilter =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        corsFilter.setInitParameter("allowedOrigins", "*");
        corsFilter.setInitParameter("allowedMethods", "*");
        // corsFilter.setInitParameter("allowedMethods", "OPTIONS,PATCH,GET,PUT,POST,DELETE,HEAD");
        corsFilter.setInitParameter("allowedHeaders", "Authorization,*");

        // Add URL mapping
        corsFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // DO NOT pass a preflight request to down-stream auth filters
        // unauthenticated preflight requests should be permitted by spec
        // If included in this app (which does not use auth filters), it breaks CORS!
        // corsFilter.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
        
        /*
         * Register HTTP resources (handlers)
         */
        
        final HealthCheckResource resourceHC = new HealthCheckResource(configuration);        
        environment.jersey().register(resourceHC);

        final SignInResource resourceSI = new SignInResource(configuration);        
        environment.jersey().register(resourceSI);

        final CsidResource resourceCSID = new CsidResource(configuration);        
        environment.jersey().register(resourceCSID);

        final CsidSessionsResource resourceSES = new CsidSessionsResource(configuration);        
        environment.jersey().register(resourceSES);
        
        final CsidConnectionsResource resourceCON = new CsidConnectionsResource(configuration);        
        environment.jersey().register(resourceCON);
        
        final CsidUsersResource resourceUSR = new CsidUsersResource(configuration);        
        environment.jersey().register(resourceUSR);
                
        final CsidUsersProducersDataResource resourcePDATA = new CsidUsersProducersDataResource(configuration);        
        environment.jersey().register(resourcePDATA);

        final CsidUsersDataResource resourceUDATA = new CsidUsersDataResource(configuration);        
        environment.jersey().register(resourceUDATA);
        
        final CsidUsersProducersSubscriptionsResource resourceDSUB = new CsidUsersProducersSubscriptionsResource(configuration);        
        environment.jersey().register(resourceDSUB);
        
        // final PresenceResourcesResource resourcePR = new PresenceResourcesResource(configuration);        
        // environment.jersey().register(resourcePR);
        
        // Test.test(configuration);
    }
    
}
