package com.nammamela.app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammamela.app.domain.model.Actor
import com.nammamela.app.domain.model.Play
import com.nammamela.app.domain.repository.AppRepository
import com.nammamela.app.util.AvatarPersistence
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ManageCastViewModel @Inject constructor(
    private val repository: AppRepository,
    @ApplicationContext private val appContext: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routePlayId: Int = savedStateHandle.get<Int>("playId") ?: 0

    private val _plays = MutableStateFlow<List<Play>>(emptyList())
    val plays: StateFlow<List<Play>> = _plays.asStateFlow()

    private val _selectedPlayId = MutableStateFlow<Int?>(null)
    val selectedPlayId: StateFlow<Int?> = _selectedPlayId.asStateFlow()

    private val _cast = MutableStateFlow<List<Actor>>(emptyList())
    val cast: StateFlow<List<Actor>> = _cast.asStateFlow()

    private val _pickedPhotoUri = MutableStateFlow<Uri?>(null)
    val pickedPhotoUri: StateFlow<Uri?> = _pickedPhotoUri.asStateFlow()

    private val _allActors = MutableStateFlow<List<Actor>>(emptyList())
    val allActors: StateFlow<List<Actor>> = _allActors.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private var castObserveJob: Job? = null

    /** Same name, role, and category (trimmed, case-insensitive) = one slot per play. */
    private fun isDuplicateInCurrentCast(
        name: String,
        role: String,
        category: String,
        excludeActorId: Int? = null
    ): Boolean {
        val n = name.trim().lowercase()
        val r = role.trim().lowercase()
        val c = category.trim().lowercase()
        return _cast.value.any { a ->
            (excludeActorId == null || a.id != excludeActorId) &&
                a.name.trim().lowercase() == n &&
                a.role.trim().lowercase() == r &&
                a.category.trim().lowercase() == c
        }
    }

    init {
        viewModelScope.launch {
            repository.getAllPlays().collect { list ->
                _plays.value = list.sortedByDescending { it.timestamp }
            }
        }
        viewModelScope.launch {
            repository.getAllActors().collect { list ->
                _allActors.value = list
            }
        }
        if (routePlayId > 0) {
            selectPlay(routePlayId)
        }
    }

    /** Performers from other plays; excludes anyone who already has an equivalent row on this play. */
    fun savedCastPoolForPicker(): List<Actor> {
        val pid = _selectedPlayId.value ?: return emptyList()
        return _allActors.value
            .filter { it.playId != pid }
            .filter { !isDuplicateInCurrentCast(it.name, it.role, it.category) }
    }

    fun cloneActorFromSaved(source: Actor) {
        val playId = _selectedPlayId.value ?: return
        if (source.playId == playId) return
        if (isDuplicateInCurrentCast(source.name, source.role, source.category)) {
            viewModelScope.launch {
                _errorEvent.emit("This performer is already in this play’s cast.")
            }
            return
        }
        viewModelScope.launch {
            val newId = repository.insertActor(
                Actor(
                    playId = playId,
                    name = source.name.trim(),
                    role = source.role.trim(),
                    category = source.category,
                    imageUrl = null
                )
            ).toInt()
            val srcPath = source.imageUrl?.takeIf { it.startsWith("/") && it.isNotBlank() }
            if (srcPath != null) {
                val newPath = withContext(Dispatchers.IO) {
                    AvatarPersistence.copyImageFromExistingFile(appContext, "actor_$newId", srcPath)
                }
                if (newPath != null) {
                    repository.updateActor(
                        Actor(
                            id = newId,
                            playId = playId,
                            name = source.name.trim(),
                            role = source.role.trim(),
                            category = source.category,
                            imageUrl = newPath
                        )
                    )
                }
            }
        }
    }

    private fun observeCastForPlay(playId: Int) {
        castObserveJob?.cancel()
        castObserveJob = viewModelScope.launch {
            repository.getActorsForPlay(playId).collect { actors ->
                _cast.value = actors
            }
        }
    }

    fun selectPlay(playId: Int) {
        if (playId <= 0) {
            _selectedPlayId.value = null
            _cast.value = emptyList()
            castObserveJob?.cancel()
            return
        }
        _selectedPlayId.value = playId
        observeCastForPlay(playId)
    }

    val effectivePlayId: Int
        get() = _selectedPlayId.value ?: 0

    fun onPhotoPicked(uri: Uri?) {
        _pickedPhotoUri.value = uri
    }

    fun clearPickedPhoto() {
        _pickedPhotoUri.value = null
    }

    fun addActor(name: String, role: String, category: String) {
        if (name.isBlank()) return
        val playId = _selectedPlayId.value ?: return
        if (isDuplicateInCurrentCast(name, role, category)) {
            viewModelScope.launch {
                _errorEvent.emit("This cast member is already listed for this play.")
            }
            return
        }
        viewModelScope.launch {
            val photoUri = _pickedPhotoUri.value
            val id = repository.insertActor(
                Actor(
                    playId = playId,
                    name = name.trim(),
                    role = role.trim(),
                    category = category,
                    imageUrl = null
                )
            ).toInt()
            photoUri?.let { uri ->
                val path = withContext(Dispatchers.IO) {
                    AvatarPersistence.copyImageToInternalFile(appContext, "actor_$id", uri)
                }
                if (path != null) {
                    repository.updateActor(
                        Actor(
                            id = id,
                            playId = playId,
                            name = name.trim(),
                            role = role.trim(),
                            category = category,
                            imageUrl = path
                        )
                    )
                }
            }
            _pickedPhotoUri.value = null
        }
    }

    fun updateActor(
        existing: Actor,
        name: String,
        role: String,
        category: String,
        newPhotoUri: Uri?
    ) {
        if (isDuplicateInCurrentCast(name, role, category, excludeActorId = existing.id)) {
            viewModelScope.launch {
                _errorEvent.emit("Another cast member already has this name, role, and category.")
            }
            return
        }
        viewModelScope.launch {
            var imagePath = existing.imageUrl
            if (newPhotoUri != null) {
                imagePath = withContext(Dispatchers.IO) {
                    AvatarPersistence.copyImageToInternalFile(
                        appContext,
                        "actor_${existing.id}",
                        newPhotoUri
                    )
                } ?: imagePath
            }
            repository.updateActor(
                existing.copy(
                    name = name.trim(),
                    role = role.trim(),
                    category = category,
                    imageUrl = imagePath
                )
            )
            _pickedPhotoUri.value = null
        }
    }

    fun deleteActor(actor: Actor) {
        viewModelScope.launch {
            repository.deleteActor(actor)
        }
    }
}
