describe('File Save Example', () => {
  beforeEach(() => {
    cy.visitLocal()
  })
  it('should save data to a file', () => {
    cy.setToken()
    cy.visit('/ipAssignMng/ipBlockManagement');
    // cy.visit('http://localhost:4000/#/ipAssignMng/ipBlockManagement')
  })
})
// describe('File Save Example', () => {
//   it('should save data to a file', () => {
//     const data = {
//       token: 'exampleToken123',
//       username: 'testUser',
//     };

//     cy.task('saveDataToFile', { filePath: 'cypress/fixtures/data.json', data })
//       .then((message) => {
//         cy.log(message); // Log success message
//       });
//   });
// });
// describe('File Load Example', () => {
//   it('should load data from a file', () => {
//     cy.task('loadDataFromFile', 'cypress/fixtures/data.json').then((data) => {
//       cy.log('Loaded data:', data);
//       expect(data.token).to.exist;
//     });
//   });
// });