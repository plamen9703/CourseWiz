package org.example.integrations;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.example.CoursewizApplication;
import org.example.CoursewizConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTests {

    public static final DropwizardAppExtension<CoursewizConfiguration> APP =
            new DropwizardAppExtension<>(CoursewizApplication.class, "test-config.yml");

    private static Client client;

    @BeforeAll
    static void runMigrations() throws Exception{
//        APP.getApplication().run("db","migrate", "test-config.yml");
        client=APP.client();
    }

    @Test
    void testCreateUser(){
        String newUserJson = """
                {
                    "username":"Alice",
                    "email":"alice@example.com",
                    "password":"alice123"
                }
                """;
        Response response = client.target("http://localhost:" + APP.getLocalPort() + "/api/user/student/register")
                .request()
                .post(Entity.entity(newUserJson, APPLICATION_JSON_TYPE));

        assertEquals(201,response.getStatus());
        String responseBody = response.readEntity(String.class);
        assertTrue(responseBody.contains("username:Alice"));
        assertTrue(responseBody.contains("email:alice@example.com"));
    }
}
