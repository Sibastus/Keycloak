package com.chernyshov.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UserInput {
    String INPUT = "user-event-input";

    @Input(INPUT)
    SubscribableChannel input();
}
