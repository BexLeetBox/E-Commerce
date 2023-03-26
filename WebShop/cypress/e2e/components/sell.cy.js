describe('Sell Item Page', () => {
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
      cy.fixture('example.jpg').then((fileContent) => {
        cy.get('#image').upload(
          { fileContent, fileName: 'example.jpg', mimeType: 'image/jpeg' },
          { subjectType: 'input' }
        )
      })
  
      // Submit the form
      cy.get('#product-form').submit()
  
      // Assert that the update message appears and contains the correct text
      cy.get('#update-msg').should('be.visible').and('contain', 'Item is now on sale')
    })
  
    it('should display an error message if not all fields are filled', () => {
      // Fill in only some of the required form fields
      cy.get('#brief-description').type('Example Brief Description')
      cy.get('#full-description').type('Example Full Description')
  
      // Submit the form
      cy.get('#product-form').submit()
  
      // Assert that the error message appears and contains the correct text
      cy.get('#update-msg').should('be.visible').and('contain', 'Fill in all forms appropriately')
    })
  })
  