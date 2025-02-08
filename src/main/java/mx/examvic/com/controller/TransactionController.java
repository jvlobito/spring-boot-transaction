package mx.examvic.com.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.examvic.com.entities.Transaction;
import mx.examvic.com.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transaction Controller", description = "API para gestionar transacciones")
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar una transacción", description = "Guarda una transacción en la base de datos con validaciones.")
    public ResponseEntity<?> saveTransaction(@Valid @RequestBody Transaction transaction, BindingResult result)
        {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Transaction savedTransaction = transactionService.saveTransaction(transaction);
            return ResponseEntity.ok(Collections.singletonMap("message", "Transaction saved successfully"));
        } catch (Exception e) {
            log.info("Salvar transaccion");
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/consult/{trackID}")
    @Operation(summary = "Consultar transacción por trackID", description = "Recupera una transacción usando su trackID.")
    public ResponseEntity<?> getTransaction(@PathVariable String trackID) {
        Optional<Transaction> transaction = transactionService.getTransactionByTrackID(trackID);
        log.info("Consulta por trackID");
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body((Transaction) Collections.singletonMap("error", "Transaction not found")));
    }


    @GetMapping("/consult/{startDate}/{endDate}")
    @Operation(summary = "Consultar transacciones por fecha", description = "Recupera transacciones dentro de un rango de fechas.")
    public ResponseEntity<?> getTransactionsByDateRange(@PathVariable String startDate, @PathVariable String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate start = LocalDate.parse(startDate + " 00:00:00", formatter);
            LocalDate end = LocalDate.parse(endDate + " 23:59:59", formatter);

            List<Transaction> transactions = transactionService.getTransactionsByDateRange(start, end);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(404).body("No transactions found in the given date range.");
            }
            log.info("Consulta por periodo");
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid date format. Use: yyyy-MM-dd");
        }
    }
}
