Feature: Demo API
  Background: Create booking data
    Given I have data of customer booking
    When I call API to create booking information
    Then the API should return status 200 and all fields display correctly

  @API @Post
  Scenario: Create booking
    Given I have data of customer booking
    When I call API to create booking information
    Then the API should return status 200 and all fields display correctly

  @API @Get
  Scenario: Get booking according to id
    When I call API to get customer booking information with id created above
    Then the API should return status 200 and response display correctly

  @API @Put
  Scenario:  Updates a current booking
    When I call API to "update" customer booking information with id created above
    Then the API should return status 200 and data is updated successfully

  @API @Delete
  Scenario: Delete a current booking
    When I call API to "delete" customer booking information with id created above
    Then the API should return status 201 and data is deleted successfully