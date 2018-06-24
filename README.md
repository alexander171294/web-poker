# web-poker

This project is for web Angular/Nativescript (Web/Mobile) poker client and Java/Spring websocket backend. And persistence of data is provided by mysql.

In the past, i try to start this proyect using php and simple html/javascript/jquery client. Now i started this project to continue this ambitious project, but this time using java and angular.

Do't worry, I put simple instructions for you to install this without know how to work this technologies.

This project is only for fun, and i will write the code in long term and including gaps of sleep times.

Additionally, sorry for my bad english, i speak spanish and learn english only reading books/papers.

# Roadmap

## Short term

In order, i code this steps:

### Sit and go

* Lobby register/login (simple) and mysql connected. **(at this moment I'm going here)**
* Frontend register/login (Simple).
* Table servers registry and showed in the frontend.
* Poker rules and gameplay.
* Frontend design of simple table.

## Medium term

### Turnaments

* Turnaments.
* User statistics.
* Full account.
* Chat in the game.

## Long term

### Extended versions and tools

* Mobile frontend.
* Desktop frontend (with electronJS)
* Servers Administration/Deployer tool (as jboss of redhat style xD)

# Architecture

## Frontend

The frontend is maked with Nativescript Angular Schematic for shared code.
In the first app i decided to use Apache Cordova to build a web-view based mobile application, but in the last months tried to apply nativescript, and in the last days they added support to codeshare to make web and mobile project in the same workspace.
Then i decide to start this proyect with nativescript by telerik.

The frontend use websockets to connect with the backend (see below).-

### Build frontend:

first clone the repo, then once time use the next command to download dependencies. (This require you download https://nodejs.org/es/) we use version 8.0.

You need Angular Cli (https://angular.io/guide/quickstart) and Nativescript with schematics, we resume the installations in this commands:

´npm install -g @angular/cli´

´npm install -g nativescript´

´npm i -g @nativescript/schematics´

´npm install´

´tns update´

For build/run i write the instructions soon.

## Bakend

The backend is splitted in two proyects, the lobby (users login/tables), and the table/turnament severs, for simple test you can use the same physical server to run all of this (Frontend, backend lobby, and backends tables).

You need to start the lobby server and configure your ip in the frontend and the table backends proyect (see in the wiki soon), and start new tables executing process of backends tables. Then build/run the client and login/register.

### Build backend:

You need jdk (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and eclipse (we use oxygen 3).

For run the backend instructions and build/deploy this in a vps/dedicated server you see the wiki (i write the instructions soon, including adding this to a apache/nginx server).-

I'm not sure if this is deployable in shared web hosting, but start research before write the readme :O

# Finally

Thanks for reading and I hope this project end in one moment of the universe history.
