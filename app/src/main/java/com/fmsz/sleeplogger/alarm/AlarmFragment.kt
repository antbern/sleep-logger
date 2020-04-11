package com.fmsz.sleeplogger.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.fmsz.sleeplogger.databinding.FragmentAlarmBinding

/**
 * A simple [Fragment] subclass used to show information about the current alarm
 */
class AlarmFragment : Fragment() {

    private val viewModel by activityViewModels<AlarmViewModel> {
        AlarmViewModelFactory(
            requireActivity().application,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate the layout using the generated data binding
        val binding = FragmentAlarmBinding.inflate(inflater)

        // set the lifecycle owner to allow for correct data binding
        binding.lifecycleOwner = this

        // set the viewModel binding variable to be used within the layout
        binding.viewModel = viewModel

        // add a CheckedChangeListener listener to the alarm on/off switch
        binding.alarmEnabled.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAlarmEnabled(isChecked)
        }

        // add click listener to the label displaying the current time
        binding.alarmTime.setOnClickListener {
            AlarmTimePickerFragment(viewModel.alarmTime.value) { newTime ->
                viewModel.onSetAlarmTime(newTime)
            }.show(parentFragmentManager, "timePicker")
        }

        viewModel.alarmTime.observe(viewLifecycleOwner, Observer { newTime ->
            // calculate difference from now
            val diff = (newTime - System.currentTimeMillis()) / 1000
            val minutes = (diff / 60) % 60
            val hours = (diff / 60) / 60


            Toast.makeText(
                context,
                "Alarm goes off in $hours hours and $minutes minutes",
                Toast.LENGTH_SHORT
            ).show()
        })

        return binding.root
    }
}
