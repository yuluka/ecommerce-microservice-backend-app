package com.selimhorri.app.helper;

import com.selimhorri.app.domain.Address;
import com.selimhorri.app.domain.User;
import com.selimhorri.app.dto.AddressDto;
import com.selimhorri.app.dto.UserDto;

public interface AddressMappingHelper {

	public static AddressDto map(final Address address) {
		return AddressDto.builder()
				.addressId(address.getAddressId())
				.fullAddress(address.getFullAddress())
				.postalCode(address.getPostalCode())
				.city(address.getCity())
				.userDto(
						UserDto.builder()
								.userId(address.getUser().getUserId())
								.firstName(address.getUser().getFirstName())
								.lastName(address.getUser().getLastName())
								.imageUrl(address.getUser().getImageUrl())
								.email(address.getUser().getEmail())
								.phone(address.getUser().getPhone())
								.build())
				.build();
	}

	public static Address map(final AddressDto addressDto) {
		Address.AddressBuilder builder = Address.builder()
				.addressId(addressDto.getAddressId())
				.fullAddress(addressDto.getFullAddress())
				.postalCode(addressDto.getPostalCode())
				.city(addressDto.getCity());

		if (addressDto.getUserDto() != null) {
			builder.user(
					User.builder()
							.userId(addressDto.getUserDto().getUserId())
							.firstName(addressDto.getUserDto().getFirstName())
							.lastName(addressDto.getUserDto().getLastName())
							.imageUrl(addressDto.getUserDto().getImageUrl())
							.email(addressDto.getUserDto().getEmail())
							.phone(addressDto.getUserDto().getPhone())
							.build());
		}

		return builder.build();
	}

}
