package com.example.room_database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase: RoomDatabase()  {

    abstract val subscriberDAO: SubscriberDAO

//we will be creating only one instance of database class so we will use singleton pattern
    companion object{
        //this annotation make it visible to all threads
        @Volatile
        private var INSTANCE : SubscriberDatabase? = null
        fun getInstance(context: Context):SubscriberDatabase{
            synchronized(this){
                var instance:SubscriberDatabase? = INSTANCE
                if (instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"

                    ).build()
                    INSTANCE = instance

                }
                return instance

            }

        }
    }

}