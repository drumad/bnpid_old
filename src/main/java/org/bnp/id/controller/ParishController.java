package org.bnp.id.controller;

import lombok.Getter;
import lombok.Setter;
import org.bnp.id.model.info.Parish;

import java.util.Map;

@Getter
@Setter
public class ParishController {

    private Parish model;

    private Map<Integer, String> countryMap;

    public ParishController() {
    }

    public static void main(String[] args) {
        ParishController c = new ParishController();
    }

    private void loadData() {
        setCountryMap(DBController.getInstance().getCountriesMap());
    }
}
