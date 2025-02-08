package mx.examvic.com.controller;

import lombok.extern.slf4j.Slf4j;
import mx.examvic.com.entities.MitecEntity;
import mx.examvic.com.repository.MitecRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mitec")
@Slf4j
public class MitecController {
    private final MitecRepository userRepository;

    public MitecController(MitecRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public MitecEntity createMitec(@RequestBody MitecEntity mitec) {
        log.info("Create Mitec");
        return userRepository.save(mitec);
    }

    @GetMapping
    public List<MitecEntity> getAllMitecs() {
        log.info("Get Mitec");
        return userRepository.findAll();
    }
}

