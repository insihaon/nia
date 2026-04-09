module.exports = {
  presets: [
    // https://github.com/vuejs/vue-cli/tree/master/packages/@vue/babel-preset-app
    /*
      [2023-07-10]
      yarn install시에 에러발생 core-js와 babel사이 호환이 안되는 에러 발생하여 변경
      '@vue/cli-plugin-babel/preset' => ['@vue/app', { useBuiltIns: 'entry' }]
    */
    ['@vue/app', { useBuiltIns: 'entry' }]
  ],
  'env': {
    'development': {
      // babel-plugin-dynamic-import-node 플러그인은 import()를 모두 require()로 변환하는 한 가지 일만 수행합니다.
      // 이 플러그인은 페이지가 많을 때 핫 업데이트의 속도를 크게 향상시킬 수 있습니다.
      'plugins': ['dynamic-import-node']
    }
  }
}
