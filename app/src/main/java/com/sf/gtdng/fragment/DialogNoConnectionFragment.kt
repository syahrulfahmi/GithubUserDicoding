package com.sf.gtdng.fragment

import androidx.appcompat.widget.AppCompatButton
import com.sf.gtdng.R
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 01/06/20
 */


class DialogNoConnection : DialogFragment() {
    companion object {
        const val TAG = "DialogNoConnection"
    }

    var onButtonClickListener: () -> Unit = {}
    var onBackPressed: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.dialog_no_connection, container, false)

        view.findViewById<AppCompatButton>(R.id.btnTryAgain)?.setOnClickListener {
            onButtonClickListener()
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(matchParent, matchParent)
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    event?.let {
                        return if (event.action == KeyEvent.ACTION_DOWN) {
                            true
                        } else {
                            onBackPressed()
                            false
                        }
                    }
                }
                return false
            }
        })
    }
}