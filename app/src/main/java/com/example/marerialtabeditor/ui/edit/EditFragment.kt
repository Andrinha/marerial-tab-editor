package com.example.marerialtabeditor.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marerialtabeditor.R
import com.example.marerialtabeditor.adapters.NoteAdapter
import com.example.marerialtabeditor.databinding.FragmentEditBinding
import com.example.marerialtabeditor.repository.data.Note
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

        binding.apply {
            recyclerSong.layoutManager = GridLayoutManager(
                requireContext(),
                6,
                GridLayoutManager.HORIZONTAL,
                false
            )
            recyclerSong.adapter = adapter
            binding.recyclerSong.onItemClick {
                viewModel.focus.value = it
            }
            buttonAddChunk.setOnClickListener {
                val data = viewModel.song.value!!
                data.addEmptyChunk()
                viewModel.song.value = data
            }
            buttonFretInc.setOnClickListener {
                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                note.fret++
                adapter.setNote(focus, note)
                textFret.text = note.toString()
            }
            buttonFretDesc.setOnClickListener {
                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                note.fret--
                adapter.setNote(focus, note)
                textFret.text = note.toString()
            }
            buttonFlagDefault.setOnClickListener {
                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                note.type = Note.Flags.DEFAULT
                adapter.setNote(focus, note)
            }
            buttonFlagHarmonic.setOnClickListener {
                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                note.type = Note.Flags.HARMONIC
                adapter.setNote(focus, note)
            }
            buttonFlagMuted.setOnClickListener {
                val focus = viewModel.focus.value!!
                val note = adapter.getNote(focus)
                note.type = Note.Flags.MUTED
                adapter.setNote(focus, note)
            }
        }
        viewModel.apply {
            song.observe(viewLifecycleOwner) {
                adapter.setData(it.notes)
            }
            focus.observe(viewLifecycleOwner) {
                adapter.setFocus(it)
                when (adapter.getNote(it).type) {
                    Note.Flags.DEFAULT -> binding.toggleButton.check(R.id.buttonFlagDefault)
                    Note.Flags.HARMONIC -> binding.toggleButton.check(R.id.buttonFlagHarmonic)
                    Note.Flags.MUTED -> binding.toggleButton.check(R.id.buttonFlagMuted)
                }
                binding.textFret.text = adapter.getNote(it).toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}