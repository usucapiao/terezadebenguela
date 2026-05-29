package br.com.instituto.teresa.controller;

import br.com.instituto.teresa.domain.VolunteerBenefit;
import br.com.instituto.teresa.domain.VolunteerPage;
import br.com.instituto.teresa.dto.VolunteerPageRequestDTO;
import br.com.instituto.teresa.repository.VolunteerPageRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteer/page")
public class VolunteerPageController {

    private final VolunteerPageRepository repository;

    public VolunteerPageController(VolunteerPageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<VolunteerPage> getPageContent() {
        return repository.findById(1L)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    @Transactional
    public ResponseEntity<VolunteerPage> updatePageContent(@RequestBody @Valid VolunteerPageRequestDTO dto) {
        VolunteerPage page = repository.findById(1L).orElse(new VolunteerPage());
        page.setId(1L);
        page.setTitle1(dto.getTitle1());
        page.setTitle2(dto.getTitle2());
        page.setDescription(dto.getDescription());
        
        if (dto.getBenefits() != null) {
            page.getBenefits().clear();
            page.getBenefits().addAll(dto.getBenefits().stream()
                .map(b -> new VolunteerBenefit(b.icon(), b.title(), b.description()))
                .collect(Collectors.toList()));
        }
        
        return ResponseEntity.ok(repository.save(page));
    }
}
