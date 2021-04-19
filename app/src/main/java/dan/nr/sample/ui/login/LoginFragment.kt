package dan.nr.sample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dan.nr.sample.R
import dan.nr.sample.databinding.FragmentLoginBinding
import dan.nr.sample.repository.LoginRepository
import dan.nr.sample.ui.base.BaseFragment
import dan.nr.sample.util.InputValidator
import dan.nr.sample.util.isViewEnable

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginRepository>()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnter.isViewEnable(false)

        binding.edtUserName.addTextChangedListener {
            if (validateInputs()) binding.btnEnter.isViewEnable(true)
            else binding.btnEnter.isViewEnable(false)
        }
        binding.edtPassword.addTextChangedListener {
            if (validateInputs()) binding.btnEnter.isViewEnable(true)
            else binding.btnEnter.isViewEnable(false)
        }

        binding.btnEnter.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.saveUserInfo(binding.edtUserName.text.toString().trim(),
                                       binding.edtPassword.text.toString().trim())
                findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
            }
        }

    }

    fun validateInputs(): Boolean = InputValidator.validateInputs(binding.edtUserName.text.toString()
                                                                          .trim(),
                                                                  binding.edtPassword.text.toString()
                                                                          .trim())

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater,
                                    container: ViewGroup?) = FragmentLoginBinding.inflate(inflater,
                                                                                          container,
                                                                                          false)

    override fun getFragmentRepository(): LoginRepository = LoginRepository(userPreferences)

}