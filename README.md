# web-poker

This project is an Angular/Nativescript Web/Mobile poker client and Java/Spring websocket backend. Additionally, the persistence of data is provided by mysql.

I made a extensible protocol for organize the develop, the RFC of the protocol is in the wiki, please see this link: 

https://github.com/alexander171294/web-poker/wiki

In the past, I tried to start this project using php and simple html/javascript/jquery client, but for various reasons I never finished it. Now I started a new repo to continue this ambitious project, but this time using java and angular/Nativescript.

**Don't worry**, I put simple instructions for you to install all without know how to work these technologies.

This project is only for fun, and I will write the code in a long term and including spaces of times without commit.

Additionally, sorry for my bad english, I speak spanish and learn english only reading books/papers.

## Screenshots

### Lobby

![login](https://i.imgur.com/EZ0442P.png)

![lobby](https://i.imgur.com/NLr5or2.png)

### Table room

![table](https://i.imgur.com/TBJU0wp.png)

### Chips

Full Size:

![fullSize](https://i.imgur.com/TFwcrKS.png)

Scaled here: https://i.imgur.com/GLlGMiN.png

### Cards

![cardsList](https://i.imgur.com/ScEeJ8W.png)

### Command Line Interface ToolKit

![toolkit](https://i.imgur.com/dr7rspE.png)

# Roadmap

## Short term

### Gameplay:

* ~Connect Room to protocol~
* ~Finish details of design~
* Fix a very long list of bugs.
* Polish the signup/login Lobby.

Pre-Alpha Release

### Fine details (RC-1)

* Reconnections.
* Splitted pots.
* Finish 0/70 TODOS/FIXMES in Backend.

Release Candidate 1

### RC2 or Alpha?

 * Fix the list of bugs.
 * Improve source code.
 * General Review.

Release Candidate 2 / Alpha version 0.1

## Medium term

### Turnaments

* Turnaments. [v 0.2]
* User statistics. [v 0.3]
* Full account and follow list. [v 0.4] 
* Chat in the game. [Beta 1]
* Reviews and bugfixes [Beta 2]
* live broadcast [First Version 1.0-LTS] 

## Long term

### Extended versions and tools

* Mobile frontend.
* Desktop frontend (with electronJS)
* Servers Manager Tool (¿Based on RCP?)
* Statistics
* Train AI (Research purpose) 

# Architecture

## List of techs

Frontend

* Angular +6
* Websocket
* Stomp (for websocket routing)
* Nativescript/Xplat.

Backend

* Java with Spring Boot
* Websockets and sockets (for inter-servers communication)
* JWT for session handling
* Mysql

## Download the repo

git clone --recursive [URL from this repo]

if you already cloned this repo, you have to use submodule update:

git submodule update --init --recursive

## Pulling repo:

git pull --recurse-submodule

## Frontend

~~The frontend is maked with Nativescript Schematics for Angular using shared code.
In my first app i decided to use Apache Cordova for build a web-view based mobile application, but in the last months i tried to apply nativescript, and in the last days they added support to codeshare/schematics to make web and mobile project in the same workspace.
Then i decide to start this proyect with nativescript (by telerik).~~

For now I am centered on finish the web version only with Angular and Stomp.

The frontend use websockets to connect with the backend (see below).-

### Build frontend:

first clone the repo, then once time use the next command to download dependencies. (This require you download https://nodejs.org/es/) we use version 8.0.

You need Angular Cli (https://angular.io/guide/quickstart) and Nativescript with schematics, we resume the installations on this commands:

´npm install -g @angular/cli´

´npm install -g nativescript´

´npm i -g @nativescript/schematics´

´npm install´

´tns update´

For build/run see the wiki.

## Bakend

The backend is splitted in ~~two~~ five proyects, the ApiServer for web operations, for see the room servers, or login with your user, and some specific parts of the room protocol as Auth Challenge (See the protocol RFC for more details). The Orchestrator, in charge of register and organize room servers, The room servers for do the game (All tables are a server). Additionally have two projects, one for RFC Protocol DTOs (exchange), and another for Domain/Repository of persistence.

You need to start the Orchestrator, then ApiServer and RoomServer.

### Build backend:

You need jdk (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and eclipse (we use oxygen 3).

~~For run the backend instructions and build/deploy this in a vps/dedicated server you see the wiki (i write the instructions, including adding this to a apache/nginx server).-~~ Docker is all you need.

I'm not sure if this is deployable in shared web hosting, but I'll start an investigation after writing this readme :O.

--Revision #2: about this last comment, I lied a little bit... ~~twenty days~~ a year later I didn't investigate nothing at all... :'(

# Finally

Thanks for reading and I hope this project end in one moment of the universe history.