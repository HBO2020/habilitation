package com.enedis.habilitation.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product sold by the Online store
 */
@Schema(description = "Product sold by the Online store")
@Entity
@Table(name = "habilitation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Habilitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_habilitation", nullable = false)
    private Integer idHabilitation;

    @NotNull
    @Column(name = "cn_alex", nullable = false)
    private String cnAlex;

    @Column(name = "siren")
    private Integer siren;

    @Column(name = "date_maj")
    private Instant dateMaj;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Habilitation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdHabilitation() {
        return this.idHabilitation;
    }

    public Habilitation idHabilitation(Integer idHabilitation) {
        this.setIdHabilitation(idHabilitation);
        return this;
    }

    public void setIdHabilitation(Integer idHabilitation) {
        this.idHabilitation = idHabilitation;
    }

    public String getCnAlex() {
        return this.cnAlex;
    }

    public Habilitation cnAlex(String cnAlex) {
        this.setCnAlex(cnAlex);
        return this;
    }

    public void setCnAlex(String cnAlex) {
        this.cnAlex = cnAlex;
    }

    public Integer getSiren() {
        return this.siren;
    }

    public Habilitation siren(Integer siren) {
        this.setSiren(siren);
        return this;
    }

    public void setSiren(Integer siren) {
        this.siren = siren;
    }

    public Instant getDateMaj() {
        return this.dateMaj;
    }

    public Habilitation dateMaj(Instant dateMaj) {
        this.setDateMaj(dateMaj);
        return this;
    }

    public void setDateMaj(Instant dateMaj) {
        this.dateMaj = dateMaj;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Habilitation)) {
            return false;
        }
        return id != null && id.equals(((Habilitation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Habilitation{" +
            "id=" + getId() +
            ", idHabilitation=" + getIdHabilitation() +
            ", cnAlex='" + getCnAlex() + "'" +
            ", siren=" + getSiren() +
            ", dateMaj='" + getDateMaj() + "'" +
            "}";
    }
}
