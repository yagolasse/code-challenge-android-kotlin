package com.arctouch.codechallenge.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val api by inject<TmdbApi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        GlobalScope.launch(Dispatchers.IO) {
            val genreResult = api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
            Cache.cacheGenres(genreResult.genres)
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
        }
    }
}
