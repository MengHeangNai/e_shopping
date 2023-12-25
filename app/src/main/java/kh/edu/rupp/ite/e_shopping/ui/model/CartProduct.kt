package kh.edu.rupp.ite.e_shopping.ui.model

data class CartProduct (
    val id:Int,
    val name:String,
    val store:String,
    val image:String,
    val price:String,
    val newPrice:String?,
    val quantity:Int,
    val color:String,
    val size:String
        ) {
    constructor() : this(0,"","","","","",0,"","")
}
