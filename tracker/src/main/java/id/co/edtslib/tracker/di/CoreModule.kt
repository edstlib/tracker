package id.co.edtslib.tracker.di

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.securepreferences.SecurePreferences
import id.co.edtslib.tracker.Tracker

val networkingModule = module {
    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideGsonConverterFactory(get()) }

    single(named("tracker")) { provideRetrofit(get(), get()) }
}

val sharedPreferencesModule = module {
    single(named("trackerSharePref")) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .build()
            val masterKey = MasterKey.Builder(get())
                .setKeyGenParameterSpec(spec)
                .build()

            EncryptedSharedPreferences.create(
                get(),
                "edts_tracker_secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            SecurePreferences(get())
        }
    }
}

private fun provideOkHttpClient(): OkHttpClient = UnsafeOkHttpClient().get()

private fun provideGson(): Gson = Gson()

private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Tracker.baseUrl)
        .client(okHttpClient.newBuilder().addInterceptor(AuthInterceptor(Tracker.token)).build())
        .addConverterFactory(converterFactory)
        .build()
}