package utii.serializationapp.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import utii.serializationapp.repository.User;
import utii.serializationapp.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Profile("json")
public class JsonSerializerService implements Serializer {
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper mapper;

    public JsonSerializerService() {
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(String path) {
        System.out.println("Сериализация в JSON: " + path);
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Файл не найден");
            }

            User[] users = mapper.readValue(inputStream, User[].class);
            userRepository.saveAll(Arrays.asList(users));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deserialize() {
        List<User> users = userRepository.findAll();
        try {
            var file = new File("src/main/resources/users.json");
            mapper.writeValue(file, users);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}