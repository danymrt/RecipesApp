# RecipesApp
Group project for [Mobile and Cloud Application Master's Course 2021-2022](https://corsidilaurea.uniroma1.it/it/view-course-details/2021/30430/20210916103754/d0d41e8f-68d0-47e4-8ec2-1179259a2a30/df40caa0-3655-498a-9462-607851b615b2/ebc2faa2-f8de-483f-94f4-19fbfa794a0e/b23a91fd-d67b-4175-84c0-14166410c9d3?guid_cv=df40caa0-3655-498a-9462-607851b615b2&current_erogata=d0d41e8f-68d0-47e4-8ec2-1179259a2a30) at Sapienza Università di Roma.

### Team members
Felice Massotti ([Felixmassotti](https://github.com/Felixmassotti))

Daniela Moretti ([danymrt](https://github.com/danymrt))

## Overview

RecipesApp is an Android application that allows you to discover recipes based on what you have in our fridge, pantry and freezer.
It is divided into three main functionalities:
* For each recipe it allows the user to save the ingredients in a shopping list, 
see the main steps and information, and have an updated blog with other users.
* Look for supermarkets in the area
* Manage the shopping list and keep track of the user's favorite recipes

## Description

The architecture was built through [Android Architecture Component](https://developer.android.com/topic/architecture)
using the Kotlin programming 
language. We tried to keep the part concerning the business logic in ViewModels, in order to 
separate it from the Activities and Fragments and obtaining more persisten model.

Asynchronous activities are handled with [coroutines](https://developer.android.com/kotlin/coroutines?gclsrc=aw.ds&gclid=Cj0KCQjwr-SSBhC9ARIsANhzu17gB_UbIBsWW3wMbwSuL9wKPRWI87PDgmhkxFHG81_1sbDSF1Xa6hcaAnskEALw_wcB). 
The coroutines allow simple and secure management of network I/O calls to our database and other services. 

Our database is distributed on [PythonAnywhere](https://www.pythonanywhere.com/)
, it is a PaaS service that makes easy to create and run Python programs in the cloud.
We implement a CRUD interface for create, read, update and delete the users and their favourite recipes from two principal MySQL tables:
* **User** : Id, Username, Name, Family Name, Image, Email, Shopping List
* **Recipe**: Id, Id Recipe, Id User, Name, Image and Likes of the recipe, User Token

The first activity in our RecipesApp will be the login page implemented in the MainActivity.
ObjectAnimators are used to implement a small animation and two types of logins are made available through Facebook and Google.
The user will not have to log in every time, but it will be 
possible to keep track of the access that has already occurred or if it still has to take place.
Below there is a preview:


https://user-images.githubusercontent.com/33021786/163560584-fb1e3c6a-792d-48b7-8368-fb7a7d0cf829.mp4


After there will be the main part of the application, where thanks to a Bottom Navigation we will be able to move in the main activities.
In the search part you can enter the ingredients, and thanks to a call to the [Spoonacular API](https://spoonacular.com/food-api) you can get all 
the recipes with the likes received and the degree of feasibility based on the elements we have.
When a recipe is selected, another activity will open, and thanks to a Tab Navigation we will be able to move among the ingredients that compose it
(with the possibility to insert the missing ingredients in our personal shopping list), the main steps to be performed and a part of comments where all 
users can leave a comment (for example by saying how much they liked it, how to change it etc).
The comments part has been implemented with [Firebase](https://firebase.google.com/) services.
A RealTime Database is used to save all the comments of the different users for each recipe and thanks to the Cloud Messaging service it was possible 
to notify all users who had that recipe in their favorites that a comment had been left (with specific tokens that are saved and created for each user).

In the supermarket part we find a Google map showing the nearest supermarkets in the range of 500 meters, to find them a call is made to the function [NearbyPlace](https://developers.google.com/maps/documentation/places/web-service/search-nearby) of Google Place.
While a [FusedLocationProviderClient](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient) is used for the user's location to always keep their location updated.

In the profile part, again thanks to a Tab Navigation we will be able to move into three main functions where you can modify the main user data, keep track of all the favorite recipes and modify our shopping list by removing the elements that we have purchased.

Below there is a general overview of the entire application:

![Overview](https://user-images.githubusercontent.com/33021786/163563561-b65a5530-8390-4e8e-b04d-c64c17557a4c.jpg)

## Setting up
For the setup of the project it will be necessary: 
* Remove the database folder (if necessary: import the folder, by changing the data in the settings.py, in your account on PythonAnywhere)
* Change all the keys needed for REST APIs calls in the string.xml file
* Create an account and manage services from the main websites of: [Google Map](https://console.cloud.google.com/), [Firebase Cloud Messaging and Firebase Realtime Database](https://firebase.google.com/)




