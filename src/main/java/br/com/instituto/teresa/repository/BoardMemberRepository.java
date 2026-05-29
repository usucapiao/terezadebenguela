package br.com.instituto.teresa.repository;

import br.com.instituto.teresa.domain.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    Optional<BoardMember> findByRole(String role);
}
