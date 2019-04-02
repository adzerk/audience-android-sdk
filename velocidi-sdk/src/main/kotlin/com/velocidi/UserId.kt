package com.velocidi

class UserId(val type: String, val id:String) {
    fun toQueryString():String =
        "id_${this.type}=${this.id}"
}
