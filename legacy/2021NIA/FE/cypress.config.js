const { defineConfig } = require('cypress')
const fs = require('fs')


module.exports = defineConfig({
  component: {
    devServer: {
      framework: 'vue-cli',
      bundler: 'webpack',
    },
  },
  e2e: {
    baseUrl: 'http://localhost:4000',
    // 자동 테스트 코드 생성 설정
    experimentalStudio: true,
    failOnStatusCode: true,
    // 크롬 동일 출처 정책 보안 비활성화
    chromeWebSecurity: false,
    // numTestsKeptInMemory: 5, // Reduce to 10 tests
    setupNodeEvents(on, config) {
      // implement node event listeners here
      on('task', {
        saveDataToFile({ filePath, data }) {
          return new Promise((resolve, reject) => {
            fs.writeFile(filePath, JSON.stringify(data, null, 2), (err) => {
              if (err) {
                reject(err);
              } else {
                resolve('Data saved successfully')
              }
            })
          })
        },
        loadDataFromFile(filePath) {
          return new Promise((resolve, reject) => {
            fs.readFile(filePath, 'utf8', (err, data) => {
              if (err) {
                reject(err);
              } else {
                resolve(JSON.parse(data))
              }
            })
          })
        }
      })
    },
  },
});
