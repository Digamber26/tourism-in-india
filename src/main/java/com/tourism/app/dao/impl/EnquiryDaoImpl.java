package com.tourism.app.dao.impl;

import com.tourism.app.dao.EnquiryDao;
import com.tourism.app.exception.ResourceNotFoundException;
import com.tourism.app.model.Enquiry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EnquiryDaoImpl implements EnquiryDao {

    private final JdbcTemplate jdbcTemplate;

    public EnquiryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Enquiry mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        return new Enquiry(
                rs.getLong("id"),
                rs.getLong("package_id"),
                rs.getString("customer_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("message"),
                rs.getString("status"),
                ts != null ? ts.toLocalDateTime() : null
        );
    }

    @Override
    public List<Enquiry> findAll() {
        String sql = "SELECT id, package_id, customer_name, email, phone, message, status, created_at " +
                "FROM enquiries ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, EnquiryDaoImpl::mapRow);
    }

    @Override
    public Optional<Enquiry> findById(Long id) {
        String sql = "SELECT id, package_id, customer_name, email, phone, message, status, created_at " +
                "FROM enquiries WHERE id = ?";
        return jdbcTemplate.query(sql, EnquiryDaoImpl::mapRow, id).stream().findFirst();
    }

    @Override
    public Enquiry save(Enquiry enquiry) {
        String sql = "INSERT INTO enquiries (package_id, customer_name, email, phone, message, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, enquiry.getPackageId());
            ps.setString(2, enquiry.getCustomerName());
            ps.setString(3, enquiry.getEmail());
            ps.setString(4, enquiry.getPhone());
            ps.setString(5, enquiry.getMessage());
            ps.setString(6, "NEW");
            ps.setTimestamp(7, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        enquiry.setId(keyHolder.getKey().longValue());
        enquiry.setStatus("NEW");
        enquiry.setCreatedAt(now);
        return enquiry;
    }

    @Override
    public Enquiry updateStatus(Long id, String status) {
        Enquiry existing = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found with id: " + id));
        jdbcTemplate.update("UPDATE enquiries SET status = ? WHERE id = ?", status, id);
        existing.setStatus(status);
        return existing;
    }

    @Override
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Enquiry not found with id: " + id);
        }
        jdbcTemplate.update("DELETE FROM enquiries WHERE id = ?", id);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM enquiries WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }
}
