<template>
    <GMapMap
        :center="center"
        :zoom="12"
        :options="{
                        zoomControl: true,
                        mapTypeControl: false,
                        scaleControl: false,
                        streetViewControl: false,
                        rotateControl: false,
                        fullscreenControl: false,
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
            @click="center=m.position"
        />
      </GMapCluster>
    </GMapMap>
  </template>
  <script>
  export default {
    data() {
      return {
        center: {lat: 63.4255, lng: 10.3951},
        options: {
            mapId: '1e8c8a920e03249a'
        },
        markers: [
          {
            position: {
              lat: 51.093048, lng: 6.842120
            },
          }
          , // Along list of clusters
        ]
      }
    }
  }
  </script>