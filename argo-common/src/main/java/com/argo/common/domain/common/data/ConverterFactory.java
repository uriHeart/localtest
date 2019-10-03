package com.argo.common.domain.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ConverterFactory {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, Converter> conveterMap;
}
    /*
    public Converter getConverter(String source, String destination) {

    }
}

    @Mapper
    public interface MapStructConverter extends Converter {
        MapStructConverter MAPPER = Mappers.getMapper(MapStructConverter.class);

        @Mapping(source = "status", target = "orderStatus")
        @Override
        Order convert(SourceOrder sourceOrder);

        @Override
        DestinationCode convert(SourceCode sourceCode);


        public class ModelMapperConverter implements Converter {
            private ModelMapper modelMapper;

            public ModelMapperConverter() {
                modelMapper = new ModelMapper();
            }

            @Override
            public Order convert(SourceOrder sourceOrder) {
                return modelMapper.map(sourceOrder, Order.class);
            }

            @Override
            public DestinationCode convert(SourceCode sourceCode) {
                return modelMapper.map(sourceCode, DestinationCode.class);
            }
        }*/
