package com.example.tdbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/get-tasks")
    public Map<String, Object> getTasks(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String account = payload.get("account");
        String sql = "SELECT id, text, completed FROM todos WHERE account=?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, account);//对应js数组
        result.put("tasks", list);
        return result;
    }

    @PostMapping("/add-task")
    public Map<String, Object> addTask(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String account = payload.get("account");
        String text = payload.get("text");
        Boolean completed = Boolean.parseBoolean(payload.get("completed"));
        String sql = "INSERT INTO todos (account, text, completed) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, account, text, completed);
        String querySql = "SELECT id, text, completed FROM todos WHERE account=? AND text=? ORDER BY id DESC LIMIT 1";
        Map<String, Object> task = jdbcTemplate.queryForMap(querySql, account, text);
        result.put("task", task);
        result.put("success", true);
        return result;
    }

    @PostMapping("/del-task")
    public Map<String, Object> delTask(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        Integer id = Integer.parseInt(payload.get("id"));
        String sql = "DELETE FROM todos WHERE id=?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        result.put("success", rowsAffected == 1);
        return result;
    }

    @PostMapping("/com-task")
    public Map<String, Object> comTask(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        Integer id = Integer.parseInt(payload.get("id"));
        String sql = "UPDATE todos SET completed = NOT completed WHERE id=?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        result.put("success", rowsAffected == 1);
        return result;
    }
}
