<script setup>
import TheWelcome from '../components/ProductPage.vue'
import Map from '../components/MapControls.vue';
</script>

<template>
  <main>
    <TheWelcome />
    <Map></Map>
  </main>
</template>
<style scoped>
.map-wrapper {
  display: flex;
  justify-content: center;
}
</style>