<template>
  <main>
    <div class="sell-item-container">
      <h1>Sell an Item</h1>
      <form
        id="product-form"
        method="post"
        enctype="multipart/form-data"
        @submit.prevent="submitForm"
      >
        <label for="brief-description">Brief Description:</label>
        <input type="text" id="brief-description" name="briefDescription" /><br /><br />
        <label for="full-description">Full Description:</label>
        <textarea id="full-description" rows="4" cols="50" name="full-description"></textarea><br /><br />
        <label for="category">Category:</label>
        <select id="category" name="category">
          <option value="Furniture">Furniture</option>
          <option value="Clothing">Clothing</option>
          <option value="Electronics">Electronics</option>
          <option value="Vehicle">Vehicle</option>
          <option value="Other">Other</option>
        </select><br /><br />
        
        <label for="latitude">Latitude:</label>
        <input type="text" id="latitude" name="latitude" /><br /><br />
        <label for="longitude">Longitude:</label>
        <input type="text" id="longitude" name="longitude" /><br /><br />
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" /><br /><br />
        <label for="image">Image:</label>
        <input type="file" id="image" name="image" @change="onFileChange" /><br /><br />
        <input type="submit" value="Submit" />
      </form>
    </div>
  </main>
</template>

<script>
import Map from './MapSellItem.vue'
import store from '../stores/index'

import axios from 'axios'

export default {
  data() {
    return {
      imageUrl: null,
      imageFile: null,
      description: '',
      price: '',
      config: {}
    }
  },
  methods: {
    onFileChange(event) {
      const file = event.target.files[0]
      this.imageFile = file
      this.imageUrl = URL.createObjectURL(file)
      this.config = {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }
    },
    async submitForm() {
      const formData = new FormData()
      formData.append('briefDescription', document.getElementById('brief-description').value)
      formData.append('fullDescription', document.getElementById('full-description').value)
      formData.append('category', document.getElementById('category').value)
      formData.append('latitude', parseFloat(document.getElementById('latitude').value))
      formData.append('longitude', parseFloat(document.getElementById('longitude').value))
      formData.append('price', parseFloat(document.getElementById('price').value))
      formData.append('image', this.imageFile)

      console.log(localStorage.getItem('token'))
      try {
        const response = await axios.post('http://localhost:8001/sell-item', formData, this.config)
        console.log('token' + this.$store.getters.getToken)
        console.log(response.data)
      } catch (error) {
        console.error(error)
      }
    }
  },
  beforeUnmount() {
    if (this.imageUrl) {
      URL.revokeObjectURL(this.imageUrl)
    }
  }
}
</script>

<style scoped>
button[type='submit'] {
  background-color: var(--button-green-hover);
  color: white;
  font-size: 1.2rem;
  padding: 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

button[type='submit']:hover {
  background-color: var(--button-green);
}

#item-image {
  width: 50%;
}
.sell-item-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  margin: 2rem;
  border: 1px solid #ccc;
  border-radius: 10px;
}

form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 1rem;
}

label {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

input,
textarea, select {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  border-radius: 5px;
  border: 1px solid #ccc;
}

button {
  background-color: #4caf50;
  color: white;
  padding: 0.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #3e8e41;
}

@media screen and (min-width: 700px) {
  /* CSS for larger screens */
}
</style>
