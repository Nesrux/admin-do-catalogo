package com.nesrux.admin.catalogo.application.castmember.retrive.get;

import com.nesrux.admin.catalogo.application.UseCase;

public sealed abstract class GetCastMemberUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
