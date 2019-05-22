package com.myoxidae.moneez.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.myoxidae.moneez.model.Category
import com.myoxidae.moneez.model.CategoryStatus
import kotlinx.android.synthetic.main.activity_add_category.*
import android.graphics.Color
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import kotlinx.android.synthetic.main.activity_add_category.save_button
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import com.myoxidae.moneez.R
import com.myoxidae.moneez.picker.iconpicker.IconPicker


class AddCategoryActivity : AppCompatActivity(), ColorPickerDialogListener {
    var color = Color.BLACK
    var icon = MaterialDrawableBuilder.IconValue.SHAPE

    override fun onColorSelected(dialogId: Int, color: Int) {
        this.color = color
    }

    override fun onDialogDismissed(dialogId: Int) {
        button_color.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.CIRCLE)
                .setColor(color)
                .build(),
            null, null, null
        )
    }

    private var inputLayoutName: TextInputLayout? = null

    private var editTextName: EditText? = null
    private var editTextDescription: EditText? = null

    companion object {
        @JvmField
        var EXTRA_CATEGORY = "EXTRA_CATEGORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        inputLayoutName = this.findViewById(R.id.input_layout_name)

        editTextName = findViewById(R.id.edit_text_name)

        val colorDialog = ColorPickerDialog.newBuilder()
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setShowAlphaSlider(false)

        button_color.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.CIRCLE)
                .setColor(color)
                .build(),
            null, null, null
        )
        button_color.setOnClickListener {
            colorDialog.setColor(color).show(this)
        }


        button_icon.setCompoundDrawablesWithIntrinsicBounds(
            MaterialDrawableBuilder.with(this)
                .setIcon(icon)
                .build(),
            null, null, null
        )
        button_icon?.setOnClickListener {
            val picker = IconPicker.newInstance("Select Icon")  // dialog title
            picker.setListener { icon ->
                button_icon?.text = icon.toString().replace("_", " ")
                button_icon?.setCompoundDrawablesWithIntrinsicBounds(
                    MaterialDrawableBuilder.with(this)
                        .setIcon(icon as MaterialDrawableBuilder.IconValue?)
                        .setToActionbarSize()
                        .build(),
                    null, null, null
                )
                this.icon = icon
                picker.dismiss()
            }
            picker.show(supportFragmentManager, "ICON_PICKER")
        }


//        Set toolbar - title and back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "New category"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (!editTextName?.text.isNullOrEmpty() || !editTextDescription?.text.isNullOrEmpty()) {
                    showDialog()
                } else {
                    finish()
                }
            }
        })

//        Enable and disable save button if required fields are filled
        setSaveEnable()

        editTextName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextName?.text.isNullOrEmpty()) {
                    inputLayoutName?.setError("Name can't be empty")
                } else {
                    inputLayoutName?.setError(null)
                }
                setSaveEnable()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        editTextName?.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (!hasFocus) {
                    if (editTextName?.text.isNullOrEmpty()) {
                        inputLayoutName?.setError("Name can't be empty")
                    } else {
                        inputLayoutName?.setError(null)
                    }
                }
                setSaveEnable()
            }
        }

//        save
        save_button.setOnClickListener {
            val data = Intent()
            val name = editTextName?.text.toString()
            val description = editTextDescription?.text.toString()

            val cat = Category(
                name,
                description,
                icon.toString(),
                String.format("%06X", 0xFFFFFF and color),
                CategoryStatus.Visible
            )

            data.putExtra(EXTRA_CATEGORY, cat)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun setSaveEnable() {
        save_button.setEnabled(!editTextName?.text.isNullOrEmpty())
    }

    private fun showDialog() {
        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(this)

        builder.setMessage("You have unsaved changes. Do you want to keep editing?")

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> finish()
            }
        }

        builder.setPositiveButton("Keep editing", dialogClickListener)
        builder.setNegativeButton("Discard", dialogClickListener)

        dialog = builder.create()
        dialog.show()
    }
}
