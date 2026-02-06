<template>
    <PageHeader/>
    <div>
        <router-view/>
    </div>
</template>

<script>
import PageHeader from './components/layout/PageHeader.vue'
export default {
    name: 'App',
    components: {
        PageHeader,
    },
    created() {

    }
}

</script>

<style>
@import "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.css";
@import "https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css";
@import "https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap4.min.css";
@import "https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/cerulean/bootstrap.min.css";
@import "https://cdn.datatables.net/select/1.2.1/css/select.dataTables.min.css";
@import "https://cdn.datatables.net/buttons/1.5.6/css/buttons.dataTables.min.css";
@import "https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css";
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.none {
	display: none;
}
html {
	font-size: 14px;
}
table[datatable] td,table[datatable] th {
	font-size: 13px;
	text-overflow: ellipsis;
	overflow: hidden;
    white-space: nowrap;
    max-width: 150px;
}
th {
	text-align: center;
	vertical-align: middle;
}
.tdhead {
	background: #DDD;
}
div li:hover {
	background: #DDD;
}
input:read-only, .readonly {
	background: whitesmoke;
	color: #778899;
}
</style>
