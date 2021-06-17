package com.example.Mongo.DataBase.Encryption.Client.Side

import com.mongodb.ClientEncryptionSettings
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoNamespace
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.vault.DataKeyOptions
import com.mongodb.client.model.vault.EncryptOptions
import com.mongodb.client.vault.ClientEncryptions
import org.bson.BsonBinary
import org.bson.BsonString
import org.bson.Document
import org.bson.types.Binary
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.SecureRandom


@SpringBootApplication
class MongoDataBaseEncryptionClientSideApplication

  ;

fun main(args: Array<String>) {


	// This would have to be the same master key as was used to create the encryption key
	val localMasterKey = ByteArray(96)
	SecureRandom().nextBytes(localMasterKey)


	val keyMap: MutableMap<String, Any> = HashMap()
	keyMap["key"] = localMasterKey

	val kmsProviders: MutableMap<String, Map<String, Any>> = HashMap()
	kmsProviders["local"] = keyMap


	val clientSettings = MongoClientSettings.builder().build()
	val mongoClient = MongoClients.create(clientSettings)



// Set up the key vault for this example
	val keyVaultNamespace = MongoNamespace("encryption.testKeyVault")
	val keyVaultCollection: MongoCollection<Document> = mongoClient
		.getDatabase(keyVaultNamespace.databaseName)
		.getCollection(keyVaultNamespace.collectionName)
	keyVaultCollection.drop()



// Ensure that two data keys cannot share the same keyAltName.
	keyVaultCollection.createIndex(
		Indexes.ascending("keyAltNames"),
		IndexOptions().unique(true)
			.partialFilterExpression(Filters.exists("keyAltNames"))
	)

	val collection: MongoCollection<Document> = mongoClient.getDatabase("test").getCollection("coll")
	collection.drop() // Clear old data




// Create the ClientEncryption instance
	val clientEncryptionSettings = ClientEncryptionSettings.builder()
		.keyVaultMongoClientSettings(
			MongoClientSettings.builder()
				.applyConnectionString(ConnectionString("mongodb://localhost"))
				.build()
		)
		.keyVaultNamespace(keyVaultNamespace.fullName)
		.kmsProviders(kmsProviders)
		.build()

	val clientEncryption = ClientEncryptions.create(clientEncryptionSettings)

	//Get data key
	val dataKeyId = clientEncryption.createDataKey("local", DataKeyOptions())

// Explicitly encrypt a field

// Explicitly encrypt a field
	val encryptedFieldValue = clientEncryption.encrypt(
		BsonString("123456789"),
		EncryptOptions("AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic").keyId(dataKeyId)
	)

	collection.insertOne(Document("encryptedField", encryptedFieldValue))

	val doc: Document? = collection.find().first()
	System.out.println(doc?.toJson())



//Explicitly decrypt the field
	// Explicitly decrypt the field
	// Explicitly decrypt the field
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	println(
		clientEncryption.decrypt(BsonBinary(doc!!.get("encryptedField", Binary::class.java).data))
	)
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");




	runApplication<MongoDataBaseEncryptionClientSideApplication>(*args)
}
