package ch.ubique.libs.kmpanion.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class RunSuspendCatchingTest {

	@Test
	fun `Successful block inside runSuspendCatching should return result`() = runTest {
		val result = runSuspendCatching { longRunningTask() }

		assertTrue(result.isSuccess)
		assertEquals("result", result.getOrThrow())
	}

	@Test
	fun `Exception thrown inside runSuspendCatching should be caught`() = runTest {
		val result = runSuspendCatching { longRunningTaskWithError() }

		assertTrue(result.isFailure)
		assertIs<IllegalStateException>(result.exceptionOrNull())
	}

	@Test
	fun `Cancelling block inside runSuspendCatching should be propagated`() = runTest {
		val job = launch {
			runSuspendCatching { longRunningTask() }
			fail("Cancellation should have been propagated")
		}
		delay(500.milliseconds)
		job.cancel()
	}

	@Test
	fun `Cancelling parent job should be propagated`() = runTest {
		val parent = launch {
			launch {
				runSuspendCatching { longRunningTask()
				fail("Cancellation should have been propagated") }
			}
		}
		delay(500.milliseconds)
		parent.cancel()
	}

	private suspend fun longRunningTask(): String {
		delay(2.seconds)
		return "result"
	}

	private suspend fun longRunningTaskWithError(): String {
		delay(2.seconds)
		throw IllegalStateException("Exception")
	}

}