package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class VaultController {

    @Autowired
    private VaultService vaultService;

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Object jsonInput) {
        try {
            return vaultService.encryptJson(jsonInput);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error encrypting JSON data";
        }
    }

    @PostMapping("/decrypt")
    public Object decrypt(@RequestBody String ciphertext) {
        try {
            return vaultService.decryptJson(ciphertext, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error decrypting JSON data";
        }
    }

    @PostMapping("/setupTransit")
    public void setupTransit() {
        vaultService.setupTransit();
    }
}
