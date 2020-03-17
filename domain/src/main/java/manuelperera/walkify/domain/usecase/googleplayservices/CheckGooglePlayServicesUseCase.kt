package manuelperera.walkify.domain.usecase.googleplayservices

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import manuelperera.walkify.domain.provider.GooglePlayServicesHandler
import manuelperera.walkify.domain.usecase.base.CompletableUseCase
import javax.inject.Inject

/**
 * Can return [manuelperera.walkify.domain.entity.base.Failure.ResolvableGooglePlayServicesError]
 * and [manuelperera.walkify.domain.entity.base.Failure.NoResolvableGooglePlayServicesError]
 */
class CheckGooglePlayServicesUseCase @Inject constructor(
    private val googlePlayServicesHandler: GooglePlayServicesHandler
) : CompletableUseCase<Unit> {

    override fun invoke(params: Unit): Completable =
        googlePlayServicesHandler
            .checkPlayServices()
            .subscribeOn(Schedulers.io())

}