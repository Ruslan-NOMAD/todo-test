package org.example.ws;

import org.example.BaseApiTest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class WSTest extends BaseApiTest {

    private static final int TIMEOUT_SECONDS = 5;
    static String wsUri = "ws://localhost:8080/ws".trim();



    @Test
    public void testWebSocketConnectionEstablished() throws URISyntaxException, InterruptedException {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        WebSocketClient client = initiateWebSocketClient(connectionLatch, null);

        try {
            boolean isConnected = awaitConnection(client, connectionLatch, TIMEOUT_SECONDS);
            assertTrue(isConnected, "WebSocket connection was not established");
        } finally {
            terminateConnection(client);
        }
    }

    @Test
    public void testWebSocketReconnect() throws URISyntaxException, InterruptedException {
        boolean initialConnect, reconnect;
        WebSocketClient client;

        CountDownLatch firstLatch = new CountDownLatch(1);
        client = initiateWebSocketClient(firstLatch, null);
        try {
            initialConnect = awaitConnection(client, firstLatch, TIMEOUT_SECONDS);
        } finally {
            terminateConnection(client);
        }

        CountDownLatch secondLatch = new CountDownLatch(1);
        client = initiateWebSocketClient(secondLatch, null);
        try {
            reconnect = awaitConnection(client, secondLatch, TIMEOUT_SECONDS);
        } finally {
            terminateConnection(client);
        }

        assertTrue(initialConnect, "Initial connection failed");
        assertTrue(reconnect, "Reconnection attempt failed");
    }

    @Test
    public void testWebSocketReceivesNotification() throws URISyntaxException, InterruptedException {
        AtomicReference<String> messageHolder = new AtomicReference<>();
        CountDownLatch messageLatch = new CountDownLatch(1);
        WebSocketClient client = initiateWebSocketClient(messageLatch, messageHolder);

        try {
            boolean isConnected = awaitConnection(client, messageLatch, TIMEOUT_SECONDS);
            assertTrue(isConnected, "WebSocket connection was not established");

            boolean messageReceived = messageLatch.await(TIMEOUT_SECONDS * 2, java.util.concurrent.TimeUnit.SECONDS);
            assertTrue(messageReceived, "No message received within timeout");

            int taskId = generateTaskId();
            createNewTask(taskId, generateTaskDescription());

            String receivedMessage = messageHolder.get();
            assertNotNull(receivedMessage, "No WebSocket message received");
            assertTrue(receivedMessage.contains("\"id\":" + taskId), "Incorrect task ID in the message");
        } finally {
            terminateConnection(client);
        }
    }


    private WebSocketClient initiateWebSocketClient(CountDownLatch latch, AtomicReference<String> messageHolder) {
        try {
            URI uri = new URI(wsUri);
            System.out.println("WebSocket URI: " + uri);

            return new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    latch.countDown();
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                    if (messageHolder != null) {
                        messageHolder.set(message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                }

                @Override
                public void onError(Exception ex) {
                    fail("WebSocket encountered an error: " + ex.getMessage());
                }
            };
        } catch (URISyntaxException e) {
            fail("Invalid WebSocket URI: " + wsUri + ", " + e.getMessage());
            return null;  // This will not be reached due to the fail statement above
        }
    }




    private boolean awaitConnection(WebSocketClient client, CountDownLatch latch, int timeoutSeconds) throws InterruptedException {
        client.connect();
        return latch.await(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void terminateConnection(WebSocketClient client) {
        if (client != null && client.isOpen()) {
            client.close();
        }
    }

    private int generateTaskId() {
        return (int) (Math.random() * 1000);
    }

    private void createNewTask(int taskId, String description) {

    }

    private String generateTaskDescription() {
        return "Task_" + System.currentTimeMillis();
    }
}
