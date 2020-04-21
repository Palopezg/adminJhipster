package ar.com.pablo.repository;

import ar.com.pablo.domain.ServiceType;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ServiceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTypeRepository extends MongoRepository<ServiceType, String> {
}
