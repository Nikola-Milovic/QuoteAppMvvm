package com.example.quoteappmvvm.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteappmvvm.R
import com.example.quoteappmvvm.databinding.FavoriteQuotesFragmentBinding
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoriteQuotesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoriteQuotesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FavoriteQuotesFragmentBinding

    private lateinit var favoriteQuotesAdapter: FavoriteQuotesAdapter





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteQuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.setLifecycleOwner(this)


        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {
                    Log.d("TAG", "Move")
                    return true// true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    lifecycleScope.launch {
                        val pos = viewHolder.adapterPosition
                        val quote = favoriteQuotesAdapter.getQuoteAt(pos)
                        viewModel.deleteFavoriteQuote(quote.id)
                        favoriteQuotesAdapter.removeQuote(pos)
                    }

                }
            })


        favoriteQuotesAdapter = FavoriteQuotesAdapter(requireContext())
        viewDataBinding.quotesList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteQuotesAdapter
            mIth.attachToRecyclerView(this)
            this.addItemDecoration(
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
        }


        viewModel.quoteList.observe(this) {
            //favoriteQuotesAdapter.quoteList = it
            favoriteQuotesAdapter.updateQuotes(it)
            Log.d("TAG", "IT IS $it")
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_All_Quotes_Item){
            lifecycleScope.launch{
                viewModel.deleteAllFavoriteQuotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
