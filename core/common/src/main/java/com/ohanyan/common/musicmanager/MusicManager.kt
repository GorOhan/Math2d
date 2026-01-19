package com.ohanyan.common.musicmanager

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.ohanyan.mathgame.common.R

object MusicManager : DefaultLifecycleObserver {

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false          // track if start() has run at least once
    private var isMusicEnabled = true       // track music preference

    // --- Public API ---------------------------------------------------------

    /**
     * Call once (e.g., from Composable) to register this manager to a Lifecycle.
     *
     * @param owner Any LifecycleOwner (Activity, Fragment, or Compose LocalLifecycleOwner).
     * @param context Needed only the *first* time to create the MediaPlayer.
     */
    fun register(owner: LifecycleOwner, context: Context) {
        owner.lifecycle.addObserver(this)

        // Lazily create MediaPlayer on the first registration
        if (!isPrepared) {
            prepare(context)
        }

        // If called on a Lifecycle already in RESUMED state, resume immediately
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            resume()
        }
    }

    /**
     * Unregister explicitly when you’re sure you no longer need background music
     * (optional; it will be done automatically in onDestroy).
     */
    fun unregister(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

    fun setMusicEnabled(enabled: Boolean) {
        isMusicEnabled = enabled
        if (enabled) {
            // Only resume if we are conceptually "resumed" or just want to start playing check?
            // Since we don't track lifecycle state explicitly here other than callbacks,
            // we rely on the fact this is likely called while app is in foreground.
            // But to be safe, we could check if we have a player that is prepared.
            // A better approach: update state, if we are supposed to be playing (lifecycle resumed), this acts immediately.
            // Problem: We don't know for sure if we are in a RESUMED state here easily without tracking it.
            // But usually settings are changed while app is in foreground.
             resume()
        } else {
            pause()
        }
    }

    // --- Lifecycle callbacks -----------------------------------------------

    override fun onPause(owner: LifecycleOwner) {
        pause()
    }

    override fun onResume(owner: LifecycleOwner) {
        resume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stop()            // full cleanup
        owner.lifecycle.removeObserver(this)
    }

    // --- Internal helpers ---------------------------------------------------

    private fun prepare(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.mathy_background).apply {
            isLooping = true
            if (isMusicEnabled) {
                start()
            }
        }
        isPrepared = true
    }

    private fun resume() {
        if (isMusicEnabled && mediaPlayer?.isPlaying == false) mediaPlayer?.start()
    }

    private fun pause() {
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.run {
            stop()
            release()
        }
        mediaPlayer = null
        isPrepared = false
    }

    fun checkPlayingState(play: Boolean) {
        if (play) {
            resume()
        } else {
            pause()
        }
    }

    val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying ?: false
}