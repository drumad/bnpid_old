package org.bnp.id.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.model.info.Parish;
import org.bnp.id.service.ParishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Log4j
public class ParishController {

    @Getter
    private static Map<Integer, Parish> parishes = null;

    private ParishService parishService;

    @Autowired
    public ParishController(ParishService parishService) {

        this.parishService = parishService;

        try {
            loadParishes();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void loadParishes() throws SQLException {

        log.debug("Loading parishes..");

        if (parishes != null) {
            parishes.clear();
        }
        parishes = parishService.findAll().stream().collect(Collectors.toMap(Parish::getId, Function.identity()));
    }

    @RequestMapping(value = "/parish", produces = MediaType.TEXT_PLAIN_VALUE)
    public String view() {

        return "Hello Parish World!";
    }

    @GetMapping("/parish/{id}")
    public Parish getParish(@PathVariable Integer id) throws SQLException {

        return parishService.findById(id);
    }

    @GetMapping("/parish/{name}")
    public Collection<Parish> getParish(@PathVariable String name) throws SQLException {

        return parishService.findByName(name);
    }

    @GetMapping("/parish/list")
    public ResponseEntity<Collection<Parish>> getParishes() {

        try {
            loadParishes();
            if (parishes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException | SQLException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(parishes.values(), HttpStatus.OK);
    }

    @PostMapping("/parish")
    public ResponseEntity<Parish> addParish(@RequestBody Parish parish) {

        int id = Integer.MIN_VALUE;
        try {
            id = parishService.save(parish);
            loadParishes();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }


        return new ResponseEntity<>(parishes.get(id), HttpStatus.OK);
    }

    @PutMapping("/parish/{id}")
    public ResponseEntity<Parish> updateParish(@PathVariable Integer id, @RequestBody Parish parish) {

        try {
            parishService.update(parish);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(parish, HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteParish(@PathVariable Integer id) {

        try {
            parishService.deleteById(id);
            loadParishes();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
