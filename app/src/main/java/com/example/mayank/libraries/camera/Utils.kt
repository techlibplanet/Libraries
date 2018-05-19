package com.example.mayank.libraries.camera

object Utils {

    //validate if the string isnull or empty
    fun notNullNotFill(validate: String?): Boolean {
        return if (validate != null) {
            validate.trim { it <= ' ' } != ""
        } else {
            false
        }
    }
}