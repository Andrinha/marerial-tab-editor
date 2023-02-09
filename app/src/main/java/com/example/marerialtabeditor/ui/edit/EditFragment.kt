package com.example.marerialtabeditor.ui.edit

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.io.IOException


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val adapter = NoteAdapter()
    private val viewModel: EditViewModel by viewModels()
    private val soundPool: SoundPool by lazy {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(10)
            .build()
    }
    private val assetManager: AssetManager by lazy {
        requireActivity().assets
    }
    private var streamID = 0

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
            recyclerSong.setPadding(0,0,0,0)
            recyclerSong.clipToPadding = false
            recyclerSong.clipChildren = false
            recyclerSong.addItemDecoration(object: ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    //super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(0,0,0,0)
                }
            })
            buttonAddBlock.setOnClickListener {
                val data = viewModel.song.value!!
                data.addEmptyChunk()
                viewModel.song.value = data
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
                }
            }
            buttonFretDesc.setOnClickListener {
                if (viewModel.focus.value != null) {
                    val focus = viewModel.focus.value!!
                    val note = adapter.getNote(focus)
                    note.fret--
                    viewModel.setNote(focus, note)
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
        }
        viewModel.apply {
            song.observe(viewLifecycleOwner) {
                adapter.setData(it.notes)
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

    private fun loadSound(fileName: String): Int {
        val afd: AssetFileDescriptor = try {
            assetManager.openFd(fileName)
        } catch (e: IOException) {
            return -1
        }
        return soundPool.load(afd, 1)
    }

    private fun playSound(sound: Int): Int {
        if (sound > 0) {
            streamID = soundPool.play(sound, 1F, 1F, 1, 0, 1F)
        }
        return streamID
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