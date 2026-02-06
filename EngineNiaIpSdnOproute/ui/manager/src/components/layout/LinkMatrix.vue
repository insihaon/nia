<template>
    <table border="0" width="100%">
	<tr>
		<td colspan="2"><h1>링크지표</h1></td>
	</tr>
	<tr>
        <td align="left"><h6>{{ selectedText }}</h6></td>
		<td align="right">
            <!-- <select class="custom-select-sm" id="selErorCode" @change="getLinkfactor($event)"> -->
            <select v-on:change="onChange($event.target)" :value="selected" class="custom-select-sm" ref="linkfactorchange">
                <option v-for="factor in linkfactors" :key="factor" v-bind:value="factor.value">{{ factor.text }}</option>
			</select>
		</td>
		<!-- <td><button class="btn btn-secondary btn-sm" type="button" id="btnVerify" onclick="verify_dayreport();">자료검증</button>
			<button class="btn btn-secondary btn-sm" type="button" id="btnDelete" onclick="delete_dayreport();">전체자료삭제</button>
			<button class="btn btn-secondary btn-sm" type="button" id="btnDelete" onclick="delete_selectrow();">선택자료삭제</button>
			<button class="btn btn-secondary btn-sm" type="button" id="btnGenerate" onclick="generate_from_request();">자료생성</button>
		</td> -->
	</tr>
	</table>
    <table id="factormatrix" class='table-matrix' width="100%">
    <thead></thead>
    <tbody>
        <tr class="thead-light" align="middle" v-for="(row, index) in content" :key="index">
            <template v-for="(col, indexc) in row" :key="indexc">
                <template v-if="indexc==0 || index==0">
                    <th class="hover-td">{{ col }}</th>
                </template>
                <template v-else>
                    <td class="hover-td">{{ col }}</td>
                </template>
            </template>
        </tr>
    </tbody>
    </table>
</template>

<script>
export default {
    data () {
        return {
            content: Array,
            selected: 'latency',
            selectedText: 'Latency (단위: ms)',
            linkfactors: [
                { value: 'latency', text: 'Latency' },
                { value: 'jitter', text: '지터' },
                { value: 'hops', text: 'Hop수' },
            ]
        }
    },
    methods: {
        onChange(target) {
            this.selected = target.value;
            if(this.selected=='hops')
                this.selectedText = target[target.selectedIndex].innerText + ' (단위: 개)';
            else
                this.selectedText = target[target.selectedIndex].innerText + ' (단위: ms)';
            this.getLinkfactor(this.selected);
        },
        getLinkfactor(val) {
            this.$axios({
                url: 'http://103.22.222.7:8088/ipsdn/opt/factormatrix',
                method: 'get',
                params: {
                    factorname: val
                },
                headers: {
                    'Content-Type' : 'application/json; charset=utf-8',
                    'Authorization' : this.$store.state.token
                },
            })
            .then(response => {
                if(response.data.status==false)
                    alert(response.data.message);
                else
                    this.content = response.data.data;
            });
        },

    },
    mounted() {
        this.getLinkfactor('latency');
    },
}
</script>
<style scoped>

.table-matrix {
  overflow: hidden;
}

.hover-td, .hover-th {
    border: 1px solid #999;
    padding: 10px;
    position: relative;
}

.hover-td:hover::before,
.row:hover::before { 
    background-color: #ffa;
    content: '\00a0';  
    height: 100%;
    left: -5000px;
    position: absolute;  
    top: 0;
    width: 10000px;   
    z-index: -1;        
}

.hover-td:hover::after,
.col:hover::after { 
    background-color: #ffa;
    content: '\00a0';  
    height: 10000px;    
    left: 0;
    position: absolute;  
    top: -5000px;
    width: 100%;
    z-index: -1;        
}

body {
  margin:8em;
}

</style>