package it.springframework.msscbrewery.web.mappers;

import it.springframework.msscbrewery.domain.Beer;
import it.springframework.msscbrewery.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);
}
