<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import axios from 'axios'
const props = defineProps({
  userName: String
})

const userName = ref('')
watch(() => props.userName, val => {
  userName.value = val
}, { immediate: true })


const newTask = ref('')  // 输入框内容
const tasks = ref([])
onMounted(async () => {
    try{
        fetchTasks()
    } catch (e) {
        console.log("无法从服务器获取任务列表", e)
    }
})

async function addTask(){
  const trimmed = newTask.value.trim()
  if (trimmed) {
    const newItem = {
        account: localStorage.getItem('account'),
        text: trimmed,
        completed: false
    }
    const res = await axios.post('/api/add-task', newItem)
    if (!res.data.success) {
        alert("Adding failed.")
    } else {
        newTask.value = ''
        alert("Added successfully")
        fetchTasks()
    }
  } else {
    alert("Please enter valid task.")
  }
}

async function deleteTask(taskId) {
    try{
        await axios.post('/api/del-task',{
            id: taskId
        })
        fetchTasks()
    } catch (e) {
        console.error("请求失败", e)
        alert("network error")
    }
}

//重新拉取任务列表
async function fetchTasks() {
    try {
        const res = await axios.post('/api/get-tasks', {
            account: localStorage.getItem('account')
        })
        tasks.value = res.data.tasks
    } catch (e) {
        console.error("请求失败", e)
        alert("network error")
    }
}

async function completeTask(taskId) {
    try {
        await axios.post('/api/com-task', {
        id: taskId
        })
        fetchTasks()
    } catch (error) {
        console.error("请求失败", error)
        alert("network error")
    }
}


//重命名函数（挂在rename按钮上）
async function renameUser() {
    try {
        const name = prompt("Rename your todo-list", userName.value)
        if(name) {
        userName.value = name.trim()
        localStorage.setItem('userName', userName.value)
        await axios.post('/api/set-name', {
            account: localStorage.getItem("account"),
            userName: userName.value
        })
    } else {
        alert("Please enter valid name.")
    }
    } catch (error) {
        console.error("请求失败", error)
        alert("network error")
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
        <div v-for="task in tasks" :key="task.id" class="item">
          <div>
            <input type="checkbox" @click="completeTask(task.id)">
            <span class="task" :class="{ complete: task.completed }">{{ task.text }}</span>
          </div>
          <div class="del" @click="deleteTask(task.id)">del</div>
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