package com.example.Mongo.DataBase.Encryption.Client.Side

import com.mongodb.ClientEncryptionSettings
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.model.vault.DataKeyOptions
import com.mongodb.client.vault.ClientEncryptions
import java.util.*


class KMSHandler {
/*
    fun generateAndSaveDek():Unit{
        val connectionString = "mongodb://localhost:27017"
        val keyVaultNamespace = "encryption.__keyVault"
//Client Encryption Settings...
//Client Encryption Settings...
        val clientEncryptionSettings = ClientEncryptionSettings.builder()
            .keyVaultMongoClientSettings(
                MongoClientSettings.builder()
                    .applyConnectionString(ConnectionString(connectionString))
                    .build()
            )
            .keyVaultNamespace(keyVaultNamespace)
            .kmsProviders(kmsProviders)
            .build()
//ClientEncryption Object
//ClientEncryption Object
        val clientEncryption = ClientEncryptions.create(clientEncryptionSettings)
        val dataKeyId = clientEncryption.createDataKey(kmsProvider, DataKeyOptions())
        println("DataKeyId [UUID]: " + dataKeyId.asUuid())

        val base64DataKeyId: String = Base64.getEncoder().encodeToString(dataKeyId.data)
        println("DataKeyId [base64]: $base64DataKeyId")
    }

 */
}