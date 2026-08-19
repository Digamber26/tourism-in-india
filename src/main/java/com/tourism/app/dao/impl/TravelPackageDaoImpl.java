package com.tourism.app.dao.impl;

import com.tourism.app.dao.TravelPackageDao;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.TravelPackage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TravelPackageDaoImpl implements TravelPackageDao {

    private final JdbcTemplate jdbcTemplate;

    public TravelPackageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static TravelPackage mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new TravelPackage(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("destination_id"),
                rs.getBigDecimal("price"),
                rs.getInt("duration_days"),
                rs.getString("inclusions")
        );
    }

    @Override
    public List<TravelPackage> findAll() {
        String sql = "SELECT id, name, destination_id, price, duration_days, inclusions FROM travel_packages ORDER BY id";
        return jdbcTemplate.query(sql, TravelPackageDaoImpl::mapRow);
    }

    @Override
    public List<TravelPackage> findByDestinationId(Long destinationId) {
        String sql = "SELECT id, name, destination_id, price, duration_days, inclusions " +
                "FROM travel_packages WHERE destination_id = ? ORDER BY id";
        return jdbcTemplate.query(sql, TravelPackageDaoImpl::mapRow, destinationId);
    }

    @Override
    public Optional<TravelPackage> findById(Long id) {
        String sql = "SELECT id, name, destination_id, price, duration_days, inclusions " +
                "FROM travel_packages WHERE id = ?";
        return jdbcTemplate.query(sql, TravelPackageDaoImpl::mapRow, id).stream().findFirst();
    }

    @Override
    public TravelPackage save(TravelPackage travelPackage) {
        String sql = "INSERT INTO travel_packages (name, destination_id, price, duration_days, inclusions) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, travelPackage.getName());
            ps.setLong(2, travelPackage.getDestinationId());
            ps.setBigDecimal(3, travelPackage.getPrice());
            ps.setInt(4, travelPackage.getDurationDays());
            ps.setString(5, travelPackage.getInclusions());
            return ps;
        }, keyHolder);

        travelPackage.setId(keyHolder.getKey().longValue());
        return travelPackage;
    }

    @Override
    public TravelPackage update(Long id, TravelPackage travelPackage) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Travel package not found with id: " + id);
        }
        String sql = "UPDATE travel_packages SET name = ?, destination_id = ?, price = ?, " +
                "duration_days = ?, inclusions = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                travelPackage.getName(),
                travelPackage.getDestinationId(),
                travelPackage.getPrice(),
                travelPackage.getDurationDays(),
                travelPackage.getInclusions(),
                id);
        travelPackage.setId(id);
        return travelPackage;
    }

    @Override
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Travel package not found with id: " + id);
        }
        jdbcTemplate.update("DELETE FROM travel_packages WHERE id = ?", id);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM travel_packages WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }
}
