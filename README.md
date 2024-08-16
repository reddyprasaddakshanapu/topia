# Prerequisites
=================

Java 17

Springboot 3.x

Postgress

Maven 3.x

Liquibase

Import https://api.frankfurter.app certificate to Java certs


How to run this codebase
=========================

Clone the codebase and build using maven command mvn clean install.

add a https://api.frankfurter.app certificate to java certs using java keytool

Create a database called forexservice in postgress

Start Springboot application using main class CurrencyExchangeApplication

