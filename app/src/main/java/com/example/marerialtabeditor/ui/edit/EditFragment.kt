package com.example.marerialtabeditor.ui.edit

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            viewModel.tab.value = arguments?.getSerializable("tab") as Tab
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
                val tab = viewModel.tab.value!!
                tab.song.addEmptyChunk()
                viewModel.tab.value = tab
            }

            buttonPlay.setOnClickListener {
                viewModel.playSong()
            }

            buttonSave.setOnClickListener {
                var success = true
                if (editBand.text.isNullOrEmpty()) {
                    success = false
                    editBandLayout.error = "Please enter band"
                } else editBandLayout.error = null

                if (editName.text.isNullOrEmpty()) {
                    success = false
                    editNameLayout.error = "Please enter name"
                } else editNameLayout.error = null

                if (success) {
                    insertDataToDatabase()
                    if (!findNavController().navigateUp())
                        requireActivity().finish()
                }
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

            buttonFretDec.setOnClickListener {
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
                viewModel.tab.value!!.song.name = it.toString()
            }

            editBand.addTextChangedListener {
                viewModel.tab.value!!.song.band = it.toString()
            }
            buttonBpmInc.setOnClickListener {
                if (viewModel.tab.value!!.song.bpm < 240) {
                    viewModel.tab.value!!.song.bpm = viewModel.tab.value!!.song.bpm.inc()
                    binding.textBpm.text = viewModel.tab.value!!.song.bpm.toString()
                }
            }
            buttonBpmDec.setOnClickListener {
                if (viewModel.tab.value!!.song.bpm > 40) {
                    viewModel.tab.value!!.song.bpm = viewModel.tab.value!!.song.bpm.dec()
                    binding.textBpm.text = viewModel.tab.value!!.song.bpm.toString()
                }
            }
        }

        viewModel.apply {
            tab.observe(viewLifecycleOwner) {
                adapter.setData(it.song.notes)
                binding.editName.setText(it.song.name)
                binding.editBand.setText(it.song.band)
                binding.textBpm.text = it.song.bpm.toString()
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
        val tab = viewModel.tab.value!!
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