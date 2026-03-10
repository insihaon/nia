Vue.component('cItem', {
  props: ['cdata'],
  template: '<div>{{cdata.linkName}}</div>'
})

var app = new Vue({
  el: '#app',
  data: {
    candidateNum: 0,
    connectionOrderList: [],
    protectedLinkList: [],
    protectionLinkList: []
  },
  mounted() {
    this.getData('json_01.json');
  },
  methods: {
    getData(file) {
      axios.get('./js/' + file)
        .then(response => {
          this.candidateNum = response.data.topologyData.candidateList[0].candidateNum;
          this.connectionOrderList = response.data.topologyData.candidateList[0].connectionOrderList != undefined ? response.data.topologyData.candidateList[0].connectionOrderList : [];
          this.protectedLinkList = response.data.topologyData.candidateList[0].protectedLinkList != undefined ? response.data.topologyData.candidateList[0].protectedLinkList : [];
          this.protectionLinkList = response.data.topologyData.candidateList[0].protectionLinkList != undefined ? response.data.topologyData.candidateList[0].protectionLinkList : [];

          // console.log(this.candidateNum);
          console.log(this.connectionOrderList);
          console.log(this.protectedLinkList);
          console.log(this.protectionLinkList);
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
})