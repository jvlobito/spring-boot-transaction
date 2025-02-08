package mx.examvic.com.entities;
//
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

//
@Data
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;
    @NotBlank (message = "El trackId es requerida")
    private String trackId;

    @NotBlank (message= "La referencia es requerida")
    private String reference;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser un número positivo")
    private double amount;

    @NotBlank (message ="El correo es requerido")
    private String email;

    @NotBlank (message ="El tipo de divisa es requerido")
    private String currency;

    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe tener 16 dígitos")
    private String pan;

    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos")
    private String cvv;

    @Pattern(regexp = "(0[1-9]|1[0-2])/(\\d{2})", message = "La fecha de expiración debe estar en formato MM/YY")
    private String expDate;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
