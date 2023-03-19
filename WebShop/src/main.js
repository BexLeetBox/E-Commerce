import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import store from "./stores";
import VueGoogleMaps from '@fawmi/vue-google-maps'


import './assets/main.css'

const app = createApp(App)

app.use(createPinia());
app.use(router);
app.use(store);
app.use(VueGoogleMaps, {
    load: {
        key: 'AIzaSyCGte24sbY2tFONaLk-lq_fC4ImNQ24opg        ',
    },
})

app.mount('#app');
