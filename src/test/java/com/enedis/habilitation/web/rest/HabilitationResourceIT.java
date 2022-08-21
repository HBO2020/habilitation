package com.enedis.habilitation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.enedis.habilitation.IntegrationTest;
import com.enedis.habilitation.domain.Habilitation;
import com.enedis.habilitation.repository.HabilitationRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HabilitationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HabilitationResourceIT {

    private static final Integer DEFAULT_ID_HABILITATION = 1;
    private static final Integer UPDATED_ID_HABILITATION = 2;

    private static final String DEFAULT_CN_ALEX = "AAAAAAAAAA";
    private static final String UPDATED_CN_ALEX = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIREN = 1;
    private static final Integer UPDATED_SIREN = 2;

    private static final Instant DEFAULT_DATE_MAJ = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MAJ = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/habilitations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HabilitationRepository habilitationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHabilitationMockMvc;

    private Habilitation habilitation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habilitation createEntity(EntityManager em) {
        Habilitation habilitation = new Habilitation()
            .idHabilitation(DEFAULT_ID_HABILITATION)
            .cnAlex(DEFAULT_CN_ALEX)
            .siren(DEFAULT_SIREN)
            .dateMaj(DEFAULT_DATE_MAJ);
        return habilitation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habilitation createUpdatedEntity(EntityManager em) {
        Habilitation habilitation = new Habilitation()
            .idHabilitation(UPDATED_ID_HABILITATION)
            .cnAlex(UPDATED_CN_ALEX)
            .siren(UPDATED_SIREN)
            .dateMaj(UPDATED_DATE_MAJ);
        return habilitation;
    }

    @BeforeEach
    public void initTest() {
        habilitation = createEntity(em);
    }

    @Test
    @Transactional
    void createHabilitation() throws Exception {
        int databaseSizeBeforeCreate = habilitationRepository.findAll().size();
        // Create the Habilitation
        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isCreated());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeCreate + 1);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getIdHabilitation()).isEqualTo(DEFAULT_ID_HABILITATION);
        assertThat(testHabilitation.getCnAlex()).isEqualTo(DEFAULT_CN_ALEX);
        assertThat(testHabilitation.getSiren()).isEqualTo(DEFAULT_SIREN);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);
    }

    @Test
    @Transactional
    void createHabilitationWithExistingId() throws Exception {
        // Create the Habilitation with an existing ID
        habilitation.setId(1L);

        int databaseSizeBeforeCreate = habilitationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdHabilitationIsRequired() throws Exception {
        int databaseSizeBeforeTest = habilitationRepository.findAll().size();
        // set the field null
        habilitation.setIdHabilitation(null);

        // Create the Habilitation, which fails.

        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isBadRequest());

        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCnAlexIsRequired() throws Exception {
        int databaseSizeBeforeTest = habilitationRepository.findAll().size();
        // set the field null
        habilitation.setCnAlex(null);

        // Create the Habilitation, which fails.

        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isBadRequest());

        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHabilitations() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        // Get all the habilitationList
        restHabilitationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habilitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].idHabilitation").value(hasItem(DEFAULT_ID_HABILITATION)))
            .andExpect(jsonPath("$.[*].cnAlex").value(hasItem(DEFAULT_CN_ALEX)))
            .andExpect(jsonPath("$.[*].siren").value(hasItem(DEFAULT_SIREN)))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ.toString())));
    }

    @Test
    @Transactional
    void getHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        // Get the habilitation
        restHabilitationMockMvc
            .perform(get(ENTITY_API_URL_ID, habilitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(habilitation.getId().intValue()))
            .andExpect(jsonPath("$.idHabilitation").value(DEFAULT_ID_HABILITATION))
            .andExpect(jsonPath("$.cnAlex").value(DEFAULT_CN_ALEX))
            .andExpect(jsonPath("$.siren").value(DEFAULT_SIREN))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHabilitation() throws Exception {
        // Get the habilitation
        restHabilitationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation
        Habilitation updatedHabilitation = habilitationRepository.findById(habilitation.getId()).get();
        // Disconnect from session so that the updates on updatedHabilitation are not directly saved in db
        em.detach(updatedHabilitation);
        updatedHabilitation.idHabilitation(UPDATED_ID_HABILITATION).cnAlex(UPDATED_CN_ALEX).siren(UPDATED_SIREN).dateMaj(UPDATED_DATE_MAJ);

        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHabilitation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getIdHabilitation()).isEqualTo(UPDATED_ID_HABILITATION);
        assertThat(testHabilitation.getCnAlex()).isEqualTo(UPDATED_CN_ALEX);
        assertThat(testHabilitation.getSiren()).isEqualTo(UPDATED_SIREN);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void putNonExistingHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, habilitation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHabilitationWithPatch() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation using partial update
        Habilitation partialUpdatedHabilitation = new Habilitation();
        partialUpdatedHabilitation.setId(habilitation.getId());

        partialUpdatedHabilitation.idHabilitation(UPDATED_ID_HABILITATION).cnAlex(UPDATED_CN_ALEX).dateMaj(UPDATED_DATE_MAJ);

        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getIdHabilitation()).isEqualTo(UPDATED_ID_HABILITATION);
        assertThat(testHabilitation.getCnAlex()).isEqualTo(UPDATED_CN_ALEX);
        assertThat(testHabilitation.getSiren()).isEqualTo(DEFAULT_SIREN);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void fullUpdateHabilitationWithPatch() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation using partial update
        Habilitation partialUpdatedHabilitation = new Habilitation();
        partialUpdatedHabilitation.setId(habilitation.getId());

        partialUpdatedHabilitation
            .idHabilitation(UPDATED_ID_HABILITATION)
            .cnAlex(UPDATED_CN_ALEX)
            .siren(UPDATED_SIREN)
            .dateMaj(UPDATED_DATE_MAJ);

        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getIdHabilitation()).isEqualTo(UPDATED_ID_HABILITATION);
        assertThat(testHabilitation.getCnAlex()).isEqualTo(UPDATED_CN_ALEX);
        assertThat(testHabilitation.getSiren()).isEqualTo(UPDATED_SIREN);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void patchNonExistingHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, habilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeDelete = habilitationRepository.findAll().size();

        // Delete the habilitation
        restHabilitationMockMvc
            .perform(delete(ENTITY_API_URL_ID, habilitation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
