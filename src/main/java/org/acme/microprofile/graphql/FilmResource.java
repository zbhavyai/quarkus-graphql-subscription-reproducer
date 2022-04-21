package org.acme.microprofile.graphql;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Source;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;

@GraphQLApi
@ApplicationScoped
public class FilmResource {

    @Inject
    GalaxyService service;

    BroadcastProcessor<Hero> processor = BroadcastProcessor.create();

    @Query("allFilms")
    @Description("Get all Films from a galaxy far far away")
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @Query
    @Description("Get a Films from a galaxy far far away")
    public Film getFilm(@Name("filmId") int id) {
        return service.getFilm(id);
    }

    public List<Hero> heroes(@Source Film film) {
        return service.getHeroesByFilm(film);
    }

    @Mutation
    public Hero createHero(Hero hero) {
        service.addHero(hero);
        processor.onNext(hero);
        return hero;
    }

    @Subscription
    public Multi<Hero> heroCreated() {
        System.out.println("\nSubscription method called");
        return processor;
    }

    @Mutation
    public Hero deleteHero(int id) {
        return service.deleteHero(id);
    }

    @Query
    public List<Hero> getHeroesWithSurname(@DefaultValue("Skywalker") String surname) {
        return service.getHeroesBySurname(surname);
    }

}
