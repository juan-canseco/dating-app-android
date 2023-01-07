package com.org.datingapp.features.onboarding.birthdate

import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase
import com.org.datingapp.core.domain.user.details.BirthDate
import javax.inject.Inject

class  ComputeAge @Inject constructor(private val validator : AgeValidator,
                                     private val calculator : AgeCalculator) : UseCase<ComputeAge.Result, ComputeAge.Params>() {

    override suspend fun run(params: Params): Either<Failure, Result> {
        return try {
            val age = calculator.getAge(params.birthDate)
            if (validator.isValid(age))
                Either.Right(Result(params.birthDate, age))
            else
                Either.Left(Failures.InvalidAge())
        }
        catch (exception : Exception) {
            exception.printStackTrace()
            Either.Left(Failures.InvalidAge())
        }
    }

    data class Params(val birthDate : BirthDate)
    data class Result(val birthDate : BirthDate, val age : Int)

    sealed class Failures {
        class InvalidAge : Failure.FeatureFailure()
    }

}