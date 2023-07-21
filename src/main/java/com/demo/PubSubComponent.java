package com.demo;

import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.protobuf.Duration;
import com.google.pubsub.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class PubSubComponent {

    public void pullMessage(String projectId, String topicId, String subscription, MessageReceiver receiver) throws IOException {
        pullMessage(projectId, topicId, subscription, null, receiver);
    }

    public void pullMessage(String projectId, String topicId, String subscriptionId, Long awaitTimeout, MessageReceiver receiver) throws IOException {
        createSubscription(projectId, topicId, subscriptionId);
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            subscriber.startAsync().awaitRunning();
            log.info(String.format("Listening for messages on %s:\n", subscriptionName.toString()));
        } catch (Exception timeoutException) {
            if (subscriber != null) {
                subscriber.stopAsync();
            }

        }
    }


    public void createSubscription(String projectId, String topicId, String subscription) throws IOException {
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            TopicName topicName = TopicName.of(projectId, topicId);
            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscription);
            ExpirationPolicy expirationPolicy = ExpirationPolicy.newBuilder().setTtl(Duration.newBuilder().setSeconds(315576000000L)).build();
            Subscription.newBuilder().setExpirationPolicy(expirationPolicy);
            Subscription request =
                    Subscription.newBuilder()
                            .setName(subscriptionName.toString())
                            .setTopic(topicName.toString())
                            .setPushConfig(PushConfig.getDefaultInstance())
                            .setAckDeadlineSeconds(600)
                            .setExpirationPolicy(expirationPolicy)
                            .build();
            Subscription returnedSubscription = subscriptionAdminClient.createSubscription(request);
            log.info("subscription created {}", returnedSubscription);
        } catch (AlreadyExistsException alreadyExistsException) {
            log.info("subscription exist {}", subscription);

        }
    }


}
