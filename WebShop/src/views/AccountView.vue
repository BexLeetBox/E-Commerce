<script setup></script>

<template>
  <main>
    <RegistrationFields class="input" label="First name" type="text" v-model="user.firstName" />
    <RegistrationFields class="input" label="Last name" type="text" v-model="user.lastName" />
    <RegistrationFields class="input" label="Address" type="text" v-model="user.address" />
  </main>
</template>
