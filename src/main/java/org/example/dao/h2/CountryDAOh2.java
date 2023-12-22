package org.example.dao.h2;

import org.example.dao.CountryDAO;
import org.example.entity.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAOh2 implements CountryDAO {
    private Connection connection;

    public CountryDAOh2(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCountry(Country country) {
        String sql = "INSERT INTO countries (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, country.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    country.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM countries";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Country country = new Country(resultSet.getInt("id"), resultSet.getString("name"));
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
    @Override
    public Country findCountryById(int id) {
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Country(resultSet.getInt("id"), resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteCountry(int id) {
        String sql = "DELETE FROM countries WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
