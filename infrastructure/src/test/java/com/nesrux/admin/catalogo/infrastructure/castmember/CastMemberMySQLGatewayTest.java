package com.nesrux.admin.catalogo.infrastructure.castmember;

import com.nesrux.admin.catalogo.MySQLGatewayTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {
    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    private void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }
}