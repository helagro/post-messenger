package se.helagro.postmessenger.settings


enum class PreferenceInfo(val id: String, val defaultVal: Any) {
    ENDPOINT("endpoint_preference", ""),
    JSON_KEY("json_key_preference", "message"),
}