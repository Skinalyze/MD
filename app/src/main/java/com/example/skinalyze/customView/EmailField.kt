package com.example.skinalyze.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.skinalyze.R

class EmailField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            setError(context.getString(R.string.invalid_email), null)
        } else {
            error = null
        }
    }
}