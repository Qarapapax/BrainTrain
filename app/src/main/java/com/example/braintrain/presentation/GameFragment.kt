package com.example.braintrain.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braintrain.R
import com.example.braintrain.databinding.FragmentGameBinding
import com.example.braintrain.domain.entity.GameResult
import com.example.braintrain.domain.entity.Level

class GameFragment : Fragment() {

    private val viewModelFactory by lazy {
        GameViewModelFactory(level, requireActivity().application)
    }

    private lateinit var level: Level
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            with(binding) {
                question.observe(viewLifecycleOwner) {
                    tvSum.text = it.sum.toString()
                    tvLeftNumber.text = it.visibleNumber.toString()
                    for (i in 0 until tvOptions.size) {
                        tvOptions[i].text = it.options[i].toString()
                    }
                }
                percentOfRightAnswers.observe(viewLifecycleOwner) {
                    progressBar.progress = it
                }
                enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
                    tvAnswersProgress.setTextColor(getColorByState(it))
                }
                enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                    val color = getColorByState(it)
                    progressBar.progressTintList = ColorStateList.valueOf(color)
                }
                formattedTime.observe(viewLifecycleOwner) {
                    tvTimer.text = it
                }
                minPercent.observe(viewLifecycleOwner) {
                    progressBar.secondaryProgress = it
                }
                gameResult.observe(viewLifecycleOwner) {
                    launchGameFinishedFragment(it)
                }
                progressAnswers.observe(viewLifecycleOwner) {
                    tvAnswersProgress.text = it
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        val args =
            Bundle().apply { putParcelable(GameFinishedFragment.KEY_GAME_RESULT, gameResult) }
        findNavController().navigate(R.id.action_gameFragment_to_gameFinishedFragment, args)
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }

    }

    companion object {
        const val NAME = "GameFragment"
        const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}