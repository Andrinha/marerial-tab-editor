package com.example.marerialtabeditor.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marerialtabeditor.databinding.ActivityAboutBinding


class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.webView.loadUrl("file:///android_asset/about/help.html");
    }
}