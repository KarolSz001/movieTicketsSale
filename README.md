"# movieTicketsSale" 
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)

## General info
Data Base application with JDBI implementation

## Technologies
* Java - version 12
* gson - version 2.8.4
* maven - version 3.6
* Mocito

## Setup
download, compile and run
Local instance MySQL80
localhost port 3306
root // admin

## Code Examples
public List<MovieWithDateTime> getInfo() {
        return connection.withHandle(handle ->
                handle.createQuery("select ss.id, mm.title, ss.start_date_time, mm.price, mm.genre, mm.duration, cc.name, cc.surname, cc.email " +
                        "FROM sales_stand ss JOIN movie mm " +
                        "ON ss.movie_id = mm.id " +
                        "INNER JOIN customer cc " +
                        "ON cc.id = ss.customer_id;")
                        .mapToBean(MovieWithDateTime.class)
                        .list()
        );

## Features

To-do list:
- cleanCode - optimize code 
- Junit
- SQL
- Json
- Mocito
- pretty look console



## Status
Project is: _in_progress_