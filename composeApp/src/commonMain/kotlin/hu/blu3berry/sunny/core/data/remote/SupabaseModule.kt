package hu.blu3berry.sunny.core.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for Supabase-related dependencies.
 * Provides a configured SupabaseClient instance.
 */
@OptIn(SupabaseInternal::class)
val supabaseModule = module {
    single {
        // Use constants from Supabase.kt
        // In a production app, these could be overridden with values from secure storage
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY,
        ) {
            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )

            // Install Postgrest so client.from(...) is available at runtime
            install(Postgrest)
            // 👇 Add Ktor logging
        }

    } bind SupabaseClient::class
}
