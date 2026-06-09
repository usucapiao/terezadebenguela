package br.com.instituto.teresa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import br.com.instituto.teresa.domain.Volunteer;
import br.com.instituto.teresa.dto.VolunteerRequestDTO;
import br.com.instituto.teresa.repository.VolunteerRepository;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final EmailService emailService;

    public VolunteerService(VolunteerRepository volunteerRepository, EmailService emailService) {
        this.volunteerRepository = volunteerRepository;
        this.emailService = emailService;
    }

    public void processVolunteer(VolunteerRequestDTO dto) {
        Volunteer volunteer = new Volunteer();
        volunteer.setName(dto.name());
        volunteer.setEmail(dto.email());
        volunteer.setPhone(dto.phone());
        volunteer.setAge(dto.age());
        volunteer.setMotivation(dto.motivation());

        volunteerRepository.save(volunteer);

        emailService.sendVolunteerNotification(
            dto.name(), dto.email(), dto.phone(), dto.age(), dto.motivation()
        );
    }

    public Page<Volunteer> listarTodos(Pageable pageable) {
        return volunteerRepository.findAll(pageable);
    }

    public void deletar(@NonNull Long id) {
        if (!volunteerRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Voluntário não encontrado: " + id);
        }
        volunteerRepository.deleteById(id);
    }
}
