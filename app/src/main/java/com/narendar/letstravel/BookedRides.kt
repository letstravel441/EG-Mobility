package com.narendar.letstravel
//this data class is useful to assign the values to variables from database.
//Variable names and type must be same as the name of the fields stored in the database.
data class BookedRides(var publisherName:String?=null,var sharePickuplocation:String?=null,var shareDroplocation:String?=null,
                       var bookPickuplocation:String?=null,var bookDroplocation:String?=null,var bookDate:String?=null,var Fare:String?=null,var bookPassengers:String?=null, var rideId : String?=null,
                       var publishedRideId :String?=null,var totalseats :String?=null, var passengersBooked :String?=null, var message : String?=null,
                       var shareDate:String?=null,var bookFare:String?=null , var publisherId :String?=null,
                        var bookerId :String?=null, var BookerName  :String?=null, var ridestarted: String? = null,var ratingofpublisher: String?=null)

