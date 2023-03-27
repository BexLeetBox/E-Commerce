describe('Listing page after sell', () => {
    beforeEach(() => {
      // Log in the user before each test
      cy.login('s', 's')
      cy.visit('/sell')
    })
  
    it('should submit the form successfully with all fields filled', () => {
      // Fill in all required form fields
      cy.get('#brief-description').type('Example Brief Description')
      cy.get('#full-description').type('Example Full Description')
      cy.get('#category').select('Other')
      cy.get('#latitude').type('59.9139')
      cy.get('#longitude').type('10.7522')
      cy.get('#price').type('100')
      cy.get('#image').attachFile('example.jpg', { subjectType: 'input' });
      cy.get('form').submit()
  
      // Confirm that the item is added to the user's listing
      cy.visit('/')
      cy.contains('Example Brief Description').should('exist')
    })
  })