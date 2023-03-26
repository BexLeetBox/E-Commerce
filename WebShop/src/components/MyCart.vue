<template>
  <div class="cart-container">
    <div v-for="product in products" :key="product.id" class="cart-item">
      <img :src="getImage(product.image)" :alt="product.name" class="cart-image" />
      <div class="cart-details">
        <label for="briefDescription">Product name:</label>
        <p>{{ product.briefDescription }}</p>
        <label for="sellername">Seller:</label>
        <p class="cart-seller">{{ product.sellerName }}</p>
        <label for="price">Price:</label>
        <p class="cart-price">{{ product.price + ' NOK.' }}</p>
        
      </div>
      <button @click="removeFromCart(product)">Remove</button>
    </div>
    <div class="cart-total">
      <label>Total Price:</label>
      <span>&nbsp;{{ calculateTotalPrice() }} NOK.</span>
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
          name: 'Product 1',
          description: 'This is a description of product 1',
          price: 9.99,
          imageUrl: 'https://dummyimage.com/300x300/000/fff'
        },
        {
          id: 2,
          name: 'Product 2',
          description: 'This is a description of product 2',
          price: 19.99,
          imageUrl: 'https://dummyimage.com/300x300/000/fff'
        },
        {
          id: 3,
          name: 'Product 3',
          description: 'This is a description of product 3',
          price: 29.99,
          imageUrl: 'https://dummyimage.com/300x300/000/fff'
        },
        {
          id: 4,
          name: 'Product 2',
          description: 'This is a description of product 2',
          price: 19.99,
          imageUrl: 'https://dummyimage.com/300x300/000/fff'
        },
        {
          id: 5,
          name: 'Product 3',
          description: 'This is a description of product 3',
          price: 29.99,
          imageUrl: 'https://dummyimage.com/300x300/000/fff'
        }
      ],
      config: {},
      id : {},
    }
  },
  methods: {
    async removeFromCart(product) {
      this.config = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token')
        }
      }
      const id = new FormData();
      id.append('id', product.id)
     
      try {
        console.log(product.id)
        const response = await axios.post('http://localhost:8001/removeFromCart', id, this.config)
        location.reload()
        console.log('token' + this.$store.getters.getToken)
        console.log(response.data)
      } catch (error) {
        console.error(error)
      }
    },
    calculateTotalPrice() {
    let totalPrice = 0;
    for (const product of this.products) {
      totalPrice += product.price;
    }
    return totalPrice;
  }
    ,
    getImage(imageData) {
      try {
        return `data:image/jpeg;base64,${imageData}`
      } catch (e) {
        console.error(e)
        return 'https://dummyimage.com/300x300/000/fff'
      }
    }
  },
  mounted() {
    this.config = {
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      }
    }

    axios
      .get('http://localhost:8001/myCart', this.config)
      .then((response) => {
        this.products = response.data
        console.log(response.data)
      })
      .catch((error) => {
        this.products = [];
        console.log(error)
      })
  }
}
</script>

<style scoped>
/* Mobile styles */

button {
  background-color: var(--button-green-hover);
  color: white;
  font-size: 1rem;
  padding: .7rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  height: 50px;
 
  
}

button:hover {
  background-color: var(--button-green);
}

.cart-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.cart-item {
  width: 99vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 5px;
  background-color: #fff;
  border: 1px solid #ccc;
  
}

.cart-image {
  width: 100px;
  height: 100px;
  object-fit: contain;
}

.cart-details {
  /**display: flex;
  flex-direction: column;
  justify-content: space-between;*/
  display: grid;
  grid-template-columns: repeat(2, 10fr);
  grid-template-rows: repeat(2, auto);
  grid-gap: 10px;
}

.cart-price {
  font-weight: bold;
}

.cart-total {
  display: flex;
  justify-content: center;
  margin-top: auto;
  font-weight: bold;
}
</style>
