package com.nesrux.admin.catalogo.domain;

import com.github.javafaker.Faker;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.video.Rating;
import com.nesrux.admin.catalogo.domain.video.Resource;
import com.nesrux.admin.catalogo.domain.video.Video;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class CastMembers {

        private static final CastMember JOAO = CastMember.newMember("João", CastMemberType.DIRECTOR);
        private static final CastMember MARIA = CastMember.newMember("Maria", CastMemberType.ACTOR);
        private static final CastMember CATARINA = CastMember.newMember("Catarina", CastMemberType.DIRECTOR);
        private static final CastMember THIAGO = CastMember.newMember("Thiago", CastMemberType.ACTOR);

        public static CastMember joao() {
            return CastMember.with(JOAO);
        }

        public static CastMember catarina() {
            return CastMember.with(CATARINA);
        }

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember randomMember() {
            return FAKER.options().option(
                    CastMember.with(JOAO),
                    CastMember.with(MARIA),
                    CastMember.with(CATARINA),
                    CastMember.with(THIAGO)

            );
        }
    }

    public static final class Genres {
        private static final Genre ACAO = Genre.newGenre("Ação", true);
        private static final Genre TERROR = Genre.newGenre("Terror", false);
        private static final Genre AVENTURA = Genre.newGenre("Aventura", true);
        private static final Genre TECH = Genre.newGenre("Technology", true);
        private static final Genre BUSINESS = Genre.newGenre("Business", true);

        public static Genre randomGenre() {
            return FAKER.options().option(
                    Genre.with(ACAO),
                    Genre.with(TERROR),
                    Genre.with(AVENTURA)
            );
        }

        public static Genre tech() {
            return Genre.with(TECH);
        }

        public static Genre business() {
            return Genre.with(BUSINESS);
        }
    }

    public static final class Categories {
        private static final Category DOCUMENTARIO = Category.newCategory("Documentarios", "A categoria de documentarios", true);
        private static final Category SERIES = Category.newCategory("Séries", "A categoria de Séries", false);
        private static final Category FILMES = Category.newCategory("Filmes", "A categoria de Filmes", true);
        private static final Category AULAS = Category.newCategory("Aulas", "Video aulas", true);
        private static final Category LIVES = Category.newCategory("Lives", "Livestream de conteudos variados", true);

        public static Category aulas() {
            return Category.with(AULAS);
        }

        public static Category lives() {
            return Category.with(LIVES);
        }

        public static Category randomCategory() {
            return FAKER.options().option(
                    Category.with(DOCUMENTARIO),
                    Category.with(SERIES),
                    Category.with(FILMES)

            );
        }

    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(2022),
                Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                randomRating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.joao().getId(), CastMembers.catarina().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Video randomVideo() {
            return Video.newVideo(
                    title(),
                    description(),
                    Year.of(2022),
                    Videos.randomDuration(),
                    Fixture.bool(),
                    Fixture.bool(),
                    randomRating(),
                    Set.of(Categories.randomCategory().getId()),
                    Set.of(Genres.randomGenre().getId()),
                    Set.of(CastMembers.randomMember().getId())
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static Integer year() {
            return FAKER.random().nextInt(2015, 2025);
        }

        public static Double randomDuration() {
            return FAKER.options().option(
                    60.0, 120.0, 190.30, 210.50, 30.0
            );
        }

        public static Rating randomRating() {
            return FAKER.options().option(Rating.values());
        }

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

        public static String description() {
            return FAKER.options().option(
                    """
                            Disclaimer: O conteudo a seguir não deve ser repoduzido por pessoas
                            não expecialista, cada cena foi pensada cuidadosamente para que todos 
                            os atores saissem seguros, não tente isso em casa
                            """,
                    """
                            Togashi foi é o criador das melhores obras de ficção japonesa do ultimo século
                            ele é o autor dos mangas extremamente renomados "Hunter x Hunter" e
                            "Yuyu hakusho", onde fez seu primeiro suceso é notorio afirmar que
                            """,
                    """
                            A série não acredite nas mentiras dele é uma serie de video no youtube
                            criado pelo youtuber e influenciador de conteudo digital Rafael "Cellbit" lange
                            """,
                    """
                            a Vida e obra do dj cleiton hasta, além do grande suecesso com a musica Cabeça de gelo
                            ele atualmente tem a sua vida retratada em um filme, de mesmo nome
                            que retrata a dificuldade de ser um musico no interior do maranhão
                            """,
                    """
                            Akira toriama foi mundialmente conhecido por causa das suas obras de ficção chamada Dragon Ball e 
                            blue Dragon, também teve suas obras adaptadas para games, animes, documentarios, intelizmente ele foi
                            encontrado morto essa manha, a familia não quis dar detalhes do acontecimento, muito se expecula que...
                            """,
                    """
                            O criador de conteudo de programação Lucas montano recentemente entrou em uma polemica onde ele desistiu
                            da sua carreira de Eng de software senior na disney para viver em uma montanha concentando carros
                            em entrevista com nosse reporter ele disse "cara to cansado muito teste em produção me deixou exausto"...
                            """
            );

        }


    }
}
