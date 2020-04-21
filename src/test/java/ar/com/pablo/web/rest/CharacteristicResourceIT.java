package ar.com.pablo.web.rest;

import ar.com.pablo.AdminJhipsterApp;
import ar.com.pablo.domain.Characteristic;
import ar.com.pablo.repository.CharacteristicRepository;
import ar.com.pablo.service.CharacteristicService;

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
 * Integration tests for the {@link CharacteristicResource} REST controller.
 */
@SpringBootTest(classes = AdminJhipsterApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CharacteristicResourceIT {

    private static final String DEFAULT_CHARACTERISTIC_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHARACTERISTIC_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private CharacteristicService characteristicService;

    @Autowired
    private MockMvc restCharacteristicMockMvc;

    private Characteristic characteristic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Characteristic createEntity() {
        Characteristic characteristic = new Characteristic()
            .characteristicId(DEFAULT_CHARACTERISTIC_ID)
            .serviceId(DEFAULT_SERVICE_ID)
            .descripcion(DEFAULT_DESCRIPCION);
        return characteristic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Characteristic createUpdatedEntity() {
        Characteristic characteristic = new Characteristic()
            .characteristicId(UPDATED_CHARACTERISTIC_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .descripcion(UPDATED_DESCRIPCION);
        return characteristic;
    }

    @BeforeEach
    public void initTest() {
        characteristicRepository.deleteAll();
        characteristic = createEntity();
    }

    @Test
    public void createCharacteristic() throws Exception {
        int databaseSizeBeforeCreate = characteristicRepository.findAll().size();

        // Create the Characteristic
        restCharacteristicMockMvc.perform(post("/api/characteristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(characteristic)))
            .andExpect(status().isCreated());

        // Validate the Characteristic in the database
        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeCreate + 1);
        Characteristic testCharacteristic = characteristicList.get(characteristicList.size() - 1);
        assertThat(testCharacteristic.getCharacteristicId()).isEqualTo(DEFAULT_CHARACTERISTIC_ID);
        assertThat(testCharacteristic.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testCharacteristic.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    public void createCharacteristicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = characteristicRepository.findAll().size();

        // Create the Characteristic with an existing ID
        characteristic.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacteristicMockMvc.perform(post("/api/characteristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(characteristic)))
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkCharacteristicIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = characteristicRepository.findAll().size();
        // set the field null
        characteristic.setCharacteristicId(null);

        // Create the Characteristic, which fails.

        restCharacteristicMockMvc.perform(post("/api/characteristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(characteristic)))
            .andExpect(status().isBadRequest());

        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCharacteristics() throws Exception {
        // Initialize the database
        characteristicRepository.save(characteristic);

        // Get all the characteristicList
        restCharacteristicMockMvc.perform(get("/api/characteristics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characteristic.getId())))
            .andExpect(jsonPath("$.[*].characteristicId").value(hasItem(DEFAULT_CHARACTERISTIC_ID)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    public void getCharacteristic() throws Exception {
        // Initialize the database
        characteristicRepository.save(characteristic);

        // Get the characteristic
        restCharacteristicMockMvc.perform(get("/api/characteristics/{id}", characteristic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characteristic.getId()))
            .andExpect(jsonPath("$.characteristicId").value(DEFAULT_CHARACTERISTIC_ID))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    public void getNonExistingCharacteristic() throws Exception {
        // Get the characteristic
        restCharacteristicMockMvc.perform(get("/api/characteristics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCharacteristic() throws Exception {
        // Initialize the database
        characteristicService.save(characteristic);

        int databaseSizeBeforeUpdate = characteristicRepository.findAll().size();

        // Update the characteristic
        Characteristic updatedCharacteristic = characteristicRepository.findById(characteristic.getId()).get();
        updatedCharacteristic
            .characteristicId(UPDATED_CHARACTERISTIC_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .descripcion(UPDATED_DESCRIPCION);

        restCharacteristicMockMvc.perform(put("/api/characteristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCharacteristic)))
            .andExpect(status().isOk());

        // Validate the Characteristic in the database
        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeUpdate);
        Characteristic testCharacteristic = characteristicList.get(characteristicList.size() - 1);
        assertThat(testCharacteristic.getCharacteristicId()).isEqualTo(UPDATED_CHARACTERISTIC_ID);
        assertThat(testCharacteristic.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testCharacteristic.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    public void updateNonExistingCharacteristic() throws Exception {
        int databaseSizeBeforeUpdate = characteristicRepository.findAll().size();

        // Create the Characteristic

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc.perform(put("/api/characteristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(characteristic)))
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCharacteristic() throws Exception {
        // Initialize the database
        characteristicService.save(characteristic);

        int databaseSizeBeforeDelete = characteristicRepository.findAll().size();

        // Delete the characteristic
        restCharacteristicMockMvc.perform(delete("/api/characteristics/{id}", characteristic.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Characteristic> characteristicList = characteristicRepository.findAll();
        assertThat(characteristicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
