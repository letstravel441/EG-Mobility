package com.narendar.letstravel.mybike

data class ProductsList(
    var title: String? = null,
    var price: String? = null,
    var userID: String? = null,
    var username: String? = null,

    var imagePreview: String? = null,

    var productID: String? = null,

    var brand: String? = null,
    var fuelType : String? = null,
    var colour: String? = null,
    var ownershipStatus: String? = null,
    var bodyType: String? = null,
    var sellerLocation: String? = null,
    var odometerStatus: String? = null,
    var insuranceStatus: String? = null,
    var comment: String? = null,
    var yearOfBuy: String? = null,
    var mileage: String? = null
)