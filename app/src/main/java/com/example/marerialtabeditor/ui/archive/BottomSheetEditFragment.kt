package com.example.marerialtabeditor.ui.archive

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.databinding.FragmentBottomsheetEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class BottomSheetEditFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomsheetEditBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetEditBinding.inflate(inflater, container, false)


        binding.apply {

        }
        return binding.root
    }

    private fun showDeleteDialog(id: Int, context: Context) {
        MaterialAlertDialogBuilder(context).setTitle("Вы действительно хотите удалить табулатуру?")
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                //deleteMeasure(preferences.getString("token", "").toString(), id, context)
                dismiss()
            }.show()
    }

    private fun deleteMeasure(id: Int, context: Context) {

    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme
}