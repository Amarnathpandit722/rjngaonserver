package gov.municipal.suda.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperUtils {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /*
    Hide from public usage
    */
    private ObjectMapperUtils() {

    }

    /*
    * Note: outClass object must have default constructor with no arguments
    * @param <D>     type of result object.
    * @param <T>     type of source object to map from.
    * @param entity  entity that needs to be mapped.
    * @param outClass class of result object.
    * @return new object of outClass type.
    *
    * */

    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outClass) {
       return entityList.stream().map(entity -> map(entity, outClass)).collect(Collectors.toList());
    }

    public static <S,D> D map(final S source, D destination) {
        modelMapper.map(source,destination);
        return destination;
    }

    public static <D,T> D mapSkipNull(final T entity, Class<D> outClass) {
        ModelMapper modelMapperSkipNull = new ModelMapper();
        modelMapperSkipNull.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapperSkipNull.map(entity,outClass);
    }

    public static <D, T> List<D> mapAllSkipNull (final Collection<T> entityList, Class<D> outClass) {
        return entityList.stream().map(entity -> mapSkipNull(entity,outClass)).collect(Collectors.toList());
    }
}
