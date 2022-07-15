package arrow.core

public sealed class Result<out A, out B> {

  //internal val either: Either<A,B> = TODO()

  public inline fun <A, B, C> then(f: (B) -> Result<A, C>): Result<A, C> {
    val either = when (this) {
      is Failure -> Either.Left(this.value)
      is Success -> Either.Right(this.value)
    }

    when(either) {
      is Either.Left -> TODO()
      is Either.Right -> TODO()
    }
  }


  public data class Success<out B> constructor(val value: B) : Result<Nothing, B>()

  public data class Failure<out A> constructor(val value: A) : Result<A, Nothing>()
}
