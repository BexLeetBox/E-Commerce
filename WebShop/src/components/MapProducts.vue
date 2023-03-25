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
          <GMapMarker :key="index" v-for="(m, index) in info" :position="{lat: m.latitude, lng: m.longitude}" :clickable="true" :draggable="false"
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
export default {
  data() {
    return {
      center: { lat: 63.4255, lng: 10.3952 },
      options: {
        mapId: '1e8c8a920e03249a'
      },
    }
  },
  props: {
    products: {
      type: Array
    }
  },
  methods: {
    openMarker(id) {
       this.openedMarkerID = id
    }
  }
}
</script>


