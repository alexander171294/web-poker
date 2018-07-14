# web-poker

This project is an Angular/Nativescript Web/Mobile poker client and Java/Spring websocket backend. Additionally, the persistence of data is provided by mysql.

In the past, i tried to start this proyect using php and simple html/javascript/jquery client, but for various reasons i never finished it. Now i started this project to continue this ambitious project, but this time using java and angular/Nativescript.

Do't worry, I put simple instructions for you to install all without know how to work these technologies.

This project is only for fun, and i will write the code in a long term and including spaces of times without commit.

Additionally, sorry for my bad english, i speak spanish and learn english only reading books/papers.

## Screenshots

### Lobby

![lobby](https://i.imgur.com/HUCeZBr.png)

### Login

![login](https://i.imgur.com/qaK4Nkv.png)

# Roadmap

## Short term

I will code these subjects in respective order:

### Sit and go

* ~~Frontend register/login (simple) and mysql connected.~~
* Lobby tables and user-data **(at this moment I'm working in this)**
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
* rank system
* Servers Administration/Deployer tool (as jboss of redhat style but in this case working... xD)

# Architecture

## List of techs

Frontend

* Angular +6
* Material Design (although only for login/signup)
* Websocket
* Stomp (for websocket routing)
* Nativescript with Schematics
* ~~GraphQL~~ (in first instance, I wanted to use gql in rest queries but as they are few, the effort make no sense)

Backend

* Java with Spring Boot
* Websockets
* JWT for session handling
* Mysql
* Hibernate
* SLF4J

## Frontend

The frontend is maked with Nativescript Schematics for Angular using shared code.
In my first app i decided to use Apache Cordova for build a web-view based mobile application, but in the last months i tried to apply nativescript, and in the last days they added support to codeshare/schematics to make web and mobile project in the same workspace.
Then i decide to start this proyect with nativescript (by telerik).

The frontend use websockets to connect with the backend (see below).-

### Build frontend:

first clone the repo, then once time use the next command to download dependencies. (This require you download https://nodejs.org/es/) we use version 8.0.

You need Angular Cli (https://angular.io/guide/quickstart) and Nativescript with schematics, we resume the installations on this commands:

´npm install -g @angular/cli´

´npm install -g nativescript´

´npm i -g @nativescript/schematics´

´npm install´

´tns update´

For build/run ~~i write the instructions soon~~ see the wiki.

## Bakend

The backend is splitted in two proyects, the lobby (users login/tables), and the table/turnament severs, for simple test you can use the same physical server to run all of this (Frontend, backend lobby, and backends tables).

You need to start the lobby server and configure your ip in the frontend and the table backends proyect (see in the wiki soon), and start new tables executing process of backends tables. Then build/run the client and login/register.

### Build backend:

You need jdk (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and eclipse (we use oxygen 3).

For run the backend instructions and build/deploy this in a vps/dedicated server you see the wiki (i write the instructions ~~soon~~, including adding this to a apache/nginx server).-

I'm not sure if this is deployable in shared web hosting, but I'll start an investigation after writing this readme :O.
--Revision #1: about this last comment, i lied a little bit... twenty days later i didn't investigate nothing at all... :'(


# Finally

Thanks for reading and I hope this project end in one moment of the universe history.
