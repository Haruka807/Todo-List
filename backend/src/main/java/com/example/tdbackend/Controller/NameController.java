package com.example.tdbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NameController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/set-name")
    public Map<String, Object> setName(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String account = payload.get("account");
        String name = payload.get("userName");
        String sql = "UPDATE users SET userName = ? WHERE account = ?";
        int rowsAffected = jdbcTemplate.update(sql, name, account);
        result.put("success", rowsAffected > 0);

        return result;
    }

    @PostMapping("/get-name")
    public Map<String, Object> getName(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String account = payload.get("account");
        String sql = "SELECT userName FROM users WHERE account = ?";
        String name = jdbcTemplate.queryForObject(sql, String.class, account);
        result.put("success", true);
        result.put("userName", name);
        return result;
    }
}
