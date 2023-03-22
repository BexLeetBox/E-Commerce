<template>
    <div class="product-container">
        <div v-for="product in products" :key="product.id" class="product-item">
          <img :src="getImage(product.image)" :alt="product.name" class="product-image" />
          <div class="product-details">
            <h2>{{ product.briefDescription }}</h2>
            <p class="product-description">{{ product.fullDescription }}</p>
            <p class="product-price">{{ product.price }}</p>
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
        },
        {
          id: 4,
          name: "Product 2",
          description: "This is a description of product 2",
          price: 19.99,
          imageUrl: "https://dummyimage.com/300x300/000/fff"
        },
        {
          id: 5,
          name: "Product 3",
          description: "This is a description of product 3",
          price: 29.99,
          imageUrl: "https://dummyimage.com/300x300/000/fff"
        }
      ]
    }
  }, methods: {
    getImage(imageData) {
      return "data:image/jpeg;base64," + atob(imageData);
    }
  },
  mounted() {
    axios.get('http://localhost:8001/products')
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
    width: 70%;
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
        display: grid;
        grid-template-columns: repeat(5,1fr);
        gap: 20px;
        justify-items: center;
      }
      .product-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 10px;
        text-align: center;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 20px 20px 10px rgba(0,0,0,0.1);
      }
      .product-image {
        width: 100%;
        height: auto;
      }
      .product-description {
        margin-top: 10px;
        font-size: 14px;
        line-height: 1.2;
      }
      .product-price {
        margin-top: 5px;
        font-weight: bold;
        font-size: 16px;
      }
    
  }
</style>
