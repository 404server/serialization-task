package utii.serializationapp.serializer;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import utii.serializationapp.repository.User;
import utii.serializationapp.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Profile("csv")
public class CsvSerializerService implements Serializer {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void serialize(String path) {
        System.out.println("Сериализация в CSV: " + path);
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(path + ".csv")) {
            if (inputStream == null) {
                throw new IOException("Файл не найден");
            }
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();

            MappingIterator<User> it = mapper.readerFor(User.class)
                    .with(schema)
                    .readValues(inputStream);

            List<User> users = it.readAll();
            userRepository.saveAll(users);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deserialize() {
        List<User> users = userRepository.findAll();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("firstName")
                .addColumn("lastName")
                .addColumn("middleName")
                .addColumn("email")
                .addColumn("age")
                .addColumn("sex")
                .setUseHeader(true)
                .build();
        try {
            var file = new File("src/main/resources/users.csv");
            mapper.writer(schema).writeValue(file, users);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}