package com.example.quoteappmvvm.ui.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteQuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.setLifecycleOwner(this)



        viewModel.quoteList.observe(this) {
            viewDataBinding.quotesList.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = FavoriteQuotesAdapter(it)
                this.addItemDecoration(
                    DividerItemDecoration(
                        context!!,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }


        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    // viewModel.deleteFavoriteQuote()
                }
            })
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
