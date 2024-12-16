package dev.erhahahaa.storyapp.utils

// class ApiResponseSerializerTest {
//
//  private lateinit var serializer: ApiResponseSerializer<String>
//
//  @Before
//  fun setUp() {
//    serializer = ApiResponseSerializer(String.serializer())
//  }
//
//  @Test
//  fun `test descriptor is not null`() {
//    assertNotNull(serializer.descriptor)
//  }
//
//  @Test
//  fun `test serialization`() {
//    val apiResponse = ApiResponse(false, "Success", "data")
//    val json = Json.encodeToString(serializer, apiResponse)
//    assertEquals("""{"error":false,"message":"Success","data":"data"}""", json)
//  }
//
//  @Test
//  fun `test deserialize with error`() {
//    val json = """{"error":true,"message":"Error occurred"}"""
//    val apiResponse = Json.decodeFromString(serializer, json)
//    assertEquals(ApiResponse(true, "Error occurred", null), apiResponse)
//  }
//
//  @Test
//  fun `test deserialize with missing data`() {
//    val json = """{"error":false,"message":"Success"}"""
//    val apiResponse = Json.decodeFromString(serializer, json)
//    assertEquals(ApiResponse(false, "Success", null), apiResponse)
//  }
//
//  @Test
//  fun `test deserialize with unknown fields`() {
//    val json = """{"error":false,"message":"Success","data":"data","extra":"field"}"""
//    val apiResponse = Json.decodeFromString(serializer, json)
//    assertEquals(ApiResponse(false, "Success", "data"), apiResponse)
//  }
//
//  @Test
//  fun `test deserialization with missing data`() {
//    val json = """{"error":false,"message":"Success"}"""
//    val apiResponse = Json.decodeFromString(serializer, json)
//    assertEquals(ApiResponse(false, "Success", null), apiResponse)
//  }
// }
