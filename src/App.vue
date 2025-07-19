<script setup>
import { ref, watch, onMounted } from 'vue'

const newTask = ref('')  // 输入框内容
// 先尝试从浏览器本地读取，若首次访问则加载空列表
const tasks = ref(JSON.parse(localStorage.getItem('tasks') || '[]'))   //待办列表

function addTask(){
  const trimmed = newTask.value.trim()
  if (trimmed) {
    tasks.value.push({ text: trimmed, completed: false})
    newTask.value = ''
  }
}

function deleteTask(index) {
  tasks.value.splice(index, 1)
}

// 浏览器存储变化
watch(tasks, (newVal)=>{
  localStorage.setItem('tasks', JSON.stringify(newVal))
}, { deep: true })

// 首次创建名字弹窗
const userName = ref(localStorage.getItem('userName') || '') //用ref创建的响应式变量，访问要用属性value
onMounted(()=>{
    if(!userName.value) {
        const name = prompt("Please enter your name.")//浏览器原生弹窗，不能自定义样式，name不是响应式变量可以直接访问
        if (name) {
            userName.value = name.trim()
            localStorage.setItem('userName', userName.value)
        }
    }
})
//重命名函数（挂在rename按钮上）
function renameUser() {
    const name = prompt("Rename your todo-list", userName.value)
    if(name) {
        userName.value = name.trim()
        localStorage.setItem('userName', userName.value)
    }
}
</script>
<template>
  <div class="todo-app">
        <div class="title">
            {{ userName }}'s Todo List
        </div>
        <div class="todo-form">
            <input v-model="newTask" class="todo-input" type="text" placeholder="Add a todo">
            <div @click="addTask" class="todo-button">Add</div>
        </div>
        <div v-for="(task, index) in tasks" :key="index" class="item">
          <div>
            <input type="checkbox" v-model="task.completed">
            <span class="task" :class="{ complete: task.completed }">{{ task.text }}</span>
          </div>
          <div class="del" @click="deleteTask(index)">del</div>
        </div>
        <div class="rename" @click="renameUser">Rename</div>
    </div>
</template>

<style scoped>
        body {
            min-height: 100vh;
            background: linear-gradient(to bottom, #a4d4de, #5f97c2);
        }
        .todo-app {
            width: 95%;
            height: 500px;
            padding-top: 30px;
            box-sizing: border-box;
            background-color: rgba(247,247,247,0.7);
            margin: 30px auto;
            border-radius: 15px;
        }
        .title {
            font-size: 30px;
            font-weight: 666;
            font-family: Cascadia Mono, Helvetica, sans-serif;
            text-align: center;
            color: #035d71;
        }
        .todo-form {
            display: flex;
            margin-top: 25px;
            justify-content: center;
        }
        .todo-input {
            padding-left: 20px;
            box-sizing: border-box;
            border: 1px solid #c2d1dc;
            width: 60%;
            height: 50px;
            border-radius: 25px 0 0 25px;
        }
        .todo-input:focus,
        .todo-input:hover {
            outline: 1px solid #5f97c2;
        }
        .todo-input::placeholder {
            font-size: 15px;
            font-weight: 400;
            font-family: Cascadia Mono, Helvetica, sans-serif;
        }
        .todo-button {
            border: 0.5px solid #c2d1dc;
            width: 85px;
            height: 48px;
            border-radius: 0 25px 25px 0;
            background: linear-gradient(to right, #88cddc, #5f97c2);
            line-height: 45px;
            text-align: center;
            cursor: pointer;
            user-select: none;
            color: azure;
            font-size: 15px;
            font-weight: 400;
            font-family: Cascadia Mono, Helvetica, sans-serif;
        }
        .todo-button:hover {
            outline: 0.5px solid #5f97c2
        }
        .item {
            display: flex;
            box-sizing: border-box;
            width: 70%;
            height: 45px;
            margin: 20px auto;
            padding: 12px 16px;
            border-radius: 20px;
            background-color: rgb(245, 250, 255);
            box-shadow: 2px 4px 8px rgba(0,0,0,0.2);
            align-items: center;
            justify-content: space-between;
        }
        .del {
            color: rgb(38, 173, 151);
            cursor: pointer;
        }
        .complete {
            text-decoration: line-through;
            opacity: 0.4;
        }
        .rename {
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
</style>