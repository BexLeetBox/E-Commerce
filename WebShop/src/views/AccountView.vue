<script setup></script>
<template>
  <div class="account-container">
    <h1>Account details</h1>
    <form class="account-form" @submit.prevent="updateAccount">
      <div class="form-group">
        <label for="first-name">First Name:</label>
        <input type="text" id="first-name" name="first-name" v-model="firstName" required />
      </div>
      <div class="form-group">
        <label for="last-name">Last Name:</label>
        <input type="text" id="last-name" name="last-name" v-model="lastName" required />
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" v-model="email" required />
      </div>
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="phone-number">Phone Number:</label>
        <input type="number" id="phone-number" name="phone-number" v-model="phoneNumber" required />
      </div>
      <div class="form-group">
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" v-model="address" required />
      </div>
      <button type="submit">Update Account</button>
    </form>

    <form class="account-form" @submit.prevent="updateAccount">
    <h2>Change Password</h2>

      <div class="form-group">
        <label for="old-password">Old password:</label>
        <input type="password" id="old-password" name="old-password" v-model="oldPassword" required />
      </div>
      <div class="form-group">
        <label for="new-password">New password:</label>
        <input type="password" id="new-password" name="new-password" v-model="newPassword" required />
      </div>
      <button type="submit">Change password</button>
    </form>
    <div id="logoutDiv">
    <button type="button" @click="logOut" style="margin: 10px;">Log out</button>
  </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      password: '',
      phoneNumber: '',
      address: '',
      oldPassword: '',
      newPassword: '',
      config: {},
    }
  },
  methods: {
    updateAccount() {
      const data = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        oldPassword: this.oldPassword,
        newPassword: this.newPassword,
        phoneNumber: this.phoneNumber,
        address: this.address,
        username: this.username
      }
      const config = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token')
        }
      }
      this.config = config
      axios
        .put('http://localhost:8001/updateAccount', data, config)
        .then((response) => {
          console.log(response.data)
        })
        .catch((error) => {
          console.error(error)
        })
    }
  },
  mounted(){
    const config = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token')
        }
      }
      this.config = config
    // Fetch the account data from the backend and populate the form fields with the data
    axios.get('http://localhost:8001/account', this.config)
      .then(response => {
        const data = response.data;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.email = data.email;
        this.phoneNumber = data.phoneNumber;
        this.address = data.address;
        this.username = data.username;
      })
      .catch(error => {
        console.error(error);
      });
  }
}
</script>

<style>
#logoutDiv{
  margin-top: 5px;
  background-color: #333333;
  width: 100vw;
}
hr {
  margin: 10px;
  border-color: var(--dark-grey);
  width: 50%;
}
h2 {
  padding: 10px;
  color: black;
}

button {
  background-color: var(--button-green);
  color: white;
  font-size: 1rem;
  padding: 0.7rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

button:hover {
  background-color: var(--button-green-hover);
}
.account-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}

.account-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 50%;
  border: 1px solid #ccc;
  padding: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
}

input[type='text'],
input[type='number'],
input[type='email'],
input[type='password'] {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  width: 100%;
}
</style>
