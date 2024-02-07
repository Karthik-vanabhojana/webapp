# CSYE6225 Network Structure and Cloud Computing

## Description

Creating CI using .yml file for running git actions for a user authentication project for creating user, get self details and update self details with every user having unique username(email Id)
## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) version 11 or higher
- Apache Maven
- MySQL Database
- Git

## Installation

To install and run this project, please follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/Karthik-vanabhojana/webapp.git

2. Move inside the repository
    ```bash
    cd webapp

3. Build Dependency
    ```bash
    mvn -B package --file pom.xml

4. Maven clean Install
    ```bash
    mvn -B clean install

5. Compile with Maven
    ```bash
    mvn clean compile

6. Run the Project

## Git operations 

1. Fork the repository

2. Clone the Forked repository

3. Create a branch 
    ```bash
    git checkout -b <branchName>

4. Make the code changes

5. Check the branches to be Commited
    ```bash
    git status

6. Add the updated file
    ```bash
    git add <FileName>

7. Commit the changes
    ```bash
    git commit -m "<Commit Messages>"

8. Push the changes to Fork branch
     ```bash
    git push origin <branchName>

9. Then go to the branch and raise the pull request if the build file runs sucessfully without errors you should be able to rebase and merge else you wont be able to merge
