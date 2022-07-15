package arrow.core

public sealed class Result<out A, out B> {

  //internal val either: Either<A,B> = TODO()

  public data class Success<out B> constructor(val value: B) : Result<Nothing, B>()

  public data class Failure<out A> constructor(val value: A) : Result<A, Nothing>()
}

public inline fun <A, B, C> Result<A, B>.then(f: (B) -> Result<A, C>): Result<A, C> {

  val either = when (this) {
    is Result.Failure -> Either.Left(this.value)
    is Result.Success -> Either.Right(this.value)
  }

  val flatMap = either.flatMap { value: B ->
    f(value).let { result: Result<A, C> -> when() }
  }
  when(flatMap) {

  }

  return when (this) {
    is Result.Failure -> this
    is Result.Success -> f(this.value)
  }
}
