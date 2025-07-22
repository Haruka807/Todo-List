<template>
  <div class="box">
    <input v-model="account" placeholder="Enter your account" />
    <input v-model="password" placeholder="Enter your password" />
    <button @click="login">LOGIN</button>
  </div>
  <div class="register" @click="register">Register</div>
</template>

<script setup>
//目前还只是前端模拟登录，接下来还需要：
//1. 注册
//2. 登录校验
//3. 后端数据持久化
//4. 前端限时免登录
import { ref } from 'vue'
import axios from 'axios'

//收集输入
const account = ref('')
const password = ref('')

//注册事件发射器，声明组件支持哪些事件，防止写错
const emit = defineEmits(['login-success'])

//登录逻辑，向后端发送请求
async function login() {
    if (!account.value) {
        alert("Invalid account.")
        return
    }
    if (!password.value) {
        alert("Invalid password.")
        return
    }
    if (account.value.includes(' ')) {
        alert("Spaces are not allowed in account.")
        return
    }
    if (password.value.includes(' ')) {
        alert("Spaces are not allowed in password.")
        return
    }
    try {
        const res = await axios.post('/api/login', {
            account: account.value,
            password: password.value
        })

        if(res.data.success) {
            localStorage.setItem('account', account.value)
            emit('login-success', res.data.userName) 
        } else {
            const errMsg = res.data.message
            alert(errMsg)
        }
    } catch (err) {
        console.error('登录请求失败', err)
        alert('后端服务未响应')
    }
}
//注册逻辑
async function register(){
    if (!account.value) {
        alert("Invalid account.")
        return
    }
    if (!password.value) {
        alert("Invalid password.")
        return
    }
    if (account.value.includes(' ')) {
        alert("Spaces are not allowed in account.")
        return
    }
    if (password.value.includes(' ')) {
        alert("Spaces are not allowed in password.")
        return
    }

    try{
        const res = await axios.post('/api/register', {
            account: account.value,
            password: password.value
        })

        if(res.data.success) {
            localStorage.setItem('account', account.value)
            emit('login-success', res.data.userName)
        } else {
            const errMsg = res.data.message
            alert(errMsg)
        }
    } catch (err) {
        console.error('注册请求失败',err)
        alert('后端服务未响应')
    }
}
</script>

<style scoped>
.box {
    max-width: 400px;
    min-height: 200px;
    margin: 100px auto;
    padding: 24px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.5);
    border-radius: 12px;
    background-color: rgba(247,247,247,0.7);
    display: flex;
    flex-direction: column;
    gap: 16px;
}
input {
    background-color: rgba(247, 247, 247, 0.5);
    padding: 17px 16px;
    font-size: 16px;
    border: 1.5px solid #7388a4;
    border-radius: 8px;
    transition: border-color 0.3s ease;
    font-family: Cascadia Mono, Helvetica, sans-serif;
    color: #035d71;
}
input:focus {
    outline: none;
    border-color: rgba(15, 15, 189, 0.702);
}
button {
    margin-top: 8px;
    background-color: rgb(178, 186, 203, 0.5);
    border: 1.5px solid #7388a4;
    border-radius: 12px;
    padding-top: 10px;
    padding-bottom: 10px; 
    font-size: 16px;
    font-family: Cascadia Mono, Helvetica, sans-serif;
    color: #054553;
}
button:hover {
    background-color: rgba(106, 171, 173, 0.767);
    cursor: pointer;
}
.register {
    position: fixed;
    right: 5vw;
    bottom: 5vh;
    background-color: rgba(247,247,247,0.5);
    padding: 0.5rem 1rem;
    border-radius: 1rem;
    font-size: 1rem;
    font-family: Cascadia Mono, Helvetica, sans-serif;
    color: #035d71;
    cursor: pointer;
    box-shadow: 0 2px 6px rgba(0,0,0,0.2);
    z-index: 1000;
}
@media (max-width: 400px) {
/*css层叠规则，后写的会覆盖前面的，并且@media是条件生效*/
    div {
        max-width: 90%;
        padding: 16px;
    }
    input, button {
        font-size: 14px;
        padding: 10px 12px;
    }
}
</style>
