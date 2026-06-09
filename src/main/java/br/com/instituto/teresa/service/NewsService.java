package br.com.instituto.teresa.service;

import br.com.instituto.teresa.domain.News;
import br.com.instituto.teresa.dto.NewsRequestDTO;
import br.com.instituto.teresa.dto.NewsResponseDTO;
import br.com.instituto.teresa.repository.NewsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final FileStorageService fileStorageService;

    public NewsService(NewsRepository newsRepository, FileStorageService fileStorageService) {
        this.newsRepository = newsRepository;
        this.fileStorageService = fileStorageService;
    }

    public Page<NewsResponseDTO> getActiveNews(Pageable pageable) {
        Pageable sorted = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.DESC, "publishedAt"))
        );
        return newsRepository.findByActiveTrue(sorted).map(this::mapToDTO);
    }

    public Page<NewsResponseDTO> getAllNews(Pageable pageable) {
        Pageable sorted = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.DESC, "publishedAt"))
        );
        return newsRepository.findAll(sorted).map(this::mapToDTO);
    }

    @Transactional
    public NewsResponseDTO createNews(NewsRequestDTO dto) {
        News news = new News();
        applyDto(news, dto);
        return mapToDTO(newsRepository.save(news));
    }

    @Transactional
    public NewsResponseDTO updateNews(long id, NewsRequestDTO dto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notícia não encontrada: " + id));
        applyDto(news, dto);
        return mapToDTO(newsRepository.save(Objects.requireNonNull(news)));
    }

    @Transactional
    public void deleteNews(long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notícia não encontrada: " + id));
        fileStorageService.deleteIfUploaded(news.getImageUrl());
        newsRepository.delete(news);
    }

    private void applyDto(News news, NewsRequestDTO dto) {
        news.setTitle(dto.title());
        news.setSummary(dto.summary());
        news.setContent(dto.content());
        news.setImageUrl(dto.imageUrl());
        news.setVideoUrl(dto.videoUrl());
        news.setPublishedAt(dto.publishedAt());
        news.setActive(dto.active());
    }

    private NewsResponseDTO mapToDTO(News news) {
        return new NewsResponseDTO(
                news.getId(),
                news.getTitle(),
                news.getSummary(),
                news.getContent(),
                news.getImageUrl(),
                news.getVideoUrl(),
                news.getPublishedAt(),
                news.isActive()
        );
    }
}