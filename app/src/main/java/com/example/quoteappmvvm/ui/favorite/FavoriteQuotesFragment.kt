package com.example.quoteappmvvm.ui.favorite

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteappmvvm.R
import com.example.quoteappmvvm.data.model.Quote
import com.example.quoteappmvvm.databinding.FavoriteQuotesFragmentBinding
import com.example.quoteappmvvm.ui.util.OnQuoteClickListener
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoriteQuotesFragment : DaggerFragment(), OnQuoteClickListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoriteQuotesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FavoriteQuotesFragmentBinding

    private lateinit var favoriteQuotesAdapter: FavoriteQuotesAdapter

    private lateinit var currentQuote: Quote

    companion object {
        private const val sharedPreferencesFile = "com.example.quoteappmvvm.preferences"
        private const val IS_FIRST_RUN_QUOTES = "isFirstRunQuotes"
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteQuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this


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



        favoriteQuotesAdapter = FavoriteQuotesAdapter(requireContext(), this)
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
            favoriteQuotesAdapter.updateQuotes(it)
        }

        viewDataBinding.deleteAllQuotes.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.grow_anim)
            it.startAnimation(anim)
            deleteAllQuote()
        }
        return viewDataBinding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun deleteAllQuote() {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to delete ALL favorite quotes?")
        builder.setPositiveButton("YES") { dialog, which ->
            lifecycleScope.launch {
                viewModel.deleteAllFavoriteQuotes()
                favoriteQuotesAdapter.deleteAll()
            }
            Toast.makeText(context, "Successfully cleared all favorite quotes", Toast.LENGTH_SHORT)
                .show()

        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()


    }

    override fun quoteClicked(quote: Quote, view: View) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.longclick_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        currentQuote = quote
        popup.show()
    }

    fun copyQuote() {
        val clipBoard =
            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager // THIS SHOULD PROBABLY BE INJECTED WITH DAGGER AND CONTEXT SHOULD BE PROVIDED WITH DAGGER, BUT DAGGER IS KINDA WEIRD AND I DONT LIKE IT
        viewModel.copyText(currentQuote, clipBoard)
        Toast.makeText(context, "Quote successfully copied", Toast.LENGTH_SHORT).show()
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        if (p0?.itemId == R.id.copy_item) {
            copyQuote()
        }
        return true
    }

}
