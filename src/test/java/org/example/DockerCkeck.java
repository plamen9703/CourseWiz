package org.example;
import org.testcontainers.DockerClientFactory;
public class DockerCkeck {

    public static void main(String[] args) {
        if (DockerClientFactory.instance().isDockerAvailable()) {
            System.out.println("✅ Docker is available and running!");
        } else {
            System.out.println("❌ Docker is NOT available.");
        }
    }
}
