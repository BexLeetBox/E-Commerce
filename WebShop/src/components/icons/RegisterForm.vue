<template>
    <div id="inputs">
      <h1 id="header">Register form</h1>
      <BaseInput class="input" label="Name" type="text" />
      <BaseInput class="input" label="Email" type="email" />
      <div id="username">
        <label>Username</label>
        <input
          data-test="usernameRegInput"
          type="text"
          name="username"
          placeholder="username"
          v-model="user.username"
        />
      </div>
      <div id="password">
        <label id="passwordLabel">Password </label>
        <input
          data-test="passwordRegInput"
          type="password"
          placeholder="password"
          v-model="user.password"
        />
      </div>
      <BaseInput class="input" label="Phone Number" type="tel" />
  
      <button
        id="submitRegistartion"
        v-on:click="handleRegistration"
        data-test="button"
      >
        Register
      </button>
      <label data-test="registrationStatus" v-if="registrationStatus">
        {{ this.registrationStatus }}
      </label>
    </div>
  </template>
  
  <script>
  import BaseInput from "../components/BaseInput.vue";
  import FeedbackService from "@/utils/FeedbackService";
  import { v4 as uuidv4 } from "uuid";
  export default {
    components: { BaseInput },
    name: "RegisterPage",
    methods: {
      async handleRegistration() {
        const user = {
          ...this.user,
          id: uuidv4(),
        };
        if (!(user.username === "" || user.password === "")) {
          const response = await FeedbackService.postUser(user);
          console.log(response);
          if (
            response.username === user.username &&
            response.password === user.password
          ) {
            this.registrationStatus = "Succsefully registered";
          } else this.registrationStatus = "User already registered";
        } else this.registrationStatus = "Please fill out the registration";
      },
    },
    data() {
      return {
        user: {
          id: "",
          username: "",
          password: "",
        },
        registrationStatus: "",
      };
    },
  };
  </script>
  <style scoped>
  #inputs {
    width: 80%;
    margin: auto;
    margin-top: 100px;
    padding: 20px;
    background-color: #fff;
    border: 1px solid #ccc;
    box-shadow: 0 0 10px #ccc;
    text-align: center;
    font-family: Arial, sans-serif;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  #header {
    margin-bottom: 20px;
    font-size: 24px;
    font-weight: bold;
    color: #333;
  }
  
  #username,
  #password {
    display: block; /* added */
    margin-bottom: 20px;
  }
  #username input,
  #password input,
  .inputField {
    padding: 5px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
    width: 200px;
  }
  
  #error {
    margin-top: 10px;
    font-size: 12px;
    font-weight: bold;
    color: #33333391;
  }
  
  #registrationStatus {
    margin-top: 20px;
    font-size: 16px;
    font-weight: bold;
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
    margin-top: 20px;
  }
  
  button:hover {
    background-color: #3e8e41;
  }
  
  /* custom styles for BaseInput component */
  .input {
    margin-top: 20px;
  }
  
  .input .error {
    border-color: red;
  }
  
  .input label {
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 12px;
    font-weight: bold;
    color: #33333391;
  }
  
  label {
    display: block;
    margin-top: 20px;
    margin-bottom: 10px;
    font-size: 16px;
    font-weight: bold;
    color: #333;
  }
  
  #submitRegistartion {
    display: block;
    margin-top: 20px;
  }
  
  ::placeholder {
    text-align: center;
  }
  </style>
  