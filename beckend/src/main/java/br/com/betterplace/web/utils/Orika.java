package br.com.betterplace.web.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class Orika {

    private static MapperFactory mapperFactory;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
//        mapperFactory.registerClassMap(mapperFactory.classMap(CreativeFileValidation.class, CreativeFileValidationVO.class)
//                .field("id.creativeFileId", "creativeFileId")
//                .field("id.siteId", "siteId")
//                .field("id.formatId", "formatId")
//                .byDefault()
//                .toClassMap());
//        mapperFactory.registerClassMap(mapperFactory.classMap(ExternalAdvertiserRel.class, IDDescriptionVO.class)
//                .field("adserverAdvertiserId", "id")
//                .field("adserverAdvertiserDescription", "description")
//                .byDefault()
//                .toClassMap());
//        mapperFactory.registerClassMap(mapperFactory.classMap(ExternalAgencyRel.class, IDDescriptionVO.class)
//                .field("adserverAgencyId", "id")
//                .field("adserverAgencyDescription", "description")
//                .byDefault()
//                .toClassMap());
//        mapperFactory.registerClassMap(mapperFactory.classMap(PushStatus.class, IDDescriptionVO.class)
//                .byDefault()
//                .toClassMap());
//        mapperFactory.registerClassMap(mapperFactory.classMap(CreativeAction.class, IDDescriptionVO.class)
//                .byDefault()
//                .toClassMap());
//        mapperFactory.registerClassMap(mapperFactory.classMap(Placement.class, PlacementVO.class)
//                .field("onlyTags", "tags")
//                .byDefault()
//                .toClassMap());
    }

    public static MapperFacade get() {
        return mapperFactory.getMapperFacade();
    }
}