package com.tgd.order.dto;

import lombok.Data;

@Data
public class AddressDTO {
	private Long id;
	private String add1;
	private String add2;
	private String city;
	private String pincode;
	private String country;
}
