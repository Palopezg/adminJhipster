package ar.com.pablo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Characteristic.
 */
@Document(collection = "characteristic")
public class Characteristic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("characteristic_id")
    private String characteristicId;

    @Field("service_id")
    private String serviceId;

    @Field("descripcion")
    private String descripcion;

    @DBRef
    @Field("serviceType")
    @JsonIgnoreProperties("characteristics")
    private ServiceType serviceType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    public Characteristic characteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
        return this;
    }

    public void setCharacteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Characteristic serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Characteristic descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public Characteristic serviceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Characteristic)) {
            return false;
        }
        return id != null && id.equals(((Characteristic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
            "id=" + getId() +
            ", characteristicId='" + getCharacteristicId() + "'" +
            ", serviceId='" + getServiceId() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
