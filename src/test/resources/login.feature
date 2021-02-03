Feature: Login

  Background:
    Given A Task for Login Exists

  Scenario: Correct Password and username
    Then I enter the username "test"
    And I enter the password "abc"
    Then I have finished the task

  Scenario: Correct user with invalid password
    Then I enter the username "test"
    And I enter the password "thisiswrong"
    Then I have to do task again


  Scenario: Invalid user with invalid password
    Then I enter the username "thisiswrong"
    And I enter the password "thisiswrong"
    Then I have to do task again


