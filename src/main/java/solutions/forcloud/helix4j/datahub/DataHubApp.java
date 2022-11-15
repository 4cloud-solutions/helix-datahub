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
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersDataResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersProducersSubscriptionsResource;
import solutions.forcloud.helix4j.datahub.resources.CsidUsersResource;
import solutions.forcloud.helix4j.datahub.resources.SignInResource;
import solutions.forcloud.helix4j.datahub.resources.CsidWebsocketResource;

/**
 * This is the main class of the <b>Data-Hub</b> application.
 * <br><br>
 * <b>Data-Hub</b> is a sample application demonstrating use of the <b>Helix4j</b> framework.<br>
 * The framework requires access to a datagrid (<b>Redis</b>) and a streaming platform (<b>Kafka</b>).<br>
 * Data-Hub is a REST application and takes advantage of the <b>Dropwizard</b> bundle to implement the REST server.<br> 
 * <br>
 * I To start <b>Kafka</b> server:<br>
 * I.1. Start the ZooKeeper service in a shell<br>
 * &nbsp; cd /home/milos/Programs/Kafka<br>
 * &nbsp; bin/zookeeper-server-start.sh config/zookeeper.properties -----&gt; runs on port 2181<br>             
 * I.2. Start the Kafka broker service in a shell<br>
 * &nbsp; cd /home/milos/Programs/Kafka<br>
 * &nbsp; bin/kafka-server-start.sh config/server.properties -----&gt; runs on port 9092<br>
 * I.3. From another shell create replication topics<br>
 * &nbsp; cd /home/milos/Programs/Kafka<br>
 * &nbsp; bin/kafka-topics.sh --create --topic ".datagrid.replication.topic.AtoB" --replication-factor 1 --partitions 1 --bootstrap-server localhost:9092 <br>
 * &nbsp; bin/kafka-topics.sh --create --topic ".datagrid.replication.topic.BtoA" --replication-factor 1 --partitions 1 --bootstrap-server localhost:9092 <br>
 * &nbsp; bin/kafka-topics.sh --list --bootstrap-server localhost:9092<br>
 * &nbsp; bin/kafka-topics.sh --delete --topic ".datagrid.replication.topic.AtoB" --bootstrap-server localhost:9092<br>
 * <br>
 * II To start <b>Redis</b> server:<br>
 * &nbsp; cd /home/milos/Programs/Redis<br>
 * &nbsp; ./redis-server ./conf/redis.conf -----&gt; runs on port 6379<br>
 * &nbsp; ./redis-server ./conf/redis1.conf -----&gt; runs on port 6380<br>
 * II.1 To start a Redis CLI client:<br>
 * &nbsp; cd /home/milos/Programs/Redis<br>
 * &nbsp; ./redis-cli -h localhost -p 6379 | 6380<br>
 * &nbsp; localhost:pppp$<br>
 * <br>
 * III To run <b>Data-Hub</b> application:<br>
 * &nbsp; cd ~/workspaces/workspace-cloud/helix-datahub<br>
 * &nbsp; mvn package<br>
 * &nbsp; java -jar target/helix-datahub-1.0.jar server config.yml<br>
 * &nbsp; config.yml -------- the app server runs on port 9090 - single site (Redis), no Kafka, 1st app server<br>
 * &nbsp; config1.yml ------- the app server runs on port 9091 - single site (Redis), no Kafka, 2nd app server<br>
 * &nbsp; configA.yml ------- the app server runs on port 9090 - 1st site (Redis), with Kafka, 1st app server<br>
 * &nbsp; configA1.yml ------ the app server runs on port 9091 - ist site (Redis), with Kafka, 2nd app server<br>
 * &nbsp; configB.yml ------- the app server runs on port 9096 - 2nd site (Redis), with Kafka, 1st app server<br>
 * <br>
 * IV To run the application and the infrastructure in <b>Docker</b> containers<br>
 * &nbsp; cd ~/workspaces/workspace-cloud/helix-datahub<br>
 * - Build the Docker-Hub<br>
 * &nbsp; mvn package<br>
 * - Build docker image (requires Dockerfile)<br>
 * &nbsp; docker login -u 4cloudsolutions<br>
 * &nbsp; docker build -t helix-datahub .<br>
 * &nbsp; docker image ls<br>
 * - Tag the image<br>
 * &nbsp; docker tag helix-datahub 4cloudsolutions/helix-datahub:v1<br>
 * &nbsp; docker image ls<br>
 * - Push the docker image into the Docker Hub repository<br>
 * &nbsp; docker push 4cloudsolutions/helix-datahub:v1<br>
 * - Run the whole HA setup locally in 6 Docker containers using docker-compose (requires docker-compose-ha.yml file)<br>
 * &nbsp; docker-compose -f docker-compose-ha.yml up<br>
 * <br>
 * <b>Try the following</b>:<br>
 * <br>
 * Data-Hub API:<br>
 * ==================<br>
 * <a href="http://localhost:9090/data-hub/admin" target="_blank">http://localhost:9090/data-hub/admin</a><br>
 * <a href="http://localhost:9090/data-hub/api/healthcheck" target="_blank">http://localhost:9090/data-hub/api/healthcheck</a><br>
 * <a href="http://localhost:9090/data-hub/index.html" target="_blank">http://localhost:9090/data-hub/index.html</a> (A Browser-based REST client - side A)<br>
 * <a href="http://localhost:9096/data-hub/index.html" target="_blank">http://localhost:9096/data-hub/index.html</a> (A Browser-based REST client - side B)<br>
 * <br>
 * Sign-In i.e. create a session:<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/signin?userid=u1" -H  "accept: application/json"<br>
 * Get session details:<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59" -H  "accept: application/json"<br>
 * Refresh the session:<br>
 * curl -X PATCH  "http://localhost:9090/data-hub/api/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59" -H  "accept: application/json"<br>
 * Delete the session:<br>
 * curl -X DELETE "http://localhost:9090/data-hub/api/csid/43506746-9135-4f0c-a79a-d63e8d715aab" -H  "accept: application/json"<br>
 * Get all existing sessions (superadmin):<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/sessions" -H  "accept: application/json"<br>
 * Get all existing connections (superadmin):<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/connections" -H  "accept: application/json"<br>
 * Get all existing users (superadmin):<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/25cd5afb-5ad1-4624-a869-dbd8b6986b59/users" -H  "accept: application/json"<br>
 * Add producer data:<br>
 * curl -X PUT    "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"data\":\"/u1/p1/TEST_DATA\"}"<br>
 * Get producer data:<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json"<br>
 * Delete producer data:<br>
 * curl -X DELETE "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/data" -H  "accept: application/json"<br>
 * Get user data (all producers of a user):<br>
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/data" -H  "accept: application/json"<br>
 * Subscribe:<br>
 * curl -X POST   "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"subscriberId\":\"s1\"}"<br>
 * Check subscription:
 * curl -X GET    "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json"<br>
 * Unsubscribe:
 * curl -X DELETE "http://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/users/u1/producers/p1/subscriptions" -H  "accept: application/json"<br>
 * <br>
 * Websocket: <a href="ws://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/websocket">ws://localhost:9090/data-hub/api/csid/4e103a5b-f42a-4088-a43e-22e634f99189/websocket</a><br>
 * <br>
 * Important Redis objects:<br>
 * ========================<br>
 * - Replication queue:<br> 
 * &nbsp; rpush /datagrid/replication/queue message<br>
 * &nbsp; blpop /datagrid/replication/queue 120 (timeout)<br>
 * - Session Expiry Queue: /datagrid/sessions/expiryqueue<br>
 * - Session Expiry Channel: /datagrid/sessions/expirychannel<br>
 * - Data Change Channel: 
 * &nbsp; publish /datagrid/data/datachangechannel message<br>
 * &nbsp; subscribe /datagrid/data/datachangechannel<br>
 * - User Map: hgetall /datagrid/users<br>
 * - Session Map: hgetall /datagrid/sessions/map<br>
 * - Session Sorted Set: zrange /datagrid/sessions/expiryset 0 -1 withscores<br>
 * - User Connection Map: hgetall /datagrid/users/u1/connections<br>
 * - Node Connection Map: hgetall /datagrid/nodes/A_NODE-0/connections<br>
 * - User Data Map: hgetAll /datagrid/users/u1/data<br> 
 * - Producer Subscription Set: smembers /datagrid/users/u1/producers/p1/subscriptions<br>
 * <br>
 * Kafka Topics:<br>
 * =============<br>
 * - .datagrid.replication.topic.AtoB<br>
 * - .datagrid.replication.topic.BtoA<br>
 * bin/kafka-console-consumer.sh --topic ".datagrid.replication.topic.AtoB" --from-beginning --property print.key=true --bootstrap-server localhost:9092<br>
 * bin/kafka-console-consumer.sh --topic ".datagrid.replication.topic.BtoA" --from-beginning --property print.key=true --bootstrap-server localhost:9092<br>
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
        bootstrap.addBundle(new AssetsBundle("/assets", "/"));
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

        final CsidUsersProducersSubscriptionsResource resourceDSUB = new CsidUsersProducersSubscriptionsResource(configuration);        
        environment.jersey().register(resourceDSUB);
        
        // final PresenceResourcesResource resourcePR = new PresenceResourcesResource(configuration);        
        // environment.jersey().register(resourcePR);
        
        // Test.test(configuration);
    }
    
}
