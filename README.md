TaskMngr – First Version

This is the first version of TaskMngr, a task and project management application built with Spring Boot. It allows admins to manage users, projects, and tasks, and basic users to view projects and manage their own accounts.

Tools:
SpringBoot, Maven, Thymeleaf, single Rest Api to get User statistics, PostgreSQL, VSCode.

Features

Admin Capabilities:

Create the first admin user via environment variables.

Create, update, and delete users.

Assign projects and tasks to users.

Force password change for users on next login.

User Capabilities:

View all projects.

Manage their own account (update personal details, change password).

Cannot modify other users or projects.

Rest API to help track the User's completed Projects.

Authentication:

Login and logout functionality.

Admin and basic user roles supported.

Getting Started
1. Clone the repository
git clone https://github.com/DinaKyr/TaskMngr.git


You can also clone it under a different folder name:

git clone https://github.com/DinaKyr/TaskMngr.git TaskMngrClone

2. Set environment variables

Before running the application, set the initial admin user credentials.

Windows PowerShell:

$env:ADMIN_DEFAULT_USERNAME="myAdmin"
$env:ADMIN_DEFAULT_PASSWORD="myPassword"
$env:ADMIN_DEFAULT_ROLE="ROLE_ADMIN"


Windows Command Prompt (cmd.exe):

set ADMIN_DEFAULT_USERNAME=myAdmin
set ADMIN_DEFAULT_PASSWORD=myPassword
set ADMIN_DEFAULT_ROLE=ROLE_ADMIN


Mac/Linux (bash/zsh):

export ADMIN_DEFAULT_USERNAME=myAdmin
export ADMIN_DEFAULT_PASSWORD=myPassword
export ADMIN_DEFAULT_ROLE=ROLE_ADMIN

3. Run the application
mvn spring-boot:run

4. Access the application

Open a browser and go to: http://localhost:8080

Login with the admin credentials you set.
