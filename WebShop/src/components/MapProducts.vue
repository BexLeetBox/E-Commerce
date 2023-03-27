<template>
  <div class="map-wrapper">
    <div>{{info}}</div>
      <GMapMap
        :center="center"
        :zoom="12"
        :options="{
          zoomControl: true,
          mapTypeControl: false,
          scaleControl: false,
          streetViewControl: false,
          rotateControl: false, 
          fullscreenControl: false
        }"
        style="width: 500px; height: 300px"
      >
        <GMapCluster>
          <GMapMarker :key="index" v-for="(m, index) in products" :position="{lat: m.latitude, lng: m.longitude}" :clickable="true" :draggable="false"
        @click="openMarker(m.id)" >
          <GMapInfoWindow
          :closeclick="true"
          @closeclick="openMarker(null)"
          :opened="openedMarkerID === m.id">
              <div> {{ m.brief-description }} </div>
           </GMapInfoWindow>
          </GMapMarker>
        </GMapCluster>
      </GMapMap>
  </div>
</template>

<script>
import axios from 'axios'


export default {
  data() {
    return {
      products: [],
      center: { lat: 63.4255, lng: 10.3952 },
      options: {
        mapId: '1e8c8a920e03249a'
      },
    }
  },
  mounted() {
    axios
      .get('http://localhost:8001/products')
      .then((response) => {
        this.products = response.data
        console.log(response.data)
      })
      .catch((error) => {
        console.log(error)
      })
  },
  methods: {
    openMarker(id) {
       this.openedMarkerID = id
    }
  }
}
</script>
<style scoped>
.vue-map-container{
  display: flex;
  justify-content: center;

}
</style>

