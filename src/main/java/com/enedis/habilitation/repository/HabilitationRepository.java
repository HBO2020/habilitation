package com.enedis.habilitation.repository;

import com.enedis.habilitation.domain.Habilitation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Habilitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HabilitationRepository extends JpaRepository<Habilitation, Long> {}
