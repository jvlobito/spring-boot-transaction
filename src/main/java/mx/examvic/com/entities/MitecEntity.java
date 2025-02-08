package mx.examvic.com.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "mitec")
public class MitecEntity {
    @Id
    private String id;
    private String name;
    private String email;
}
