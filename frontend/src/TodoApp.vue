<template>
  <div>
    <!--登录状态判断-->
    <!--父组件监听，子组件用emit触发-->
    <Login v-if="!isLoggedIn" @login-success="handleLoginSuccess" />
    <Main v-else :userName="userName" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import Login from './Login.vue'
import Main from './Main.vue'

const isLoggedIn = ref(false)
const userName = ref('')

function handleLoginSuccess(name) {
    isLoggedIn.value = true
    checkUserName()
}
async function checkUserName() {
    const account = localStorage.getItem('account')
    try {
        const res = await axios.post('/api/get-name', 
            { account }
        )
        userName.value = res.data.userName
        while (!userName.value) {
            // 名字为空
            const inputName = prompt("Please enter your name.")
            if (inputName) {
                await axios.post('/api/set-name', {
                    account: account,
                    userName: inputName.trim()
                })
                userName.value = inputName
                break
            } else {
                alert("Please enter valid name.")
            }
        }
        localStorage.setItem("userName", userName.value) 
    } catch (e) {
        console.error('设置用户名失败', e)
        alert('后端服务未响应')
    }
}
</script>

<style scoped>
</style>
