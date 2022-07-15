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

  public data class Success<out B> constructor(val value: B) : Result<Nothing, B>() {
    override val isFailure = false
    override val isSuccess = true
  }

  public data class Failure<out A> constructor(val value: A) : Result<A, Nothing>() {
    override val isFailure = true
    override val isSuccess = false
  }
}

public inline fun <A, B, C> Result<A, B>.then(f: (B) -> Result<A, C>): Result<A, C> =
  when (this) {
    is Result.Failure -> this
    is Result.Success -> f(this.value)
  }
