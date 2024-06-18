package com.example.skinalyze.ui.product

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skinalyze.adapter.SearchAdapter
import com.example.skinalyze.data.response.Product
import com.example.skinalyze.databinding.FragmentProductBinding
import com.example.skinalyze.viewmodel.ViewModelFactory
import com.example.skinalyze.DetailActivity

class  ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter

    private val viewModel by viewModels<ProductViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.productName = " "
        viewModel.findProduct(requireContext())

        viewModel.listProduct.observe(viewLifecycleOwner) { listProduct ->
            setProductsData(listProduct)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                viewModel.productName = searchView.text.toString()
                viewModel.findProduct(requireContext())
                searchView.hide()
                false
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvProduct.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvProduct.addItemDecoration(itemDecoration)
        adapter = SearchAdapter()
        binding.rvProduct.adapter = adapter

        return root
    }

    private fun setProductsData(products: List<Product>) {
        adapter.submitList(products)
        adapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                val moveToDetailUserIntent =
                    Intent(requireContext(), DetailActivity::class.java)
                Log.d("DEBUG Product Fragment", id)
                moveToDetailUserIntent.putExtra(DetailActivity.ID, id)
                startActivity(moveToDetailUserIntent)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}