package com.selimhorri.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.selimhorri.app.constant.AppConstant;
import com.selimhorri.app.domain.id.FavouriteId;
import com.selimhorri.app.dto.FavouriteDto;
import com.selimhorri.app.dto.ProductDto;
import com.selimhorri.app.dto.UserDto;
import com.selimhorri.app.exception.wrapper.FavouriteNotFoundException;
import com.selimhorri.app.helper.FavouriteMappingHelper;
import com.selimhorri.app.repository.FavouriteRepository;
import com.selimhorri.app.service.FavouriteService;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {
	
	private final FavouriteRepository favouriteRepository;
	private final RestTemplate restTemplate;

	// This method is commented out because it is the selected one to be refactorized to implement the RETRY pattern.
	// @Override
	// public List<FavouriteDto> findAll() {
	// 	log.info("*** FavouriteDto List, service; fetch all favourites *");
	// 	return this.favouriteRepository.findAll()
	// 			.stream()
	// 				.map(FavouriteMappingHelper::map)
	// 				.map(f -> {
	// 					f.setUserDto(this.restTemplate
	// 							.getForObject(AppConstant.DiscoveredDomainsApi
	// 									.USER_SERVICE_API_URL + "/" + f.getUserId(), UserDto.class));
	// 					f.setProductDto(this.restTemplate
	// 							.getForObject(AppConstant.DiscoveredDomainsApi
	// 									.PRODUCT_SERVICE_API_URL + "/" + f.getProductId(), ProductDto.class));
	// 					return f;
	// 				})
	// 				.distinct()
	// 				.collect(Collectors.toUnmodifiableList());
	// }

	@TimeLimiter(name = "userService")
	@Retry(name = "userService", fallbackMethod = "fallbackUser")
	public UserDto getUserById(int userId) {
		return this.restTemplate.getForObject(
			AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_URL + "/" + userId,
			UserDto.class);
	}

	@TimeLimiter(name = "productService")
	@Retry(name = "productService", fallbackMethod = "fallbackProduct")
	public ProductDto getProductById(int productId) {
		return this.restTemplate.getForObject(
			AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_URL + "/" + productId,
			ProductDto.class);
	}

	public UserDto fallbackUser(int userId, Exception e) {
		log.warn("Fallo al obtener user {}", userId);
		return null;
	}

	public ProductDto fallbackProduct(int productId, Exception e) {
		log.warn("Fallo al obtener product {}", productId);
		return null;
	}

	@Override
	public List<FavouriteDto> findAll() {
		log.info("*** FavouriteDto List, service; fetch all favourites *");
		return this.favouriteRepository.findAll()
				.stream()
					.map(FavouriteMappingHelper::map)
					.map(f -> {
						f.setUserDto(getUserById(f.getUserId()));
						f.setProductDto(getProductById(f.getProductId()));
						return f;
					})
					.distinct()
					.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public FavouriteDto findById(final FavouriteId favouriteId) {
		log.info("*** FavouriteDto, service; fetch favourite by id *");
		return this.favouriteRepository.findById(favouriteId)
				.map(FavouriteMappingHelper::map)
				.map(f -> {
					f.setUserDto(this.restTemplate
							.getForObject(AppConstant.DiscoveredDomainsApi
									.USER_SERVICE_API_URL + "/" + f.getUserId(), UserDto.class));
					f.setProductDto(this.restTemplate
							.getForObject(AppConstant.DiscoveredDomainsApi
									.PRODUCT_SERVICE_API_URL + "/" + f.getProductId(), ProductDto.class));
					return f;
				})
				.orElseThrow(() -> new FavouriteNotFoundException(
						String.format("Favourite with id: [%s] not found!", favouriteId)));
	}
	
	@Override
	public FavouriteDto save(final FavouriteDto favouriteDto) {
		return FavouriteMappingHelper.map(this.favouriteRepository
				.save(FavouriteMappingHelper.map(favouriteDto)));
	}
	
	@Override
	public FavouriteDto update(final FavouriteDto favouriteDto) {
		return FavouriteMappingHelper.map(this.favouriteRepository
				.save(FavouriteMappingHelper.map(favouriteDto)));
	}
	
	@Override
	public void deleteById(final FavouriteId favouriteId) {
		this.favouriteRepository.deleteById(favouriteId);
	}
	
	
	
}










