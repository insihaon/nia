<template>
    <div class="left50 mb-3">
        노드자원선택
        <select class="form-select" aria-label="Default select example">
            <option value="CPU" selected>CPU</option>
            <option value="MEM">Memory</option>
        </select>
        <highcharts :options="chartOptions"/>
    </div>
</template>
<script>
import highcharts3d from 'highcharts/highcharts-3d';
import highcharts from 'highcharts';
highcharts3d(highcharts);
export default {
    name: 'MainPage',
    data() {
        const chartTypeOptions = {
            type: 'line',
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 0,
                depth: 10,
                viewDistance: 100
            }
        }
        const titleOptions = {
            useHTML: true,
            text: '노드 CPU/Memory 사용율'
        }

        return {
            chartOptions: {
                series: [
                    // {
                    //     type: 'line',
                    //     // renderTo: 'container',
                    //     name: '구성비율',
                    //     data: [1, 2, 3]
                    // },
                ],
                chart: chartTypeOptions,
                title: titleOptions,
                xAxis: {
                    categories: [],
                    labels: {
                        rotations: 30
                    }
                },
                plotOptions: {
                    line: {
                        showInLegend: true
                    }
                }
                
            },
        }
    },
    methods: {
        getCpuMemusage(factor, toDate) {
            this.$axios({
                url: 'http://103.22.222.7:8080/ipsdn/opt/nodefactor/series',
                method: 'get',
                params: {
                    factorname: factor,
                    sourcedate: toDate,
                },
                headers: {
                    'Content-Type' : 'application/json; charset=utf-8',
                    'Authorization' : this.$store.state.token
                },
            })
            .then(response => {
                if(response.data.status==false)
                    alert(response.data.message);
                else {
                    var nodes = response.data.data;
                    for(var i in nodes) {
                        this.chartOptions.series.push({
                            type: 'line',
                            name: nodes[i].nodeName,
                            data: [],
                        });
                        for(var j in nodes[i].usageRates) {
                            this.chartOptions.series[i].data.push(nodes[i].usageRates[j].usageRate);
                            this.chartOptions.xAxis.categories.push(nodes[i].usageRates[j].sourceDate);
                        }
                    }
                }
            });
        }
    },
    mounted() {
        this.getCpuMemusage('MEM', '2022-11-25');
    },
}
</script>
<style scoped>
div.left50 {
    width: 50%;
    float: left;
}
div.right50 {
    width: 50%;
    float: right;
}
</style>
