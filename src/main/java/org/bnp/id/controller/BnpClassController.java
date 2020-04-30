package org.bnp.id.controller;

import org.bnp.id.model.retreat.ClassInfo;

import java.util.HashMap;

public class BnpClassController {

    private ClassInfo model;

    private HashMap<Integer, String> classMap;

    public BnpClassController() {
        loadData();
    }

    public void loadData() {

    }

    public static void main(String[] args) {
        new BnpClassController();
    }
}
