package org.example.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.TourDTO;
import org.example.dto.UserTourOrderDTO;
import org.example.modal.Tour;
import org.example.modal.UserTourOrder;
import org.example.service.IUserTourOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class UserTourOrderController {
	@Autowired
	private IUserTourOrderService userTourOrderService;
	@Autowired
	private ModelMapper modelMapper;
	@GetMapping
	public Page<UserTourOrderDTO> pageUTOs(Pageable pageable) {
	    Page<UserTourOrder> pageUTOs = userTourOrderService.pageUTOs(pageable);

	    // Ánh xạ từ List<UserTourOrder> sang List<UserTourOrderDTO>
	    List<UserTourOrderDTO> pageUTOsDTO = pageUTOs.getContent().stream()
	            .map(uto -> modelMapper.map(uto, UserTourOrderDTO.class))  // Cung cấp kiểu đích rõ ràng
	            .collect(Collectors.toList());

	    // Tạo Page<UserTourOrderDTO> từ List<UserTourOrderDTO>
	    Page<UserTourOrderDTO> dtoPage = new PageImpl<>(pageUTOsDTO, pageable, pageUTOs.getTotalElements());
	    return dtoPage;
	}

}
