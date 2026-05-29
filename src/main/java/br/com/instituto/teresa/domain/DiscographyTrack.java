package br.com.instituto.teresa.domain;

import jakarta.persistence.*;

@Entity // Indica que a classe representa uma tabela no banco de dados
@Table(name = "discography_tracks") // Específica o nome customizado da tabela
public class DiscographyTrack {

    @Id // Marca a propriedade como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que o valor será gerado com Auto-Incremento no banco
    private Long id;

    private String code;
    private String title;
    private String artist;
    private String audioFile;

    public DiscographyTrack() {}

    public DiscographyTrack(Long id, String code, String title, String artist, String audioFile) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.artist = artist;
        this.audioFile = audioFile;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getAudioFile() { return audioFile; }
    public void setAudioFile(String audioFile) { this.audioFile = audioFile; }
}
