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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Publish {

    private static String projectId = System.getenv("PROJECT_ID");

    private static String topicId = System.getenv("PUBSUB_TOPIC_ID");




    public static void publishWithErrorHandlerExample(String username, String firstname) throws IOException, InterruptedException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("firstname", firstname);
        String jsonPayload = gson.toJson(jsonObject);

        publishWithJsonPayload(jsonPayload);
    }

    private static void publishWithJsonPayload(String jsonPayload) throws IOException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();

            ByteString data = ByteString.copyFromUtf8(jsonPayload);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            ApiFuture<String> future = publisher.publish(pubsubMessage);

            ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = ((ApiException) throwable);
                        System.out.println(apiException.getStatusCode().getCode());
                        System.out.println(apiException.isRetryable());
                    }
                    System.out.println("Error publishing message: " + jsonPayload);
                }

                @Override
                public void onSuccess(String messageId) {
                        System.out.println("Published message ID: " + messageId);
                }
            }, MoreExecutors.directExecutor());

        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }


}
