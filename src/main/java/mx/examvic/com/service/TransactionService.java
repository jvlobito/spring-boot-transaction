package mx.examvic.com.service;

import mx.examvic.com.entities.Transaction;
import mx.examvic.com.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    // Algoritmo de Luhn para validar número de tarjeta
    public boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Validar si la fecha de expiración es válida (no está vencida)
    public boolean isValidExpirationDate(String expDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth cardExpDate = YearMonth.parse(expDate, formatter);
        YearMonth currentMonth = YearMonth.now();
        return cardExpDate.isAfter(currentMonth) || cardExpDate.equals(currentMonth);
    }

    public boolean isDuplicate(String trackId) {
        return transactionRepository.findByTrackId(trackId).isPresent();
    }

    public boolean isCardDataValid(Transaction transaction) {
        String pan = transaction.getPan();
        String cvv = transaction.getCvv();
        String expDate = transaction.getExpDate();

        return pan != null && pan.matches("\\d{16}") &&  // Tarjeta de 16 dígitos
                cvv != null && cvv.matches("\\d{3}") &&   // CVV de 3 dígitos
                expDate != null && expDate.matches("\\d{2}/\\d{2}"); // MM/YY
    }

    public Transaction saveTransaction(Transaction transaction) throws Exception {
        if ( transaction.getTrackId() == null || transaction.getTrackId().isEmpty()) {
            throw new Exception("Transacción no puede ser null");
        }
        if (isDuplicate(transaction.getTrackId())) {
            throw new Exception("Transacción duplicada");
        }
        if (transaction.getAmount() <= 0) {
            throw new Exception("Monto no permitido");
        }
        if (!isCardDataValid(transaction)) {
            throw new Exception("Datos de tarjeta invalido (cvv 3 digitos, pan 16 digitos, exDate MM/YY)");
        }

        transaction.setCreatedAt(LocalDate.now());
        transaction.setUpdatedAt(LocalDate.now());
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionByTrackID(String trackId) {
        return transactionRepository.findByTrackId(trackId);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }
}

