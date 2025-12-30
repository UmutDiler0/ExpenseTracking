package com.expense.expensetracking.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.expense.expensetracking.AppDataBase
import com.expense.expensetracking.data.local_repo.UserDao
import com.expense.expensetracking.data.manager.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class) // Bu modül tüm uygulama boyunca yaşar
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "user_database" // Veritabanı dosya adı
        )
            .fallbackToDestructiveMigration() // Versiyon yükseltmelerinde hata almamak için (opsiyonel)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDataBase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}