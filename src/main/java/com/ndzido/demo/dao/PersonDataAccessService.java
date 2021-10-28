package com.ndzido.demo.dao;

import com.ndzido.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final String sql = "INSERT INTO person(id, name) VALUES (?,?)";
        Object[] args = {id, person.getName()};
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public List<Person> getAllPersons() {
        final String sql = "SELECT id, name FROM person;";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new Person(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("name"));
        });
    }

    @Override
    public Optional<Person> selectPersonByID(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";
        Object[] args = new Object[]{id};
        Person person = jdbcTemplate.queryForObject(
                sql,
                args,
                (resultSet, i) -> {
                    return new Person(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("name"));
        });

        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {
        final String sql = "DELETE FROM person WHERE id = ?";
        Object[] args = new Object[]{id};
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public int updatePersonById(UUID id, Person newPerson) {
        final String sql = "UPDATE person SET name = ? WHERE id = ?";
        Object[] args = new Object[]{newPerson.getName(), id};
        return jdbcTemplate.update(sql, args);
    }
}
