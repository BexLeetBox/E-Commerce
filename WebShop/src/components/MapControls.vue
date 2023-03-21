<template>
  <div class="map-wrapper">
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
          <GMapMarker
            :key="index"
            v-for="(m, index) in markers"
            :position="m.position"
            :clickable="true"
            :draggable="true"
            @click="center = m.position"
          />
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
      markers: [
        {
          position: {
            lat: 51.093048,
            lng: 6.84212
          }
        } // Along list of clusters
      ]
    }
  }
}
</script>


