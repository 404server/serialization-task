package utii.serializationapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utii.serializationapp.serializer.Serializer;

@SpringBootApplication
public class SerializationAppApplication implements CommandLineRunner {
    @Autowired
    private Serializer serializer;

    public static void main(String[] args) {
        SpringApplication.run(SerializationAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        switch (args[2]) {
            case "1" -> serializer.serialize(args[1]);
            case "2" -> serializer.deserialize();
            default -> System.out.println("Chose the action");
        }
    }
}
