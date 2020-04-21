package ar.com.pablo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceType.
 */
@Document(collection = "service_type")
public class ServiceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("service_id")
    private String serviceId;

    @Field("descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ServiceType serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ServiceType descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceType)) {
            return false;
        }
        return id != null && id.equals(((ServiceType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceType{" +
            "id=" + getId() +
            ", serviceId='" + getServiceId() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
