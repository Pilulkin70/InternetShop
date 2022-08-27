package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.BrandDto;
import ua.garmash.internetshop.model.Brand;

import java.util.List;


public interface BrandService {

    void save(Brand brand);

    List<BrandDto> findAll();
}
