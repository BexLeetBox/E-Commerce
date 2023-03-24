<template>
  <div id="loginContainer">
    <div id="loginTitle">
      <label>Login form</label>
    </div>
    <div id="username">
      <label>Username:</label>
      <input data-test="userInput" type="text" name="username" v-model="user.username" />
    </div>
    <div id="password">
      <label id="passwordLabel">Password: </label>
      <input data-test="passwordInput" type="password" v-model="user.password" />
    </div>
    <button data-test="button" v-on:click="handleClickSignin">Sign in</button>
    <div id="loginStatus">
      <h3>{{ this.loginStatus }}</h3>
      <router-link to="/register"><p data-test="loginStatus"></p></router-link>
    </div>
    <h3>Dont have an account? Sign up by clicking register below</h3>
    <router-link to="register">Register</router-link>
  </div>
</template>

<script>
import store from '@/stores/index.js'
import router from '@/router/index.js'
export default {
  name: 'LoginComponent',
  methods: {
    async handleClickSignin() {
      let gotResponse = false
      let response
      try {
        response = await store.dispatch('fetchUserName', this.user)
        gotResponse = true
      } catch (exception) {
        gotResponse = false
      }
      console.log(gotResponse)
      console.log(response)
      const token = response.token
      localStorage.setItem('token', token);
      if (gotResponse === false) {
        this.loginStatus = 'Wrong credentials'
        store.commit('SET_LOGINSTATUS', false)
        console.log('Wrong credentials')
      } else {
        this.loginStatus = 'Logged in'
        store.commit('SET_LOGINSTATUS', true)
        localStorage.setItem('isLoggedIn', true);
        await router.push('/')
        window.location.reload()
        console.log(store.getters.getLoginStatus)
        console.log('Logged in')
      }
    }
  },
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      loginStatus: ''
    }
  }
}
</script>

<style scoped>
#loginContainer {
  width: 80%;
  margin: auto;
  margin-top: 100px;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0 0 10px #ccc;
  text-align: center;
  font-family: Arial, sans-serif;
}

#loginTitle {
  margin-bottom: 20px;
}

#loginTitle label {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

#username,
#password {
  margin-bottom: 20px;
}

#username label,
#password label {
  display: inline-block;
  width: auto;
  text-align: left;
  font-size: 16px;
  color: #333;
  margin-right: 10px;
}

#username input,
#password input {
  display: inline-block;
  padding: 5px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
}

#passwordLabel {
  display: inline-block;
  width: 100px;
  text-align: left;
  font-size: 16px;
  color: #333;
  margin-right: 10px;
}

#loginStatus {
  margin-top: 20px;
}

h3 {
  margin-top: 20px;
  margin-bottom: 10px;
  font-size: 12px;
  font-weight: bold;
  color: #33333391;
}
#loginStatus h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

#loginStatus p {
  font-size: 14px;
  color: #333;
}

button {
  background-color: #4caf50;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #3e8e41;
}
</style>
