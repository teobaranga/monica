package com.teobaranga.monica

import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

private const val sdk = 36

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [sdk])
abstract class RobolectricTest

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [sdk])
abstract class RobolectricParameterizedTest
