Feature: Word count match - positive and negative scenarios

  # sample.txt file given in word-count-api-master project is used for testing
  Scenario: Verify word count 7 - Positive
    Given the words exist with count of '7'
    When a user retrieves word count with auth Yes
    Then the status code is 200
    And response includes the following
      | I      | 5 |
      | this   | 3 |
      | my     | 3 |
      | could  | 2 |
      | know   | 2 |
      | play   | 1 |
      | stupid | 1 |

  Scenario: Verify word count 6 - Positive
    Given the words exist with count of '6'
    When a user retrieves word count with auth Yes
    Then the status code is 200
    And response includes the following
      | I     | 5 |
      | this  | 3 |
      | my    | 3 |
      | could | 2 |
      | know  | 2 |
      | play  | 1 |

  Scenario: Verify word count 1 - Positive
    Given the words exist with count of '1'
    When a user retrieves word count with auth Yes
    Then the status code is 200
    And response includes the following
      | I | 5 |

  Scenario: Verify word count 0 - Positive
    Given the words exist with count of '0'
    When a user retrieves word count with auth Yes
    Then the status code is 200
    And response includes empty record

  Scenario: Verify word count max in descending order - Positive
    Given user enters max word count 44 and expects response in below order
      | {"I":5,"this":3,"my":3,"could":2,"know":2,"play":1,"stupid":1,"die":1,"be":1,"thought":1,"hoped":1,"soul":1,"couch":1,"Is":1,"reptilian":1,"beautiful":1,"and":1,"God":1,"just":1,"on":1,"Space":1,"That":1,"But":1,"always":1,"a":1,"mind":1,"d":1,"learn":1,"like":1,"in":1,"was":1,"friends":1,"really":1,"the":1,"never":1,"Pope":1,"once":1,"to":1,"If":1,"thing":1,"You":1,"sitting":1,"Goodbye":1} |

  Scenario: Verify word count with no authorization - Negative
    Given the words exist with count of '1'
    When a user retrieves word count with auth No
    Then the status code is 401
    And the error message is as below
      | error | Unauthorized |

  Scenario: Verify word count with invalid authorization - Negative
    Given the words exist with count of '1'
    When a user retrieves word count with auth Invalid
    Then the status code is 401
    And the error message is as below
      | error | Unauthorized |

  Scenario: Verify word count with negative number - Negative
    Given the words exist with count of '-1'
    When a user retrieves word count with auth Yes
    Then the status code is 500
    And the error message is as below
      | error | Internal Server Error |

  Scenario: Verify word count with no value - Negative
    Given the words exist with count of ''
    When a user retrieves word count with auth Yes
    Then the status code is 500
    And the error message is as below
      | error | Internal Server Error |

  Scenario: Verify word count with special character - Negative
    Given the words exist with count of '&'
    When a user retrieves word count with auth Yes
    Then the status code is 400
    And the error message is as below
      | error | Bad Request |
