package com.enedis.habilitation.service;

import com.enedis.habilitation.domain.Habilitation;
import com.enedis.habilitation.repository.HabilitationRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Habilitation}.
 */
@Service
@Transactional
public class HabilitationService {

    private final Logger log = LoggerFactory.getLogger(HabilitationService.class);

    private final HabilitationRepository habilitationRepository;

    public HabilitationService(HabilitationRepository habilitationRepository) {
        this.habilitationRepository = habilitationRepository;
    }

    /**
     * Save a habilitation.
     *
     * @param habilitation the entity to save.
     * @return the persisted entity.
     */
    public Habilitation save(Habilitation habilitation) {
        log.debug("Request to save Habilitation : {}", habilitation);
        return habilitationRepository.save(habilitation);
    }

    /**
     * Update a habilitation.
     *
     * @param habilitation the entity to save.
     * @return the persisted entity.
     */
    public Habilitation update(Habilitation habilitation) {
        log.debug("Request to save Habilitation : {}", habilitation);
        return habilitationRepository.save(habilitation);
    }

    /**
     * Partially update a habilitation.
     *
     * @param habilitation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Habilitation> partialUpdate(Habilitation habilitation) {
        log.debug("Request to partially update Habilitation : {}", habilitation);

        return habilitationRepository
            .findById(habilitation.getId())
            .map(existingHabilitation -> {
                if (habilitation.getIdHabilitation() != null) {
                    existingHabilitation.setIdHabilitation(habilitation.getIdHabilitation());
                }
                if (habilitation.getCnAlex() != null) {
                    existingHabilitation.setCnAlex(habilitation.getCnAlex());
                }
                if (habilitation.getSiren() != null) {
                    existingHabilitation.setSiren(habilitation.getSiren());
                }
                if (habilitation.getDateMaj() != null) {
                    existingHabilitation.setDateMaj(habilitation.getDateMaj());
                }

                return existingHabilitation;
            })
            .map(habilitationRepository::save);
    }

    /**
     * Get all the habilitations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Habilitation> findAll() {
        log.debug("Request to get all Habilitations");
        return habilitationRepository.findAll();
    }

    /**
     * Get one habilitation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Habilitation> findOne(Long id) {
        log.debug("Request to get Habilitation : {}", id);
        return habilitationRepository.findById(id);
    }

    /**
     * Delete the habilitation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Habilitation : {}", id);
        habilitationRepository.deleteById(id);
    }
}
