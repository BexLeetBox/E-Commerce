<template>
  <div class="sidebar">
    <h3>Filter by Category</h3>
    <ul>
      <li v-for="category in categories" :key="category.id">
        <input
          type="checkbox"
          :value="category.id"
          :checked="category.selected"
          @change="filterProducts"
          @click="category.selected = !category.selected"
        />
        <label>{{ category.name }}</label>
      </li>
    </ul>
  </div>
  <Map info="products"></Map>
  <div class="product-container">
    <div v-for="product in products" :key="product.id" class="product-item">
      <img :src="getImage(product.image)" :alt="product.name" class="product-image" />
      <div class="product-details">
        <label for="product-name">Product:</label>
        <h2>{{ product.briefDescription }}</h2>
        <label for="seller-name">Seller:</label>
        <p class="product-description">{{ product.sellerName }}</p>
        <label for="price">Price:</label>
        <p class="product-price">{{ product.price + ' NOK.' }}</p>
        <button @click="addToCart(product, product.id)">Add to cart</button>
        <p id="update-msg" v-if="selectedItem == product.id">{{ updateMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Map from '../components/MapProducts.vue'

export default {
  components:{
    Map
  },
  data() {
    return {
      products: [],
      updateMessage: '',
      categories: [
        { id: 1, name: 'Furniture', selected: false },
        { id: 2, name: 'Clothing', selected: false },
        { id: 3, name: 'Electronics', selected: false },
        { id: 4, name: 'Vehicle', selected: false },
        { id: 5, name: 'Other', selected: false }
        // ...
      ],

      selectedCategories: [],
      config: {},
      id: {},
      allProducts: [],
      selectedItem: 0,
    }
  },
  methods: {
    async addToCart(product, productId) {
      this.config = {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token')
        }
      }
      const id = new FormData()
      id.append('id', product.id)

      this.selectedItem = productId;

      try {
        console.log(product.id)
        const response = await axios.post('http://localhost:8001/addToCart', id, this.config)

        this.updateMessage = 'Added to cart'

          // clear updateMessage after 3 seconds
          setTimeout(() => {
            this.updateMessage = ''
          }, 3000)
        console.log('token' + this.$store.getters.getToken)
        console.log(response.data)
      } catch (error) {
        this.updateMessage = 'Could not add to cart'

          // clear updateMessage after 3 seconds
          setTimeout(() => {
            this.updateMessage = ''
          }, 3000)
        console.error(error)
      }
    },
    async filterProducts() {

  
      this.selectedCategories = this.categories
        .filter((category) => category.selected)
        .map((category) => category.name)

      const params = new URLSearchParams()
      params.append('categories', this.selectedCategories.join(','))

      
      try {
        const response = await axios.get('http://localhost:8001/products', {
          params: params
        })

        this.products = response.data
        console.log(response.data)
      } catch (error) {
        console.error(error)
      }
    },
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
    axios
      .get('http://localhost:8001/products')
      .then((response) => {
        this.allProducts = response.data
        this.products = this.allProducts
        console.log(response.data)
      })
      .catch((error) => {
        console.log(error)
      })
  }
}
</script>

<style scoped>
map-wrapper{
  text-align: center;
}


button {
  background-color: var(--button-green-hover);
  color: white;
  font-size: 1.2rem;
  padding: 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}

button:hover {
  background-color: var(--button-green);
}
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
  button {
    margin-top: 10px;
  }
  .sidebar ul {
    padding: 0;
    margin: 0;
    list-style: none;
  }

  .sidebar li {
    display: inline-block;
    margin-right: 10px;
  }

  .sidebar input[type='checkbox'] {
    margin-right: 5px;
  }
  .product-container {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
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
    box-shadow: 20px 20px 10px rgba(0, 0, 0, 0.1);
  }
  .product-image {
    width: 300px;
    height: 250px;
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
