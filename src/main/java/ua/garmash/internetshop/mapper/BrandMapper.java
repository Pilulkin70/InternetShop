package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.garmash.internetshop.dto.BrandDto;
import ua.garmash.internetshop.model.Brand;

import java.util.List;

@Mapper
public interface BrandMapper {
    BrandMapper MAPPER = Mappers.getMapper(BrandMapper.class);

    Brand toBrand(BrandDto dto);

    @InheritInverseConfiguration
    BrandDto fromBrand(Brand brand);

    List<Brand> toBrandList(List<BrandDto> BrandDtos);

    List<BrandDto> fromBrandList(List<Brand> brands);
}
