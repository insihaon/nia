<template>
<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="/" style="font-weight:bold;color:red">IP-SDN</a>

<div class="collapse navbar-collapse" id="navbarsExampleDefault">
    <ul class="navbar-nav mr-auto">
        <li class="nav-item">
            <a class="nav-link" href="/linkmatrix">링크지표<span class="sr-only">(current)</span></a>
        </li>
        <!-- <li class="nav-item">
            <a class="nav-link" href="#">메뉴2<span class="sr-only"></span></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#">메뉴2<span class="sr-only"></span></a>
        </li>
        <li class="nav-item dropdown">
            <a class="nav-link" href="#">메뉴3<span class="sr-only"></span></a>
        </li>
        <li class="nav-item dropdown">
            <a class="nav-link" href="#">메뉴4<span class="sr-only"></span></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#">메뉴5<span class="sr-only"></span></a>
        </li>
        <li class="nav-item sr-only">
            <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
        </li>
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="dropdown02" 
                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">메뉴6</a>
            <div class="dropdown-menu" aria-labelledby="dropdown02">
                <a class="dropdown-item" href="#">소메뉴1</a>
                <a class="dropdown-item" href="#">소메뉴2</a>
                <a class="dropdown-item" href="#">소메뉴3</a>
                <a class="dropdown-item" href="#">소메뉴4</a>
                <a class="dropdown-item" href="#">소메뉴5</a>
            </div>
        </li> -->
    </ul>
    <span id="login_status" style="font-size:12px;"><a href= "#" @click="login">{{ loginState }}</a></span>
</div>
</nav>
</template>
<script>
export default {
    name: "PageHeader",
    data () {
        return {
            loginState: '로그인'
        }
    },
    methods: {
        login() {
            this.$axios({
                url: 'http://103.22.222.7:8088/login',
                method: 'post',
                data: JSON.stringify({loginid: 'codej', password: '!codej@'}),
                headers: {
                    'Content-Type' : 'application/json; charset=utf-8',
                },
            })
            .then(response => {
                // store.commit("setToken", response.headers["authorization"]);
                if(response.data.status==true) {
                    this.loginState = '로그인완료';
                    this.$store.dispatch("setToken", response.headers["authorization"]);
                }
                else {
                    console.log(response.data.status);
                    alert(response.data.message);
                }
            });
        }
    },
    mounted() {
        if(this.$store.state.token==null) this.loginState = '로그인하세요';
        else this.loginState = '로그인완료';
    }
}
</script>
<style scoped>

</style>
