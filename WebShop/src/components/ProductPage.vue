<template>
    <div class="product-list">
      <div v-for="product in products" :key="product.id" class="product">
        <img :src="product.imageUrl" :alt="product.name" />
        <div class="details">
          <h2>{{ product.name }}</h2>
          <p>{{ product.description }}</p>
          <p class="price">{{ product.price }}</p>
        </div>
      </div>
    </div>
</template>
  
<script>
import axios from 'axios'

export default {
  data() {
    return {
        products: [
        {
          id: 1,
          name: "Product 1",
          description: "This is a description of product 1",
          price: 9.99,
          imageUrl: "https://dummyimage.com/300x300/000/fff"
        },
        {
          id: 2,
          name: "Product 2",
          description: "This is a description of product 2",
          price: 19.99,
          imageUrl: "https://dummyimage.com/300x300/000/fff"
        },
        {
          id: 3,
          name: "Product 3",
          description: "This is a description of product 3",
          price: 29.99,
          imageUrl: "https://dummyimage.com/300x300/000/fff"
        }
      ]
    }
  },
  mounted() {
    axios.get('https://example.com/products')
      .then(response => {
        this.products = response.data
      })
      .catch(error => {
        console.log(error)
      })
  }
}
</script>

<style scoped>
  /* Mobile styles */
  .product-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  .product-item {
    justify-content: center;
    width: 100%;
    padding: 10px;
  }
  .product-image {
    width: 100%;
    height: auto;
  }
  .product-description {
    margin-top: 10px;
    font-size: 14px;
    text-align: center;
  }
  .product-price {
    margin-top: 5px;
    font-weight: bold;
    font-size: 16px;
    text-align: center;
  }

  /* Tablet and desktop styles */
  @media screen and (min-width: 768px) {
    .product-container {
      justify-content: flex-start;
    }
    .product-item {
      width: calc(33.33% - 20px);
      margin: 10px;
    }
  }
</style>
