package com.znggis.githubusersapp.ui

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(this.context)


/**
 * source: https://github.com/Ezike/Baking-App-Kotlin/blob/master/core/src/main/java/com/example/eziketobenna/bakingapp/core/viewBinding/ViewBinding.kt
 */

/**
 * A lazy property that gets cleaned up when the fragment's view is destroyed.
 * Accessing this variable while the fragment's view is destroyed will throw an [IllegalStateException].
 */
class ViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

    init {
        fragment.lifecycle.addObserver(this)
    }

    private var _value: T? = null

    private val viewLifecycleObserver: DefaultLifecycleObserver =
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                disposeValue()
            }
        }

    private fun disposeValue() {
        _value = null
    }

    override fun onCreate(owner: LifecycleOwner) {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.addObserver(viewLifecycleObserver)
        }
    }


    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding: T? = _value
        if (binding != null) {
            return binding
        }

        val lifecycle: Lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when " +
                        "Fragment views are destroyed."
            )
        }

        return viewBindingFactory(thisRef.requireView()).also {
            _value = it
        }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T): ViewBindingDelegate<T> =
    ViewBindingDelegate(
        fragment = this,
        viewBindingFactory = viewBindingFactory
    )


/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(it, timeLength)
        }
    })
}


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        val view: View = this.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = 500
        view.layoutParams = params
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            }
        })
        show()
    }
}


fun ViewGroup.inflate(layout: Int): View {
    val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return layoutInflater.inflate(layout, this, false)
}