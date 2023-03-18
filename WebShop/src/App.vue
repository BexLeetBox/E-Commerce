<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <div id="logodiv">
      <img id="logo" src="./assets/logo.svg" alt="" />
    </div>
    
  </header>
  <div class="mobile-container">
    <div class="topnav">
      <RouterLink to="/" exact class="active">Home</RouterLink>
      <div id="myLinks">
        <RouterLink to="/products">Products</RouterLink>
        <RouterLink to="/cart">My cart</RouterLink>
        <RouterLink to="/sell">Sell</RouterLink>
        <RouterLink to="/about">About us</RouterLink>
      </div>
      <a href="javascript:void(0);" class="icon" @click="menuDr()">
        <i class="fa fa-bars"></i>
      </a>
    </div>
  </div>
  <RouterView />
</template>
<script>
function menuDr() {
  var x = document.getElementById('myLinks')
  if (x.style.display === 'block') {
    x.style.display = 'none'
  } else {
    x.style.display = 'block'
  }
}
</script>
<style scoped>
*{
    font-family: 'Open Sans', sans-serif;
}

#logodiv {
  display: flex;
  justify-content: center;
  align-items: center;
}
#logo{
  max-width: 150px;
  text-align: center;
}
.mobile-container {
  max-width: 520px;
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

@media screen and (min-width: 520px) {
*{
  margin: 10px;
  text-align: center;
}
#logo{
  max-width: 200px;
  text-align: center;
}
  .topnav{
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
