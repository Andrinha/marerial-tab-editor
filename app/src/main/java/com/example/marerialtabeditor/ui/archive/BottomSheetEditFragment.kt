package com.example.marerialtabeditor.ui.archive

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.databinding.FragmentBottomsheetEditBinding
import com.example.marerialtabeditor.repository.data.tab.AppDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class BottomSheetEditFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomsheetEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetEditBinding.inflate(inflater, container, false)
        val id = arguments?.getInt("id")

        binding.apply {
            buttonEdit.setOnClickListener {

            }

            buttonDelete.setOnClickListener {
                if (id != null)
                    showDeleteDialog(id, it.context)
            }
        }
        return binding.root
    }

    private fun showDeleteDialog(id: Int, context: Context) {
        MaterialAlertDialogBuilder(context, R.style.Theme_MarerialTabEditor_Dialog_Alert).setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                deleteMeasure(id, context)
                dismiss()
            }.show()
    }

    private fun deleteMeasure(id: Int, context: Context) {
        val dao = AppDatabase.getDatabase(context).tabDao()
        runBlocking(Dispatchers.Default) {
            dao.deleteItem(id)
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme
}