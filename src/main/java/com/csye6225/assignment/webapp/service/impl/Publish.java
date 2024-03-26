package com.csye6225.assignment.webapp.service.impl;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Component
public class Publish {
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    @Value("${PROJECT_ID}")
    private  String projectId;
    @Value("${PUBSUB_TOPIC_ID}")
    private  String topicId;



    public void publishWithErrorHandlerExample(String username, String firstname) throws IOException, InterruptedException {
        LOGGER.debug("Inside publish {}");

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("firstname", firstname);
        String jsonPayload = gson.toJson(jsonObject);
        LOGGER.debug("Inside converting to json");

        publishWithJsonPayload(jsonPayload);
    }

    private  void publishWithJsonPayload(String jsonPayload) throws IOException, InterruptedException {
        LOGGER.debug("beforeinitializingtopic");

        TopicName topicName = TopicName.of(projectId, topicId);
        LOGGER.debug("jsonPayload");
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topicName).build();
            LOGGER.debug("after publisher");
            ByteString data = ByteString.copyFromUtf8(jsonPayload);
            LOGGER.debug("before publisher message");
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            LOGGER.debug("after publisher PubsubMessage");
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            LOGGER.debug("after publisher ApiFuture");

            ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = ((ApiException) throwable);
                        System.out.println(apiException.getStatusCode().getCode());
                        System.out.println(apiException.isRetryable());
                    }
                    System.out.println("Error publishing message: " + jsonPayload);
                    LOGGER.debug("Error publishing message: " + jsonPayload);

                }

                @Override
                public void onSuccess(String messageId) {
                        System.out.println("Published message ID: " + messageId);
                    LOGGER.debug("sucess publishing message: " + messageId);

                }
            }, MoreExecutors.directExecutor());

        } finally {
            if (publisher != null) {
                LOGGER.debug("publisher is not null " );

                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
                LOGGER.debug("publisher close" );

            }
        }
    }


}
