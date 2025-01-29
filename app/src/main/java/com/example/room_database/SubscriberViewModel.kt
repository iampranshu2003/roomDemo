package com.example.room_database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.example.room_database.db.Subscriber
import com.example.room_database.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberViewModel(private val subscriberRepository: SubscriberRepository): ViewModel() {

    val subscribers = subscriberRepository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val deleteOrClearAllButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
    get() = statusMessage

    init {
        saveOrUpdateButtonText.value= "Save"
        deleteOrClearAllButtonText.value= "Clear All"

    }

    fun saveOrUpdate(){
        if (isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))
            inputName.value = ""
            inputEmail.value = ""

        }

    }
    fun clearAllOrDelete() = viewModelScope.launch(Dispatchers.IO) {
        if (isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            subscriberRepository.deleteAll()
            withContext(Dispatchers.Main){
                statusMessage.value = Event("Subscribers DeletedAll Successfully")

            }
        }

    }
    fun insert(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.insert(subscriber)
            withContext(Dispatchers.Main){
                statusMessage.value = Event("Subscriber Inserted Successfully")

            }
        }
    fun update(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO)  {
            subscriberRepository.update(subscriber)
        withContext(Dispatchers.Main){
            statusMessage.value = Event("Subscriber Updated Successfully")

        }
        }
    fun delete(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.delete(subscriber)
        withContext(Dispatchers.Main){
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            deleteOrClearAllButtonText.value = "Clear All"
            statusMessage.value = Event("Subscriber Deleted Successfully")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        deleteOrClearAllButtonText.value = "Delete"


    }


}