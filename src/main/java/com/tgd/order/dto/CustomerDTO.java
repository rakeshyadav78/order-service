package com.tgd.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String mobNum;
	private String mobNum2;
	private String email;
	private String email2;
	private List<AddressDTO> addresses;
}
