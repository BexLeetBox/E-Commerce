<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
    />
    <div id="logodiv">
      <img id="logo" src="./assets/logo.svg" alt="" />
    </div>
  </header>
  <div class="mobile-container">
    <div class="topnav">
      <RouterLink to="/" exact :exact-active-class="'active'">Home</RouterLink>
      <div id="myLinks">
        <RouterLink to="/cart" exact :exact-active-class="'active'">My cart</RouterLink>
        <RouterLink :to="sellLink" :class="{ active: $route.path === '/sell' }">Sell</RouterLink>
        <RouterLink :to="listingLink" :class="{ active: $route.path === '/mylisting' }">My listings</RouterLink>
        <RouterLink to="/about" exact :exact-active-class="'active'">About us</RouterLink>
        <RouterLink
          :to="loginLink"
          id="loginButton"
          exact
          :exact-active-class="'active'"
          >{{ loginText }}</RouterLink
        >
      </div>

      <a href="javascript:void(0);" class="icon" @click="menuDr()">
        <i class="fa fa-bars"></i>
      </a>
    </div>
  </div>
  <RouterView />
</template>
<script>
import store from '../src/stores/index'
import { computed } from 'vue'


export default {
  
  data() {
    return {
      count: 0,
      loginLink: '/login',
      loginText: 'Login',
      sellLink: '/login',
      listingLink: '/login'
    }
  },
  mounted() {

    const sellLink = computed(() => {
      return localStorage.getItem('isLoggedIn') ? '/sell' : '/login'
    })
    console.log(localStorage.getItem('isLoggedIn'))
    // Computed property that returns the correct link based on the isLoggedIn state
    const loginLink = computed(() => {
      return localStorage.getItem('isLoggedIn') ? '/myaccount' : '/login'
    })

    // Computed property that returns the correct text based on the isLoggedIn state
    var loginText = computed(() => {
      return localStorage.getItem('isLoggedIn') ? 'My Account' : 'Login'
    })

    var listingLink = computed(() => {
      return localStorage.getItem('isLoggedIn') ? '/mylisting' : '/login'
    })

    console.log('Logged in status = ' + localStorage.getItem('isLoggedIn'))

    this.loginLink = loginLink
    this.loginText = loginText
    this.sellLink = sellLink
    this.listingLink = listingLink

  },
  methods: {
    incrementCount() {
      store.dispatch('increment')
    },
    menuDr() {
      var x = document.getElementById('myLinks')
      if (x.style.display === 'block') {
        x.style.display = 'none'
      } else {
        x.style.display = 'block'
      }
    }
  }
}
</script>
<style scoped>
* {
  font-family: 'Open Sans', sans-serif;
  text-align: center;
}

#logodiv {
  display: flex;
  justify-content: center;
  align-items: center;
}
#logo {
  max-width: 150px;
  text-align: center;
}
.mobile-container {
  max-width: 700px;
  margin: auto;
  background-color: #555;

  color: white;
  border-radius: 10px;
}

.topnav {
  overflow: hidden;
  background-color: #333;
  position: relative;
  border-radius: 0px;
  margin-bottom: 20px;
}

.topnav #myLinks {
  display: none;
}

.topnav a {
  color: white;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
  display: block;
}

.topnav a.icon {
  display: none;
  background: black;
  display: block;
  position: absolute;
  right: 0;
  top: 0;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.active {
  background-color: #04aa6d;
  color: white;
}
map-wrapper{
  text-align: center;
}
.vue-map-container{
  display: flex;
  justify-content: center;
  position: absolute;
  width: 8000%;
}
.vue-map{
  text-align: center;
}
@media screen and (min-width: 700px) {
  #loginButton {
    background-color: #4caf50;
    position: absolute;
    right: 0;
    padding: 14px 16px;
    box-shadow: 2px 2px 5px rgba(37, 2, 2, 0.5);
    color: white;
    border-radius: 5px;
  }
  #loginButton:hover {
    background-color: #04aa6d;
  }
  * {
    margin: 10px;
    text-align: center;
  }
  #logo {
    max-width: 200px;
    text-align: center;
  }
  .topnav {
    display: block;
    border-radius: 0px;
  }
  .topnav a {
    display: inline-block;
    padding: 14px 10px;
  }

  .mobile-container {
    max-width: 100%;
    margin: auto;
    background-color: #555;
    color: white;
    border-radius: 10px;
  }

  .topnav #myLinks {
    display: inline-block;
  }

  .topnav a.icon {
    background: black;
    display: none;
    right: 0;
    top: 0;
  }

  .topnav a:hover {
    background-color: #ddd;
    color: black;
  }

  .active {
    background-color: #04aa6d;
    color: white;
  }

  .topnav #myLinks {
    display: inline-block;
  }

  .topnav a.icon {
    background: black;
    display: none;
    position: absolute;
    right: 0;
    top: 0;
  }

  .topnav a:hover {
    background-color: #ddd;
    color: black;
  }

  .active {
    background-color: #04aa6d;
    color: white;
  }
}
</style>
