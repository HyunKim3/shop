package com.shop.Controller;

import com.shop.constant.ViewModel;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ItemService itemService;

    @GetMapping(value = { "/menu", "/menu/{menu_type}" })
    public String menu(ItemSearchDto itemSearchDto, Optional<Integer> page,
                       @PathVariable(name = "menu_type") String menuType,
                       Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);

        if(!StringUtils.isEmpty(menuType)) {
            if(!"AllMenu".equals(menuType)) {
                itemSearchDto.setSearchItemValue(menuType.toUpperCase());
                itemSearchDto.setSearchBy("itemValue");
            }
        }

        // itemSearchDto
        System.out.println("itemSearchDto ==> " + itemSearchDto);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        // 사용자 정보를 모델에 추가
        System.out.println(items.getNumber()+"!!!!!!!!!");
        System.out.println(items.getTotalPages()+"##########");

        if(!StringUtils.isEmpty(menuType)) {
            System.out.println("menu_type ==> " + menuType);
        }

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("menuType", menuType);

        model.addAttribute(ViewModel.IS_EMPTY, items.getTotalElements() <= 0 ? "Y" : "N");
        model.addAttribute(ViewModel.USER_ID, ViewModel.getUserName());

        return "menu";
    }
}
