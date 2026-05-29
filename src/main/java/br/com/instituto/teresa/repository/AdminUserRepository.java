package br.com.instituto.teresa.repository;

import br.com.instituto.teresa.domain.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    UserDetails findByLogin(String login);
}
