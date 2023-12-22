package org.example.dao;

import org.example.entity.Country;

import java.util.List;

public interface CountryDAO {
    void addCountry(Country country);

    List<Country> getAllCountries();

    Country findCountryById(int id);

    void deleteCountry(int id);
}
