package com.fmsz.sleeplogger.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fmsz.sleeplogger.databinding.FragmentAlarmBinding

/**
 * A simple [Fragment] subclass used to show information about the current alarm
 */
class AlarmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireActivity().application

        // inflate the layout using the generated data binding
        val binding = FragmentAlarmBinding.inflate(inflater)

        // set the lifecycle owner to allow for data binding
        binding.lifecycleOwner = this

        // create the ViewModel and set it up together with the binding
        val viewModel = ViewModelProvider(this, AlarmViewModelFactory(application)).get(AlarmViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

}
