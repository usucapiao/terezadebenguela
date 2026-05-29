package br.com.instituto.teresa.dto;

public record DiscographyTrackResponseDTO(
    Long id,
    String code,
    String title,
    String artist,
    String audioFile
) {}
