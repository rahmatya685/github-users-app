package com.znggis.githubusersapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.navArgs
import com.znggis.githubusersapp.R
import com.znggis.githubusersapp.databinding.FragmentUserItemDetailBinding
import com.znggis.githubusersapp.databinding.UserItemsFragmentBinding
import com.znggis.githubusersapp.ui.image.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class UserItemDetailFragment : Fragment(R.layout.fragment_user_item_detail) {


    val args: UserItemDetailFragmentArgs by navArgs()

    @Inject
    lateinit var imageLoader: ImageLoader


    private val binding: FragmentUserItemDetailBinding by viewBinding(
        FragmentUserItemDetailBinding::bind
    )


    private val viewModel: UserItemsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSelectedItem(args.item)
        loadData()
        loadImage()

    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun loadData() {
        viewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.userItem = item
                binding.btnLike.setOnClickListener {
                    viewModel.toggleItemFavourite(item)
                }
            }
        }
    }

    private fun loadImage() {
        imageLoader.loadImage(binding.imgUser, args.item.avatarUrl)
    }

}