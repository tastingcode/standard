package com.shop.web;

import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        try {
            Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
            model.addAttribute("items", items);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "메인 화면 조회 중 에러가 발생하였습니다.");
        }

        return "main";

    }



    @GetMapping("/myPage")
    public String myPage(){
        return "myPage";
    }

}
