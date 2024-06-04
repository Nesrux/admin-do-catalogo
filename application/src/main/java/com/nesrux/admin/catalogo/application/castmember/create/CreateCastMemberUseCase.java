package com.nesrux.admin.catalogo.application.castmember.create;

import com.nesrux.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
