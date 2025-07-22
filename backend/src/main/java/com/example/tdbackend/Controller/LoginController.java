package com.example.tdbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//rest风格控制器，所有方法默认以JSON格式返回数据
@RestController
//类的统一请求前缀，类中所有接口都以/api开头
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    /*
    1.接收前端传来的账号和密码
    2.查询 users 表中是否存在该账号
    3.如果存在，进一步验证密码
    4.如果不存在，返回“账号不存在”
    5. 如果存在但密码错误，返回“密码错误”
    6.成功则返回用户名。（前端判断：如果用户名为空，则弹窗让用户输入用户名）
    */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        //将前端请求发过来的json（账号+密码）自动转换为java的map
        String account = payload.get("account");
        String password = payload.get("password");
        Map<String, Object> result = new HashMap<>();
        // 1.判断账号是否存在
        String sql = "SELECT COUNT(*) FROM users WHERE account=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, account);

        if (count != null && count == 1 ) {
            //2. 查密码
            String getPassword = "SELECT password FROM users WHERE account=?";
            String realPassword = jdbcTemplate.queryForObject(getPassword, String.class, account);
            if (realPassword != null && realPassword.equals(password)) {
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "Wrong password.");
            }
        } else {
            result.put("success", false);
            result.put("message", "Account doesn't exist, please register first."); //输入不存在的账号，要先注册
        }
        return result;
    }
}
/*
*举例：
*请求：
* POST /api/login
{
  "account": "test",
  "password": "123456"
}
*响应：
* {
  "success": true
}
*用map作为返回值（而不是直接用布尔值）的好处：
* 1. 拓展性强，方便同意响应结构
* {
  "success": false,
  "message": "账号或密码错误"
}
2. 前端方便解析
* */
