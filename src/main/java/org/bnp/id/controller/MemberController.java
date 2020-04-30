package org.bnp.id.controller;

import java.util.HashMap;

public class MemberController {

    public MemberController() {

    }

    public HashMap<Integer, String> getCountriesMap() {
        return DBController.getInstance().getCountriesMap();
    }
}
