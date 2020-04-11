package com.fmsz.sleeplogger.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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

        // add a click listener to the alarm on/off switch
        // (using a onCheckedChangedListener is not good since the switch is connected to
        // viewModel.alarmEnabled, causing a lot of confusing updates when it changes (and the callback fires)
        binding.alarmEnabledSwitch.setOnClickListener { view ->
            viewModel.setAlarmEnabled((view as Switch).isChecked)
        }

        // add click listener to the label displaying the current time
        binding.alarmTime.setOnClickListener {
            AlarmTimePickerFragment(AlarmTime.fromMillis(viewModel.alarmTime.value!!)) { newTime ->
                viewModel.onSetAlarmTime(newTime)
            }.show(parentFragmentManager, "timePicker")
        }

        return binding.root
    }
}
