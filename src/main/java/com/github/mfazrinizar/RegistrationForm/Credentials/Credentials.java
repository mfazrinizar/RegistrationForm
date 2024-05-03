package com.github.mfazrinizar.RegistrationForm.Credentials;

/*
 * Author       : M. Fazri Nizar
 * Institution  : Sriwijaya University
 * GitHub       : github.com/mfazrinizar
 * File Name    : Credentials.java
 */

import com.github.mfazrinizar.RegistrationForm.Util.PasswordHasher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Credentials {
    private final String ACCOUNT_ID;
    private final String DATABASE_ID;
    private final String API_URL;
    private final String BEARER_TOKEN;

    public Credentials() {
        final Map<String, String> env = loadEnv();
        final PasswordHasher hasher = new PasswordHasher();
        this.ACCOUNT_ID = env.get("ACCOUNT_ID");
        this.DATABASE_ID = env.get("DATABASE_ID");
        this.API_URL = "https://api.cloudflare.com/client/v4/accounts/" + hasher.urlBase64Decode(ACCOUNT_ID)
                + "/d1/database/"
                + hasher.urlBase64Decode(DATABASE_ID) + "/query";
        this.BEARER_TOKEN = hasher.urlBase64Decode(env.get("BEARER_TOKEN"));
    }

    public String getAPI_URL() {
        return this.API_URL;
    }

    public String getBEARER_TOKEN() {
        return this.BEARER_TOKEN;
    }

    public String getACCOUNT_ID() {
        return this.ACCOUNT_ID;
    }

    public String getDATABASE_ID() {
        return this.DATABASE_ID;
    }

    private Map<String, String> loadEnv() {
        Map<String, String> env = new HashMap<>();
        try {
            try (BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/java/com/github/mfazrinizar/RegistrationForm/Credentials/.env"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        env.put(parts[0], parts[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return env;
    }
}