describe('Login Page', () => {
    beforeEach(() => {
      cy.visit('/login')
    })
  
    it('should log in successfully with valid credentials', () => {
      cy.get('[data-test="userInput"]').type('s')
      cy.get('[data-test="passwordInput"]').type('s')
      cy.get('[data-test="button"]').click()
      cy.url().should('include', '/')
    })
  
  })
  