package com.apps.kim.todo.tools.classes

/**
 * Created by UserModel on 15.07.2017.
 */

class UserFacebookModel {
    var id: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var email: String? = null

    override fun toString(): String {
        return "UserFacebookModel{" +
                "id='" + id + '\''.toString() +
                "first_name='" + first_name + '\''.toString() +
                ", last_name='" + last_name + '\''.toString() +
                ", email='" + email + '\''.toString() +
                '}'.toString()
    }
}
