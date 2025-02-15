package utii.serializationapp.serializer;

import org.springframework.stereotype.Service;

@Service
public interface Serializer {
    void serialize(String path);
    void deserialize();
}