# event-tables-constraints
Usage of Choco Solver v3.3.1 to experiment with JSR-331, Constraint programming.
This application realizes a model that represents an event with
* the list of guests
* the tables and their seats
* the constraints about the relationships between the guests

This project provides the initiation of the model and the variables.
Currently, there are 2 initiation methods implemented:
* Hard coded test data
* Simple file Initiation

Choco Solver is used to calculate all the possible solutions of the problem.
The output of the application is a list of the guests with the corresponding table number for all of them according to the constraints.

# Hard coded test data
Test data is provided in the code in order to demonstrate the functionality.

# Simple file Initiation
The list of guests is given in the guests.csv file. Each line represents a guest, with the first field being an index number, and the second field being the full name.
The list of tables is given in the tables.csv file. Each line represents a table, with the first field being the tag of the table, and the second field being the number of the corresponding seats.
The list of constraints is given in two files (avoids.csv and follows.csv). Each line represents a constraint, mapping two guests that need to follow each other, or to avoid each other respectively.

# Extension
An implementation of the Initiator has to be a class that extends Initiator and provides implementation of:
* getGuestsDefinition
* getTablesDefinition
* getAvoids
* getFollows
