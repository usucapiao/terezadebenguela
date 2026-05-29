package br.com.instituto.teresa.repository;

import br.com.instituto.teresa.domain.DiscographyTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscographyTrackRepository extends JpaRepository<DiscographyTrack, Long> {
}
