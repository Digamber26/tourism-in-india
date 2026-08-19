package com.tourism.app.dao.impl;

import com.tourism.app.dao.DestinationDao;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.Destination;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class DestinationDaoImpl implements DestinationDao {

    private final JdbcTemplate jdbcTemplate;

    public DestinationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Destination mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Destination(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("state"),
                rs.getString("description"),
                rs.getString("category")
        );
    }

    @Override
    public List<Destination> findAll() {
        String sql = "SELECT id, name, state, description, category FROM destinations ORDER BY id";
        return jdbcTemplate.query(sql, DestinationDaoImpl::mapRow);
    }

    @Override
    public Optional<Destination> findById(Long id) {
        String sql = "SELECT id, name, state, description, category FROM destinations WHERE id = ?";
        List<Destination> results = jdbcTemplate.query(sql, DestinationDaoImpl::mapRow, id);
        return results.stream().findFirst();
    }

    @Override
    public Destination save(Destination destination) {
        String sql = "INSERT INTO destinations (name, state, description, category) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, destination.getName());
            ps.setString(2, destination.getState());
            ps.setString(3, destination.getDescription());
            ps.setString(4, destination.getCategory());
            return ps;
        }, keyHolder);

        Long newId = keyHolder.getKey().longValue();
        destination.setId(newId);
        return destination;
    }

    @Override
    public Destination update(Long id, Destination destination) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Destination not found with id: " + id);
        }
        String sql = "UPDATE destinations SET name = ?, state = ?, description = ?, category = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                destination.getName(),
                destination.getState(),
                destination.getDescription(),
                destination.getCategory(),
                id);
        destination.setId(id);
        return destination;
    }

    @Override
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Destination not found with id: " + id);
        }
        jdbcTemplate.update("DELETE FROM destinations WHERE id = ?", id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM destinations WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
