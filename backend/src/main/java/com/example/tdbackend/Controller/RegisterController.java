package com.example.tdbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
/*
1.接收前端传来的账号和密码
2.查询 users 表中是否已存在该账号
3.如果存在，返回失败信息
4.如果不存在，插入新用户并返回成功
*/
@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> payload) {
        //将前端请求发过来的json（账号+密码）自动转换为java的map
        String account = payload.get("account");
        String password = payload.get("password");
        Map<String, Object> result = new HashMap<>();
        // 1.判断账号是否已经存在
        String sql = "SELECT COUNT(*) FROM users WHERE account=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, account);

        if (count != null && count > 0) {
            result.put("success", false);
            result.put("message", "Account already exists.");
            return result;
        }
        //1.1 判断account和password是否有一方为空
        if (account == null || account.isBlank() || password == null || password.isBlank() || account.contains(" ") || password.contains(" ")) {
            result.put("success", false);
            result.put("message", "Invalid account or password. Spaces are not allowed.");
            return result;
        }

        // 2.数据库中没有查出重复账号，可以插入新账号
        String insertSql = "INSERT INTO users(account, password, userName) VALUES(?, ?, ?)";
        jdbcTemplate.update(insertSql, account, password, "");

        result.put("success", true);

        return result;
    }
}
