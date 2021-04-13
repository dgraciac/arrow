package arrow.optics.combinators

import arrow.optics.AffineFoldK
import arrow.optics.AffineTraversalK
import arrow.optics.FoldK
import arrow.optics.Getter
import arrow.optics.IxGetter
import arrow.optics.IxLens
import arrow.optics.Lens
import arrow.optics.Optic
import arrow.optics.TraversalK
import arrow.optics.get
import arrow.optics.ixGet
import arrow.optics.ixLens
import arrow.optics.lens
import arrow.optics.set
import arrow.optics.viewOrNull

@JvmName("at_affineTraversal")
fun <K: AffineTraversalK, I, S, A> Optic<K, I, S, S, A, A>.at(
  ind: I
): Lens<S, A?> =
  Optic.lens({ s ->
    s.viewOrNull(this@at.index(ind))
  }, { s, a: A? ->
    a?.let { s.set(this@at.index(ind), it) } ?: s
  })

@JvmName("at_affineFold")
fun <K: AffineFoldK, I, S, A> Optic<K, I, S, S, A, A>.at(
  ind: I
): Getter<S, A?> =
  Optic.get { s -> s.viewOrNull(this@at.index(ind)) }

// Unsafe if the index is not unique in th traversal
@JvmName("at_traversal")
fun <K: TraversalK, I, S, A> Optic<K, I, S, S, A, A>.at(
  ind: I
): IxLens<I, S, A?> =
  Optic.ixLens({ s ->
    ind to s.viewOrNull(this@at.uIndex(ind))
  }, { s, a: A? ->
    a?.let { s.set(this@at.uIndex(ind), it) } ?: s
  })

// Unsafe if the index is not unique in th traversal
@JvmName("at_fold")
fun <K: FoldK, I, S, A> Optic<K, I, S, S, A, A>.at(
  ind: I
): IxGetter<I, S, A?> =
  Optic.ixGet { s -> ind to s.viewOrNull(this@at.uIndex(ind)) }
