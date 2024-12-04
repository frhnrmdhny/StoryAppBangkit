package com.dicoding.picodiploma.loginwithanimation.view.custom


import android.content.Context
import android.util.AttributeSet
import android.text.TextWatcher
import android.text.Editable
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak perlu implementasi khusus di sini
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Validasi panjang password langsung, termasuk spasi
                error = if (s != null && s.length < 8) {
                    "Password tidak boleh kurang dari 8 karakter"
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak ada tindakan khusus setelah teks berubah
            }
        })
    }
}

