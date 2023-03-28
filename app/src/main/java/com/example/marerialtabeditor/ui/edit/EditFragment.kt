package com.example.marerialtabeditor.ui.edit

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
                itemAnimator?.changeDuration = 0
                layoutManager = GridLayoutManager(
                    requireContext(),
                    6,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
                adapter = this@EditFragment.adapter
                recyclerSong.onItemClick {
                    viewModel.focus.value = it
                    viewModel.selectedColumn.value = it / 6
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
                adapter.notifyItemRangeChanged(viewModel.playingColumn.value!!, 6)
                play()
            }

            buttonStop.setOnClickListener {
                adapter.notifyItemRangeChanged(viewModel.playingColumn.value!!, 6)
                stop()
            }

            buttonPause.setOnClickListener {
                pause()
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
                if (viewModel.focus.value == null)
                    return@setOnClickListener

                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)

                if (note.type == Note.Type.HARMONIC) {
                    note.fret = when (note.fret) {
                        24 -> -1
                        19 -> 24
                        16 -> 19
                        12 -> 16
                        9 -> 12
                        7 -> 9
                        5 -> 7
                        4 -> 5
                        else -> 4
                    }
                } else {
                    note.fret++
                }
                viewModel.setNote(focus, note)

                if (note.fret == -1)
                    return@setOnClickListener

                val sound = when (note.type) {
                    Note.Type.DEFAULT -> {
                        viewModel.loaded.value?.getOrNull(
                            note.fret + viewModel.getOffset(
                                focus
                            )
                        ) ?: -1
                    }
                    Note.Type.HARMONIC -> {
                        viewModel.loadedHarmonics.value?.getOrNull(
                            viewModel.getHarmonic(focus, note.fret)
                        ) ?: -1
                    }
                    else -> {
                        viewModel.muteSound.value!!
                    }
                }

                viewModel.playSound(sound)
            }

            buttonFretDec.setOnClickListener {
                if (viewModel.focus.value == null)
                    return@setOnClickListener

                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                if (note.type == Note.Type.HARMONIC) {
                    note.fret = when (note.fret) {
                        24 -> 19
                        19 -> 16
                        16 -> 12
                        12 -> 9
                        9 -> 7
                        7 -> 5
                        5 -> 4
                        4 -> -1
                        else -> 24
                    }
                } else {
                    note.fret--
                }
                viewModel.setNote(focus, note)
                if (note.fret == -1)
                    return@setOnClickListener

                val sound = when (note.type) {
                    Note.Type.DEFAULT -> {
                        viewModel.loaded.value?.getOrNull(
                            note.fret + viewModel.getOffset(
                                focus
                            )
                        ) ?: -1
                    }
                    Note.Type.HARMONIC -> {
                        viewModel.loadedHarmonics.value?.getOrNull(
                            viewModel.getHarmonic(focus, note.fret)
                        ) ?: -1
                    }
                    else -> {
                        viewModel.muteSound.value!!
                    }
                }

                viewModel.playSound(sound)
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
                    binding.textBpm.setText(viewModel.tab.value!!.song.bpm.toString())
                }
            }
            buttonBpmDec.setOnClickListener {
                if (viewModel.tab.value!!.song.bpm > 40) {
                    viewModel.tab.value!!.song.bpm = viewModel.tab.value!!.song.bpm.dec()
                    binding.textBpm.setText(viewModel.tab.value!!.song.bpm.toString())
                }
            }
            textBpm.addTextChangedListener {
                val bpm = it.toString().toIntOrNull()
                if (bpm != null && viewModel.tab.value!!.song.bpm != bpm && bpm in 40..240) {
                    viewModel.tab.value!!.song.bpm = bpm
                }
            }
            textFret.addTextChangedListener {
                if (viewModel.focus.value == null)
                    return@addTextChangedListener

                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                val newFret = it.toString().toIntOrNull()
                if (newFret == null || note.fret == newFret || newFret !in 0..24)
                    return@addTextChangedListener

                note.fret = newFret
                viewModel.setNote(focus, note)
            }
            buttonCopy.setOnClickListener {
                if (viewModel.focus.value == null)
                    return@setOnClickListener

                val column = viewModel.focus.value!! / 6
                val copied = Array(6) {
                    viewModel.tab.value!!.song.notes[column * 6 + it]
                }
                viewModel.copiedColumn.value = copied
            }
            buttonPaste.setOnClickListener {
                if (viewModel.focus.value == null)
                    return@setOnClickListener

                val column = viewModel.focus.value!! / 6
                val tab = viewModel.tab.value!!
                repeat(6) {
                    tab.song.notes[column * 6 + it] = viewModel.copiedColumn.value!![it]
                }
                viewModel.tab.value = tab
            }
        }

        viewModel.apply {
            tab.observe(viewLifecycleOwner) {
                adapter.setData(it.song.notes)
                binding.editName.setText(it.song.name)
                binding.editBand.setText(it.song.band)
                binding.textBpm.setText(it.song.bpm.toString())
            }
            focus.observe(viewLifecycleOwner) {
                adapter.setFocus(it)
                when (adapter.getNote(it).type) {
                    Note.Type.DEFAULT -> binding.toggleButton.check(R.id.button_flag_default)
                    Note.Type.HARMONIC -> binding.toggleButton.check(R.id.button_flag_harmonic)
                    Note.Type.MUTED -> binding.toggleButton.check(R.id.button_flag_muted)
                }
                binding.textFret.setText(adapter.getNote(it).toString())
            }
            playingColumn.observe(viewLifecycleOwner) {
                adapter.setColumn(it)
            }
        }
    }

    private fun pause() {
        if (viewModel.isPlaying.value == true) {
            viewModel.isPlaying.value = false
        } else playTab()
    }

    private fun stop() {
        if (viewModel.isPlaying.value == true) {
            viewModel.isPlaying.value = false
            adapter.notifyItemRangeChanged(viewModel.playingColumn.value!! * 6, 6)
            viewModel.playingColumn.value = -1
            return
        }
        playTab()
    }

    private fun play() {
        if (viewModel.isPlaying.value == true) {
            viewModel.isPlaying.value = false
            adapter.notifyItemRangeChanged(viewModel.playingColumn.value!! * 6, 6)
            viewModel.playingColumn.value = -1
            return
        }
        if (viewModel.focus.value != null)
            viewModel.playingColumn.value = viewModel.focus.value!! / 6
        playTab()
    }

    private fun playTab() {
        lifecycleScope.launch {
            viewModel.isPlaying.value = true
            val bpm = viewModel.tab.value!!.song.bpm
            val delay = 1000L * 16L / bpm
            val size = viewModel.tab.value!!.song.notes.size
            val startColumn = if (viewModel.playingColumn.value!! == -1)
                0 else viewModel.playingColumn.value!!
            for (y in startColumn until size / 6) {
                if (viewModel.isPlaying.value != true) {
                    return@launch
                }
                binding.recyclerSong.smoothScrollToPosition(6 * (y + 5))
                viewModel.playingColumn.value = y
                playColumn(y, delay)
            }
            viewModel.playingColumn.value = -1
            viewModel.isPlaying.value = false
        }
    }

    private suspend fun playColumn(y: Int, duration: Long) {
        repeat(6) { x ->
            val position = x + 6 * y
            val note = viewModel.tab.value!!.song.notes[position]
            when {
                note.type == Note.Type.MUTED -> viewModel.playSound(viewModel.muteSound.value!!)
                note.type == Note.Type.HARMONIC -> viewModel.playSound(
                    viewModel.loadedHarmonics.value?.getOrNull(
                        viewModel.getHarmonic(position, note.fret)
                    ) ?: -1
                )
                note.fret != -1 -> viewModel.playSound(
                    viewModel.loaded.value?.getOrNull(
                        note.fret + viewModel.getOffset(
                            position
                        )
                    ) ?: -1
                )
            }
        }
        delay(duration)
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