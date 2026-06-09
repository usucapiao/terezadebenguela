package br.com.instituto.teresa.controller;

import br.com.instituto.teresa.dto.NewsRequestDTO;
import br.com.instituto.teresa.dto.NewsResponseDTO;
import br.com.instituto.teresa.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public Page<NewsResponseDTO> getActiveNews(
            @PageableDefault(size = 20) Pageable pageable) {
        return newsService.getActiveNews(pageable);
    }

    @GetMapping("/all")
    public Page<NewsResponseDTO> getAllNews(
            @PageableDefault(size = 20) Pageable pageable) {
        return newsService.getAllNews(pageable);
    }

    @PostMapping
    public ResponseEntity<NewsResponseDTO> createNews(@RequestBody @Valid NewsRequestDTO dto) {
        return ResponseEntity.ok(newsService.createNews(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDTO> updateNews(@PathVariable long id, @RequestBody @Valid NewsRequestDTO dto) {
        return ResponseEntity.ok(newsService.updateNews(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}