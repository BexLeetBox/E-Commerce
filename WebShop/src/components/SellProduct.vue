<template>
  <main>
    <div class="sell-item-container">
      <h1>Sell an Item</h1>
      <form>
        <div>
          <label for="item-image">Upload an Image:</label>
          <input type="file" @change="onFileChange" />
          <img v-if="imageUrl" id="item-image" :src="imageUrl" />
        </div>
        <div>
          <label for="item-description">Product Description:</label>
          <textarea id="item-description" name="item-description"></textarea>
        </div>
        <div>
          <label for="item-price">Price:</label>
          <input type="text" id="item-price" name="item-price" />
        </div>
        <button type="submit">Sell Item</button>
      </form>
    </div>
  </main>
</template>

<script>
export default {
  data() {
    return {
      imageUrl: null,
      imageFile: null,
    }
  },
  methods: {
    onFileChange(event) {
      const file = event.target.files[0]
      this.imageFile = file
      this.imageUrl = URL.createObjectURL(file)
    },
  },
  beforeUnmount() {
    if (this.imageUrl) {
      URL.revokeObjectURL(this.imageUrl)
    }
  },
}
</script>

<style scoped>
button[type="submit"] {
  background-color: var(--button-green-hover);
  color: white;
  font-size: 1.2rem;
  padding: 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

button[type="submit"]:hover {
  background-color: var(--button-green);
}


#item-image{
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
textarea {
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
