package com.nesrux.admin.catalogo.application;

import com.github.javafaker.Faker;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static final class CastMembers {
        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }

    public static final class Video {

        public static String title() {
            return FAKER.options().option(
                    "Não acredite nas mentiras dele",
                    "Top 10 coisas que você precisa saber antes de morrer",
                    "Falta apenas 5 minutos para namekusei explodir",
                    "A vida e a morte de Akira toriama",
                    "Como o autor de mangas Togashi criou duas lendas do mundo dos mangas",
                    "Lucas montano do canal Lucas montano montando uma montana em cima de uma montanha",
                    "Dj cleiton hasta vai fazer a galera debochar legal ao som do cabeça de Gelo, OLHA A PEDRA"
            );
        }
    }
}
