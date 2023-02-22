package com.example.marerialtabeditor.ui.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.marerialtabeditor.adapters.SongAdapter
import com.example.marerialtabeditor.databinding.FragmentArchiveBinding
import com.example.marerialtabeditor.repository.data.ActionEnum

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!
    private val adapter = SongAdapter()
    private val viewModel: ArchiveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fab.setOnClickListener {
                // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                findNavController().navigate(
                    ArchiveFragmentDirections.actionArchiveFragmentToEditFragment(ActionEnum.ADD)
                )
            }
            recyclerSongs.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            recyclerSongs.adapter = adapter

            editSearch.addTextChangedListener {
                viewModel.searchQuery.value = it.toString()
            }
        }

        viewModel.apply {
            searchQuery.observe(viewLifecycleOwner) { query ->
                if (!songs.value.isNullOrEmpty()) {
                    adapter.setQuery(query)
                    viewModel.songs.value = viewModel.songs.value!!
                }
            }
            songs.observe(viewLifecycleOwner) { items ->
                if (!items.isNullOrEmpty()) {
                    val data = items.filter {
                        it.name.contains(viewModel.searchQuery.value!!, ignoreCase = true)
                                || it.band.contains(
                            viewModel.searchQuery.value!!,
                            ignoreCase = true
                        )
                    }
                    adapter.setData(data)
                }
            }
            tabs.observe(viewLifecycleOwner) { tabs ->
                viewModel.songs.value = tabs.map { it.song }
            }

        }

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}