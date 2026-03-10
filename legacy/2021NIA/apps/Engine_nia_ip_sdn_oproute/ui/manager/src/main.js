import { createApp } from 'vue';
import App from './App.vue';
import stores from './stores/store';
import routers from './routers/index';
import axios from 'axios';
import HighchartsVue from 'highcharts-vue';
// import Highcharts3D from 'highcharts/highcharts-3d';
import Highcharts from 'highcharts';
//createApp(App).mount('#app')
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

var app = createApp(App);
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$store = stores;
app.use(routers);
app.use(stores);
// app.use(Highcharts3D);
app.use(HighchartsVue);
app.use(Highcharts);
app.mount('#app');