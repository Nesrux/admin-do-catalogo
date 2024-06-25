package com.nesrux.admin.catalogo.infrastructure.castmember;

import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.nesrux.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {
    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public void deleteById(final CastMemberID anId) {
        if (repository.existsById(anId.getValue())) {
            repository.deleteById(anId.getValue());
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return this.repository.findById(anId.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()),
                        aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpacification)
                .orElse(null);
        final var pageResult = this.repository.findAll(where, page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CastMemberID> existsByIds(Iterable<CastMemberID> ids) {
        throw new UnsupportedOperationException();
    }

    private CastMember save(final CastMember aCastMember) {
        return this.repository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpacification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
