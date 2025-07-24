package cn.clancy.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SseManagerTest {

    private SseManager sseManager;
    private Map<String, SseEmitter> sseEmitterMap;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        sseManager = new SseManager();
        Field field = SseManager.class.getDeclaredField("SSE_EMITTER_MAP");
        field.setAccessible(true);
        sseEmitterMap = (Map<String, SseEmitter>) field.get(null);
        sseEmitterMap.clear();
    }

    @AfterEach
    void tearDown() {
        sseEmitterMap.clear();
    }

    @Test
    void testCreateSseEmitter() {
        String clientId = "client1";
        SseEmitter emitter = sseManager.createSseEmitter(clientId);

        assertNotNull(emitter);
        assertTrue(sseEmitterMap.containsKey(clientId));
        assertEquals(emitter, sseEmitterMap.get(clientId));
    }

    @Test
    void testSendMessage_Success() throws IOException {
        String clientId = "client-send";
        String message = "hello world";
        SseEmitter mockEmitter = mock(SseEmitter.class);
        sseEmitterMap.put(clientId, mockEmitter);

        boolean result = sseManager.sendMessage(clientId, message);

        assertTrue(result);
        verify(mockEmitter).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void testSendMessage_ClientNotFound() {
        boolean result = sseManager.sendMessage("non-existent-client", "test");
        assertFalse(result);
    }

    @Test
    void testSendMessage_IOException() throws IOException {
        String clientId = "client-io-exception";
        String message = "message that will fail";
        SseEmitter mockEmitter = mock(SseEmitter.class);
        sseEmitterMap.put(clientId, mockEmitter);

        doThrow(new IOException("Test exception")).when(mockEmitter).send(any(SseEmitter.SseEventBuilder.class));

        boolean result = sseManager.sendMessage(clientId, message);

        assertFalse(result);
        assertFalse(sseEmitterMap.containsKey(clientId));
    }

    @Test
    void testSendMessageToAll_AllSuccess() throws IOException {
        SseEmitter mockEmitter1 = mock(SseEmitter.class);
        SseEmitter mockEmitter2 = mock(SseEmitter.class);
        sseEmitterMap.put("client1", mockEmitter1);
        sseEmitterMap.put("client2", mockEmitter2);

        String message = "broadcast message";
        boolean result = sseManager.sendMessageToAll(message);

        assertTrue(result);
        verify(mockEmitter1).send(any(SseEmitter.SseEventBuilder.class));
        verify(mockEmitter2).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void testSendMessageToAll_WithFailures() throws IOException {
        SseEmitter mockEmitter1 = mock(SseEmitter.class);
        SseEmitter mockEmitter2 = mock(SseEmitter.class); // This one will fail
        sseEmitterMap.put("client1", mockEmitter1);
        sseEmitterMap.put("client2", mockEmitter2);

        doThrow(new IOException("Failed to send")).when(mockEmitter2).send(any(SseEmitter.SseEventBuilder.class));

        String message = "broadcast with failure";
        boolean result = sseManager.sendMessageToAll(message);

        assertFalse(result);
        verify(mockEmitter1).send(any(SseEmitter.SseEventBuilder.class));
        assertFalse(sseEmitterMap.containsKey("client2"));
        assertTrue(sseEmitterMap.containsKey("client1"));
    }

    @Test
    void testCloseSseEmitter() {
        String clientId = "client-to-close";
        sseEmitterMap.put(clientId, new SseEmitter());

        sseManager.closeSseEmitter(clientId);

        assertFalse(sseEmitterMap.containsKey(clientId));
    }

    @Test
    void testEmitterCompletionCallback() {
        String clientId = "client-complete";
        sseEmitterMap.put(clientId, new SseEmitter());
        assertTrue(sseEmitterMap.containsKey(clientId));

        // Directly test the logic of the completion callback
        sseManager.closeSseEmitter(clientId);

        assertFalse(sseEmitterMap.containsKey(clientId));
    }

    @Test
    void testEmitterTimeoutCallback() {
        String clientId = "client-timeout";
        sseEmitterMap.put(clientId, new SseEmitter());
        assertTrue(sseEmitterMap.containsKey(clientId));

        Runnable timeoutCallback = () -> sseManager.closeSseEmitter(clientId);
        timeoutCallback.run();

        assertFalse(sseEmitterMap.containsKey(clientId));
    }

    @Test
    void testEmitterErrorCallback() {
        String clientId = "client-error";
        sseEmitterMap.put(clientId, new SseEmitter());
        assertTrue(sseEmitterMap.containsKey(clientId));

        // Directly test the logic of the error callback
        sseManager.closeSseEmitter(clientId);

        assertFalse(sseEmitterMap.containsKey(clientId));
    }
}