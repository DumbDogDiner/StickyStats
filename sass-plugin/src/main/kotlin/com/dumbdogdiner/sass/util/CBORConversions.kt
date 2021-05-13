package com.dumbdogdiner.sass.util

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun JsonElement.toCbor(): ByteArray {
    // convert us to a matching Jackson value
    val node = JsonFactory(JsonMapper()).createParser(toString()).use { it.readValueAsTree<JsonNode> () }
    // serialize that value as CBOR
    val output = ByteArrayOutputStream()
    val gen = CBORFactory(CBORMapper()).createGenerator(output)
    gen.codec.writeTree(gen, node)
    // return encoded data
    return output.toByteArray()
}

fun ByteArray.fromCbor(): JsonElement {
    // decode to Jackson value
    val node = CBORFactory(CBORMapper()).createParser(ByteArrayInputStream(this)).use { it.readValueAsTree<JsonNode>() }
    // convert to JSON string, feed into Gson parser
    return JsonParser().parse(node.toString())
}