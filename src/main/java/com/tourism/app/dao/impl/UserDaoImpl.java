package com.tourism.app.dao.impl;

import com.tourism.app.dao.UserDao;
import com.tourism.app.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static User mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role")
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, full_name, email, password_hash, role FROM users WHERE email = ?";
        List<User> results = jdbcTemplate.query(sql, UserDaoImpl::mapRow, email);
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, full_name, email, password_hash, role FROM users WHERE id = ?";
        List<User> results = jdbcTemplate.query(sql, UserDaoImpl::mapRow, id);
        return results.stream().findFirst();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }
}
