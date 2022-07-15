package arrow.core

public sealed class SuccessOrFailure<out A, out B> {

  //internal val either: Either<A,B> = TODO()

  public inline fun <A, B, C> then(f: (B) -> SuccessOrFailure<A, C>): SuccessOrFailure<A, C> {
    val either = when (this) {
      is Failure -> Either.Left(this.value)
      is Success -> Either.Right(this.value)
    }

    when(either) {
      is Either.Left -> TODO()
      is Either.Right -> either.flatMap {  }
    }
  }


  public data class Success<out B> constructor(val value: B) : SuccessOrFailure<Nothing, B>()

  public data class Failure<out A> constructor(val value: A) : SuccessOrFailure<A, Nothing>()
}
