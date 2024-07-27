package com.nesrux.admin.catalogo.infrastructure.configuration.usecase;

import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.DefaultGetCastMemberByIdUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.GetCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.list.DefaultListCastMembersUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.list.ListCastMembersUseCase;
import com.nesrux.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUsecase;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway gateway;

    public CastMemberUseCaseConfig(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(gateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUsecase(gateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(gateway);
    }

    @Bean
    public GetCastMemberUseCase getCastMemberUseCase() {
        return new DefaultGetCastMemberByIdUseCase(gateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(gateway);
    }
}
