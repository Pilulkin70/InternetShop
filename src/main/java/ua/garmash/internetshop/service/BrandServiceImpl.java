package ua.garmash.internetshop.service;

import org.springframework.stereotype.Service;
import ua.garmash.internetshop.repository.BrandRepository;
import ua.garmash.internetshop.dto.BrandDto;
import ua.garmash.internetshop.mapper.BrandMapper;
import ua.garmash.internetshop.model.Brand;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{
    private final BrandMapper brandMapper = BrandMapper.MAPPER;

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public List<BrandDto> findAll() {
        return brandMapper.fromBrandList(brandRepository.findAll());
    }
}
