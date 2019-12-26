package com.nikolam.simplyquotes.ui.favorite

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolam.simplyquotes.R
import com.nikolam.simplyquotes.data.model.Quote
import com.nikolam.simplyquotes.databinding.FavoriteQuotesFragmentBinding
import com.nikolam.simplyquotes.ui.util.OnQuoteClickListener
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class FavoriteQuotesFragment : DaggerFragment(), OnQuoteClickListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoriteQuotesViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FavoriteQuotesFragmentBinding

    private lateinit var favoriteQuotesAdapter: FavoriteQuotesAdapter

    private lateinit var currentQuote: Quote

    // Used for storing whether user is first time running the app or not
    companion object {
        private const val sharedPreferencesFile = "com.nikolam.simplyquotes.preferences"
        private const val IS_FIRST_RUN_FAVORITE = "isFirstRunFavorite"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private var firstRun = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteQuotesFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        viewDataBinding.lifecycleOwner = this

        // Used to swipe delete Recycle View Items, i.e. unfavorite a quote by swiping it
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT
            ) {
                private val deleteIcon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_white)
                private val intrinsicWidth = deleteIcon!!.intrinsicWidth
                private val intrinsicHeight = deleteIcon!!.intrinsicHeight
                private val background = ColorDrawable()
                private val backgroundColor = Color.parseColor("#f44336")
                private val clearPaint =
                    Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }




                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {
                    Log.d("TAG", "Move")
                    return true// true if moved, false otherwise
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {


                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top
                    val isCanceled = dX == 0f && !isCurrentlyActive

                    if (isCanceled) {
                        clearCanvas(
                            c,
                            itemView.right + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                        return
                    }

                    // Draw the red delete background
                    background.color = backgroundColor
                    background.setBounds(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.left,
                        itemView.bottom
                    )
                    // background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    background.draw(c)

                    // Calculate position of delete icon
                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.left + deleteIconMargin
                    val deleteIconRight = itemView.left + deleteIconMargin + intrinsicWidth
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    // Draw the delete icon
                    deleteIcon?.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                    deleteIcon?.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

                private fun clearCanvas(
                    c: Canvas?,
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float
                ) {
                    c?.drawRect(left, top, right, bottom, clearPaint)
                }



                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    lifecycleScope.launch {
                        val pos = viewHolder.adapterPosition
                        val quote = favoriteQuotesAdapter.getQuoteAt(pos)
                        viewModel.deleteFavoriteQuote(quote.id, quote)
                        favoriteQuotesAdapter.removeQuote(pos)
                    }
                }
            })

        favoriteQuotesAdapter = FavoriteQuotesAdapter(requireContext(), this)
        viewDataBinding.quotesList.apply {
            layoutManager = LinearLayoutManager(activity).apply {
                isSmoothScrollbarEnabled = true
            }
            adapter = favoriteQuotesAdapter
            mIth.attachToRecyclerView(this)
            this.addItemDecoration( // Set up the lines below the quotes, just for looks
                DividerItemDecoration(
                    context!!,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        //viewModel.checkForEmpty()

        viewModel.favoritequoteList.observe(this) {
            favoriteQuotesAdapter.updateQuotes(it)
        }

        viewModel.favoriteQuotesEmptyCheck.observe(this) {
            if (it) {
                viewDataBinding.textViewNoFavoriteQuotes.visibility = View.VISIBLE
            } else {
                viewDataBinding.textViewNoFavoriteQuotes.visibility = View.INVISIBLE
            }
        }

        deleteAllButtonSetup()

        enableNavigation() // To avoid unforeseen situations where somehow the enableNavigation wasn't called before the fragment has been closed

        if (firstRun) firstRun() // If it's the users first time running the app, show him Text Balloons to explain what everything does

        return viewDataBinding.root
    }

    fun deleteAllButtonSetup() {
        viewDataBinding.deleteAllQuotes.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(context, R.anim.grow_anim)
            it.startAnimation(anim)
            deleteAllQuote()
        }

        val btt = AnimationUtils.loadAnimation(context, R.anim.btt)
        viewDataBinding.deleteAllQuotes.startAnimation(btt)
    }


    // Checks whether it's users first time running THIS fragment, as other fragments have different first time checks, so you truly get first time experience on each fragment.
    // This avoids that user might exit the app while opening a certain fragment
    fun checkFirstRun() {
        sharedPreferences =
            requireActivity().getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(IS_FIRST_RUN_FAVORITE, true)) {
            firstRun = true
            with(sharedPreferences.edit()) {
                putBoolean(IS_FIRST_RUN_FAVORITE, false)
                apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkFirstRun()
    }


    private fun firstRun() { // Show user first time Balloons to help him user the app easier.   Thanks to https://github.com/skydoves/Balloon    -  NEED A BETTER WAY TO HANDLE SHOWING BALLOONS as I don't like this code, looks boiler plate-y
        lifecycleScope.launch {
            favoriteQuotesAdapter.quoteList.add(Quote("Sample quote", "The dev guy", true, 13101))
            disableNavigation()
            delay(1000)


            val balloon3 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.4f)
                setHeight(50)
                setArrowPosition(0.5f)
                setArrowVisible(false)
                setArrowOrientation(ArrowOrientation.TOP)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setBalloonAnimationStyle(R.style.LeftToRight)
                setOnBalloonDismissListener {
                    enableNavigation()
                }
                setText("Or swipe to delete it!")
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@FavoriteQuotesFragment)
            }


            val balloon2 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.8f)
                setHeight(50)
                setArrowPosition(0.5f)
                setArrowOrientation(ArrowOrientation.TOP)
                setArrowVisible(false)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setOnBalloonDismissListener {
                    if (this@FavoriteQuotesFragment.isVisible) {
                        balloon3.showAlignTop(viewDataBinding.quotesList, 0, 350)
                    }
                }
                setText("You can click on a favorite quote to copy it")
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@FavoriteQuotesFragment)
            }

            val balloon1 = createBalloon(requireContext()) {
                setArrowSize(10)
                setWidthRatio(0.7f)
                setHeight(50)
                setArrowPosition(0.9f)
                setArrowOrientation(ArrowOrientation.TOP)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setDismissWhenClicked(true)
                setDismissWhenTouchOutside(true)
                setOnBalloonDismissListener {
                    if (this@FavoriteQuotesFragment.isVisible) {
                        balloon2.showAlignTop(viewDataBinding.quotesList, 0, 400)
                    }
                }
                setText("You can delete all favorite quotes by clicking here!")
                setTextColorResource(R.color.colorAccent)
                setBackgroundColorResource(R.color.colorPrimary)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@FavoriteQuotesFragment)
            }

            balloon1.showAlignBottom(viewDataBinding.deleteAllQuotes)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun disableNavigation() { // Disable navigation while first time User is shown Balloons, avoids going somewhere else while Balloons haven't finished yet
        lifecycleScope.launch {
            val view = requireActivity().findViewById<View>(R.id.favoriteQuotesFragment)
            val view2 = requireActivity().findViewById<View>(R.id.settingsFragment)
            view.isClickable = false
            view2.isClickable = false
        }
    }

    fun enableNavigation() {
        lifecycleScope.launch {
            val view = requireActivity().findViewById<View>(R.id.favoriteQuotesFragment)
            val view2 = requireActivity().findViewById<View>(R.id.settingsFragment)
            view.isClickable = true
            view2.isClickable = true
        }
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
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        // Make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

    override fun quoteClicked(quote: Quote, view: View) { // Show a PopUp Menu
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.longclick_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        currentQuote = quote
        popup.show()
    }

    fun copyQuote() { // Copy a quote to ClipBoard
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

