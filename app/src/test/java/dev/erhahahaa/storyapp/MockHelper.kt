package dev.erhahahaa.storyapp

import org.mockito.Mockito.mock

inline fun <reified T> mockGeneric(): T = mock(T::class.java)
