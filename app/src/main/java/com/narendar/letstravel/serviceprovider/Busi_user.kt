package com.narendar.letstravel.serviceprovider

class Busi_user(val uid:String,val email:String,val station:String,val mobile:String,val location:String)
{
    constructor():this(uid="",email="",station = "",mobile = "",location="")
}