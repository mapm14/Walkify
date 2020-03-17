package manuelperera.walkify.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import manuelperera.walkify.presentation.R
import manuelperera.walkify.presentation.extensions.viewModel
import manuelperera.walkify.presentation.ui.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    override var activityLayout: Int = R.layout.activity_main

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = viewModel(viewModelFactory.get()) {

        }

        Toast.makeText(this, "Hello world!", Toast.LENGTH_LONG).show()
    }

}