package arrow.core

import kotlin.js.JsName

public sealed class Result<out A, out B> {

  /**
   * Returns `true` if this is a [Success], `false` otherwise.
   * Used only for performance instead of fold.
   */
  @JsName("_isSuccess")
  internal abstract val isSuccess: Boolean

  /**
   * Returns `true` if this is a [Failure], `false` otherwise.
   * Used only for performance instead of fold.
   */
  @JsName("_isFailure")
  internal abstract val isFailure: Boolean

  public fun isFailure(): Boolean = isFailure

  public fun isSuccess(): Boolean = isSuccess

  /**
   * Applies `ifFailure` if this is a [Failure] or `ifSuccess` if this is a [Success].
   *
   * Example:
   * ```kotlin
   * import arrow.core.*
   *
   * fun main() {
   *   fun possiblyFailingOperation(): Result.Success<Int> =
   *     Result.Success(1)
   *   //sampleStart
   *   val result: Result<Exception, Int> = possiblyFailingOperation()
   *   result.fold(
   *        { println("operation failed with $it") },
   *        { println("operation succeeded with $it") }
   *   )
   *   //sampleEnd
   * }
   * ```
   * <!--- KNIT example-either-34.kt -->
   *
   * @param ifFailure the function to apply if this is a [Failure]
   * @param ifSuccess the function to apply if this is a [Success]
   * @return the results of applying the function
   */
  public inline fun <C> fold(ifFailure: (A) -> C, ifSuccess: (B) -> C): C =
    when (this) {
      is Failure -> ifFailure(value)
      is Success -> ifSuccess(value)
    }

  /**
   * The given function is applied if this is a [Success].
   *
   * Example:
   * ```kotlin
   * import arrow.core.*
   *
   * fun main() {
   *   Result.Success(12).mapSuccess { "flower" } // Result: Success("flower")
   *   Result.Failure(12).mapSuccess { "flower" }  // Result: Failure(12)
   * }
   * ```
   * <!--- KNIT example-either-36.kt -->
   */
  public inline fun <C> mapSuccess(f: (B) -> C): Result<A, C> = fold({ Failure(it) }, { Success(f(it)) })

  /**
   * The given function is applied if this is a [Failure].
   *
   * Example:
   * ```kotlin
   * import arrow.core.*
   *
   * fun main() {
   *  Result.Success(12).mapFailure { "flower" } // Result: Success(12)
   *  Result.Failure(12).mapFailure { "flower" }  // Result: Failure("flower")
   * }
   * ```
   * <!--- KNIT example-either-37.kt -->
   */
  public inline fun <C> mapFailure(f: (A) -> C): Result<C, B> = fold({ Failure(f(it)) }, { Success(it) })

  /**
   * The given function is applied as a fire and forget effect
   * if this is a [Failure].
   * When applied the result is ignored and the original
   * Result value is returned
   *
   * Example:
   * ```kotlin
   * import arrow.core.*
   *
   * fun main() {
   *   Result.Success(12).tapFailure { println("flower") } // Result: Success(12)
   *   Result.Failure(12).tapFailure { println("flower") }  // Result: prints "flower" and returns: Failure(12)
   * }
   * ```
   * <!--- KNIT example-either-38.kt -->
   */
  public inline fun tapFailure(f: (A) -> Unit): Result<A, B> =
    when (this) {
      is Failure -> {
        f(this.value)
        this
      }
      is Success -> this
    }

  /**
   * The given function is applied as a fire and forget effect
   * if this is a [Success].
   * When applied the result is ignored and the original
   * Result value is returned
   *
   * Example:
   * ```kotlin
   *  import arrow.core.*
   *
   * fun main() {
   *   Result.Success(12).tapSuccess { println("flower") } // Result: prints "flower" and returns: Success(12)
   *   Result.Failure(12).tapSuccess { println("flower") }  // Result: Failure(12)
   * }
   * ```
   * <!--- KNIT example-either-39.kt -->
   */
  public inline fun tapSuccess(f: (B) -> Unit): Result<A, B> =
    when (this) {
      is Failure -> this
      is Success -> {
        f(this.value)
        this
      }
    }

  public data class Success<out B> constructor(val value: B) : Result<Nothing, B>() {
    override val isFailure = false
    override val isSuccess = true
  }

  public data class Failure<out A> constructor(val value: A) : Result<A, Nothing>() {
    override val isFailure = true
    override val isSuccess = false
  }

  override fun toString(): String = fold(
    { "Result.Failure($it)" },
    { "Result.Success($it)" }
  )
}

public inline fun <A, B, C> Result<A, B>.then(f: (B) -> Result<A, C>): Result<A, C> =
  when (this) {
    is Result.Failure -> this
    is Result.Success -> f(this.value)
  }

public fun <A> A.failure(): Result<A, Nothing> = Result.Failure(this)

public fun <A> A.success(): Result<Nothing, A> = Result.Success(this)
