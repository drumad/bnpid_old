package org.bnp.id.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.model.info.Country;
import org.bnp.id.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
public class CountryController {

    @Getter
    private Map<String, Country> countries;

    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {

        this.countryService = countryService;

        loadCountries();
    }

    /**
     * Loads all the countries from the database.
     */
    public void loadCountries() {

        log.debug("Loading countries..");

        Collection<Country> countryList = countryService.findAll();

        countries = countryList.stream().collect(Collectors.toMap(Country::getNiceName, Function.identity()));
    }
}
