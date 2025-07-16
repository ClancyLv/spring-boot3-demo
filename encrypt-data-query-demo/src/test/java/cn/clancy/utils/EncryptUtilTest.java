package cn.clancy.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptUtilTest {

    @Test
    void testAesEncryptDecrypt() {
        String originalText = "Hello, World!";
        String encryptedText = EncryptUtil.aesEncrypt(originalText);
        assertNotNull(encryptedText);
        assertNotEquals(originalText, encryptedText);

        String decryptedText = EncryptUtil.aesDecrypt(encryptedText);
        assertEquals(originalText, decryptedText);
    }

    @Test
    void testHmac() {
        String data = "testData";
        String hmac = EncryptUtil.hmac(data);
        assertNotNull(hmac);
        // HMAC should be consistent
        assertEquals(hmac, EncryptUtil.hmac(data));
    }

    @Test
    void testEncryptWithBlankInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EncryptUtil.aesEncrypt("");
        });
        assertEquals("明文不能为空", exception.getMessage());
    }

    @Test
    void testDecryptWithBlankInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EncryptUtil.aesDecrypt("");
        });
        assertEquals("密文不能为空", exception.getMessage());
    }

    @Test
    void testHmacWithBlankInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EncryptUtil.hmac("");
        });
        assertEquals("被摘要数据不能为空", exception.getMessage());
    }
}
