package ar.com.pablo.web.rest;

import ar.com.pablo.AdminJhipsterApp;
import ar.com.pablo.domain.ServiceType;
import ar.com.pablo.repository.ServiceTypeRepository;
import ar.com.pablo.service.ServiceTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ServiceTypeResource} REST controller.
 */
@SpringBootTest(classes = AdminJhipsterApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ServiceTypeResourceIT {

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private MockMvc restServiceTypeMockMvc;

    private ServiceType serviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createEntity() {
        ServiceType serviceType = new ServiceType()
            .serviceId(DEFAULT_SERVICE_ID)
            .descripcion(DEFAULT_DESCRIPCION);
        return serviceType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createUpdatedEntity() {
        ServiceType serviceType = new ServiceType()
            .serviceId(UPDATED_SERVICE_ID)
            .descripcion(UPDATED_DESCRIPCION);
        return serviceType;
    }

    @BeforeEach
    public void initTest() {
        serviceTypeRepository.deleteAll();
        serviceType = createEntity();
    }

    @Test
    public void createServiceType() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().size();

        // Create the ServiceType
        restServiceTypeMockMvc.perform(post("/api/service-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isCreated());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testServiceType.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    public void createServiceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().size();

        // Create the ServiceType with an existing ID
        serviceType.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceTypeMockMvc.perform(post("/api/service-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceTypeRepository.findAll().size();
        // set the field null
        serviceType.setServiceId(null);

        // Create the ServiceType, which fails.

        restServiceTypeMockMvc.perform(post("/api/service-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isBadRequest());

        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllServiceTypes() throws Exception {
        // Initialize the database
        serviceTypeRepository.save(serviceType);

        // Get all the serviceTypeList
        restServiceTypeMockMvc.perform(get("/api/service-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceType.getId())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    public void getServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.save(serviceType);

        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", serviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceType.getId()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    public void getNonExistingServiceType() throws Exception {
        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateServiceType() throws Exception {
        // Initialize the database
        serviceTypeService.save(serviceType);

        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().size();

        // Update the serviceType
        ServiceType updatedServiceType = serviceTypeRepository.findById(serviceType.getId()).get();
        updatedServiceType
            .serviceId(UPDATED_SERVICE_ID)
            .descripcion(UPDATED_DESCRIPCION);

        restServiceTypeMockMvc.perform(put("/api/service-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceType)))
            .andExpect(status().isOk());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);
        assertThat(testServiceType.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceType.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    public void updateNonExistingServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().size();

        // Create the ServiceType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTypeMockMvc.perform(put("/api/service-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteServiceType() throws Exception {
        // Initialize the database
        serviceTypeService.save(serviceType);

        int databaseSizeBeforeDelete = serviceTypeRepository.findAll().size();

        // Delete the serviceType
        restServiceTypeMockMvc.perform(delete("/api/service-types/{id}", serviceType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
