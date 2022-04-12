package com.faiq.smartalarm

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.widget.CalendarView
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.Month
import java.time.Year
import java.util.*

 class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dialogListener: DateDialogListener? = null

    // Untuk Mengaitkan Display Pada I
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DateDialogListener
    }
    // untuk mengantispisai ERROR atau Bug & Display Lebih dari sekali
    override fun onDetach() {
        super.onDetach()
        if (dialogListener != null) dialogListener = null
    }
    // Untuk Memunculkan Dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayofMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity as Context, this, year, month, dayofMonth)
    }
    // Untuk menentuka tipe data(Tahun, Bulan, Hari)
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayofMonth: Int) {
        dialogListener?.onDialogSet(tag, year, month, dayofMonth )
    }
    // untuk dipanggil di Activity agar dapat input dari user
    interface DateDialogListener{
        fun onDialogSet(tag: String?, year: Int, month: Int, dayofMonth: Int)
    }
}