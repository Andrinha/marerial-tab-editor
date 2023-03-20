package com.example.marerialtabeditor.ui.edit

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.room.Room
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.adapters.NoteAdapter
import com.example.marerialtabeditor.databinding.FragmentEditBinding
import com.example.marerialtabeditor.repository.data.Note
import com.example.marerialtabeditor.repository.data.tab.AppDatabase
import com.example.marerialtabeditor.repository.data.tab.Tab
import com.example.marerialtabeditor.utils.onItemClick


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val adapter = NoteAdapter()
    private val viewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getSerializable("tab") != null) {
            val tab = arguments?.getSerializable("tab") as Tab
            viewModel.song.value = tab.song
        }

        binding.apply {
            recyclerSong.apply {
                layoutManager = GridLayoutManager(
                    requireContext(),
                    6,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
                adapter = this@EditFragment.adapter
                recyclerSong.onItemClick {
                    viewModel.focus.value = it
                }
                setPadding(0, 0, 0, 0)
                clipToPadding = false
                clipChildren = false
                addItemDecoration(object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        //super.getItemOffsets(outRect, view, parent, state)
                        outRect.set(0, 0, 0, 0)
                    }
                })
            }

            buttonAddBlock.setOnClickListener {
                val data = viewModel.song.value!!
                data.addEmptyChunk()
                viewModel.song.value = data
            }

            buttonPlay.setOnClickListener {
                viewModel.playSong()
            }

            buttonSave.setOnClickListener {
                insertDataToDatabase()
            }

            buttonFretInc.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.fret++
                    viewModel.setNote(focus, note)
                    if (note.fret != -1)
                        viewModel.playSound(
                            viewModel.loaded.value?.getOrNull(
                                note.fret + viewModel.getOffset(
                                    focus
                                )
                            ) ?: -1
                        )
                }
            }

            buttonFretDesc.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.fret--
                    viewModel.setNote(focus, note)
                    if (note.fret != -1)
                        viewModel.playSound(
                            viewModel.loaded.value?.getOrNull(
                                note.fret + viewModel.getOffset(
                                    focus
                                )
                            ) ?: -1
                        )
                }
            }

            buttonFlagDefault.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.type = Note.Type.DEFAULT
                    viewModel.setNote(focus, note)
                }
            }

            buttonFlagHarmonic.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.type = Note.Type.HARMONIC
                    viewModel.setNote(focus, note)
                }
            }

            buttonFlagMuted.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.type = Note.Type.MUTED
                    viewModel.setNote(focus, note)
                }
            }

            editName.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    editNameLayout.error = "Empty name"
                } else {
                    viewModel.song.value!!.name = it.toString()
                }
            }

            editBand.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    editBandLayout.error = "Empty band"
                } else {
                    viewModel.song.value!!.band = it.toString()
                }
            }

        }

        viewModel.apply {
            song.observe(viewLifecycleOwner) {
                adapter.setData(it.notes)
                binding.editName.setText(it.name)
                binding.editBand.setText(it.band)
            }
            focus.observe(viewLifecycleOwner) {
                adapter.setFocus(it)
                when (adapter.getNote(it).type) {
                    Note.Type.DEFAULT -> binding.toggleButton.check(R.id.button_flag_default)
                    Note.Type.HARMONIC -> binding.toggleButton.check(R.id.button_flag_harmonic)
                    Note.Type.MUTED -> binding.toggleButton.check(R.id.button_flag_muted)
                }
                binding.textFret.text = adapter.getNote(it).toString()
            }
        }
    }

    private fun insertDataToDatabase() {
        val tab = Tab(
            0,
            viewModel.song.value!!
        )
        addTab(tab)
    }

    private fun addTab(tab: Tab) {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().build()

        val dao = db.tabDao()
        dao.addTab(tab)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}