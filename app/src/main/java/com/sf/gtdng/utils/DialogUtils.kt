package com.sf.gtdng.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.sf.gtdng.R

fun showAlert(context: Context) {
    val builder = AlertDialog.Builder(context, R.style.AlertDialogThemeTransparant)
    // set title dialog
    builder.setTitle("Keluar dari aplikasi?")

    // set pesan dari dialog
    builder
        .setMessage("Klik Ya untuk keluar!")
        .setIcon(R.mipmap.ic_launcher)
        .setCancelable(false)
        .setPositiveButton(
            "Ya"
        ) { _, _ -> // jika tombol diklik, maka akan menutup activity ini

        }
        .setNegativeButton(
            "Tidak",
            DialogInterface.OnClickListener { dialog, id -> // jika tombol ini diklik, akan menutup dialog
                // dan tidak terjadi apa2
                dialog.dismiss()
            })

    // membuat alert dialog dari builder
    val alertDialog: AlertDialog = builder.create()

    // menampilkan alert dialog
    alertDialog.show()
}