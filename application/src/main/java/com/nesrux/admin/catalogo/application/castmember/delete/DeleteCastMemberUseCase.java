package com.nesrux.admin.catalogo.application.castmember.delete;

import com.nesrux.admin.catalogo.application.UnitUseCase;

public sealed  abstract class DeleteCastMemberUseCase
extends UnitUseCase<String>
permits  DefaultDeleteCastMemberUseCase{
}
