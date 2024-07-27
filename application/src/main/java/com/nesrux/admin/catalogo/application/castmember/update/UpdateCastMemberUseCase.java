package com.nesrux.admin.catalogo.application.castmember.update;

import com.nesrux.admin.catalogo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
    extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
permits  DefaultUpdateCastMemberUsecase{
}
