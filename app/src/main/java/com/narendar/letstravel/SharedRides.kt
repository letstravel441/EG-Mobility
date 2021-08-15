package com.narendar.letstravel
// data class for shared rides
// used in fetching data in rides shared recycler view of your ride fragment
data class SharedRides(var Name:String?=null,var sharePickuplocation:String?=null,var shareDroplocation:String?=null,var shareDate:String?=null,var shareFare:String?=null,var sharePassengers:String?=null, var rideId : String?=null,var publisherId :String?=null , var publisherimage :String?=null,  var Status :String?=null,
 var sharePickupLatlang :String?=null , var shareDropLatlng  :String?=null , var passengersBooked : String?=null,var ridestarted :String?=null, var totalrating: String? =null,var noofridespublished: String?=null)
