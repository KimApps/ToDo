package com.apps.kim.todo.tools.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.text() = text.toString()
fun EditText.trimText() = text.toString().trim()

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}