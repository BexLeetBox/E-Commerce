<script setup></script>
<template>
  <div class="account-container">
    <h1>Account details</h1>
    <form class="account-form">
      <div class="form-group">
        <label for="first-name">First Name:</label>
        <input type="text" id="first-name" name="first-name" required />
      </div>
      <div class="form-group">
        <label for="last-name">Last Name:</label>
        <input type="text" id="last-name" name="last-name" required />
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required />
      </div>
      <hr>
      <h2>Change Password</h2>

      <div class="form-group">
        <label for="old-password">Old password:</label>
        <input type="password" id="old-password" name="old-password" required />
      </div>
      <div class="form-group">
        <label for="new-password">New password:</label>
        <input type="password" id="new-password" name="new-password" required />
      </div>
      <button type="submit">Update Account</button>
    </form>
  </div>
</template>

<style>
hr {
  margin: 10px;
  border-color: var(--dark-grey);
  width: 50%;
}
h2{
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
input[type='email'],
input[type='password'] {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  width: 100%;
}
</style>
