package dsw.springframework.spring6restmvc.mappers;

import dsw.springframework.spring6restmvc.entities.Beer;
import dsw.springframework.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;


@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

}