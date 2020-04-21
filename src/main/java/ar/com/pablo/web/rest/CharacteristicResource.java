package ar.com.pablo.web.rest;

import ar.com.pablo.domain.Characteristic;
import ar.com.pablo.service.CharacteristicService;
import ar.com.pablo.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ar.com.pablo.domain.Characteristic}.
 */
@RestController
@RequestMapping("/api")
public class CharacteristicResource {

    private final Logger log = LoggerFactory.getLogger(CharacteristicResource.class);

    private static final String ENTITY_NAME = "characteristic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacteristicService characteristicService;

    public CharacteristicResource(CharacteristicService characteristicService) {
        this.characteristicService = characteristicService;
    }

    /**
     * {@code POST  /characteristics} : Create a new characteristic.
     *
     * @param characteristic the characteristic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characteristic, or with status {@code 400 (Bad Request)} if the characteristic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/characteristics")
    public ResponseEntity<Characteristic> createCharacteristic(@Valid @RequestBody Characteristic characteristic) throws URISyntaxException {
        log.debug("REST request to save Characteristic : {}", characteristic);
        if (characteristic.getId() != null) {
            throw new BadRequestAlertException("A new characteristic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Characteristic result = characteristicService.save(characteristic);
        return ResponseEntity.created(new URI("/api/characteristics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /characteristics} : Updates an existing characteristic.
     *
     * @param characteristic the characteristic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characteristic,
     * or with status {@code 400 (Bad Request)} if the characteristic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characteristic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/characteristics")
    public ResponseEntity<Characteristic> updateCharacteristic(@Valid @RequestBody Characteristic characteristic) throws URISyntaxException {
        log.debug("REST request to update Characteristic : {}", characteristic);
        if (characteristic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Characteristic result = characteristicService.save(characteristic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, characteristic.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /characteristics} : get all the characteristics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characteristics in body.
     */
    @GetMapping("/characteristics")
    public ResponseEntity<List<Characteristic>> getAllCharacteristics(Pageable pageable) {
        log.debug("REST request to get a page of Characteristics");
        Page<Characteristic> page = characteristicService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /characteristics/:id} : get the "id" characteristic.
     *
     * @param id the id of the characteristic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characteristic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/characteristics/{id}")
    public ResponseEntity<Characteristic> getCharacteristic(@PathVariable String id) {
        log.debug("REST request to get Characteristic : {}", id);
        Optional<Characteristic> characteristic = characteristicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characteristic);
    }

    /**
     * {@code DELETE  /characteristics/:id} : delete the "id" characteristic.
     *
     * @param id the id of the characteristic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/characteristics/{id}")
    public ResponseEntity<Void> deleteCharacteristic(@PathVariable String id) {
        log.debug("REST request to delete Characteristic : {}", id);
        characteristicService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
