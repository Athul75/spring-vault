package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultMount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VaultService {

    @Autowired
    private VaultTemplate vaultTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String encryptJson(Object data) throws JsonProcessingException {
        String jsonData = objectMapper.writeValueAsString(data);
        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
        return transitOperations.encrypt("demo-key", jsonData);
    }

    public <T> T decryptJson(String ciphertext, Class<T> valueType) throws JsonProcessingException {
        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
        String decryptedData = transitOperations.decrypt("demo-key", ciphertext);
        return objectMapper.readValue(decryptedData, valueType);
    }

    public void setupTransit() {
        VaultSysOperations sysOperations = vaultTemplate.opsForSys();
        if (!sysOperations.getMounts().containsKey("transit/")) {
            sysOperations.mount("transit", VaultMount.create("transit"));
            VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
            transitOperations.createKey("demo-key");
        }
    }
}