package br.com.instituto.teresa.repository;

import br.com.instituto.teresa.domain.VolunteerPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerPageRepository extends JpaRepository<VolunteerPage, Long> {
}
