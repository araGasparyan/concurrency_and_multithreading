package org.thread.messagebus;

public record Message(String topic, String payload) {}