package com.shop.web.item;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.constant.category.Category;
import com.shop.dto.item.ItemFormDto;
import com.shop.dto.item.ItemSearchDto;
import com.shop.dto.item.MainItemDto;
import com.shop.entity.item.Item;
import com.shop.service.category.CategoryService;
import com.shop.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class itemController {

    private final ItemService itemService;
    private final CategoryService categoryService;


    @GetMapping("/admin/item/new")
    public String createItemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());

        ObjectNode categoryNode = categoryService.getCategoryNode(Category.Root);
        model.addAttribute("categoryNode", categoryNode);

        return "/item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String createItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors())
            return "/item/itemForm";

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값 입니다.");
            return "/item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";

    }

    @GetMapping("/admin/item/{itemId}")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            model.addAttribute("itemId", itemId);

            ObjectNode categoryNode = categoryService.getCategoryNode(Category.Root);
            model.addAttribute("categoryNode", categoryNode);


        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";

    }

    @PostMapping("/admin/item/{itemId}")
    public String updateItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors())
            return "item/itemForm";

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        try {
            Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
            model.addAttribute("items", items);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 관리 페이지 조회 중 에러가 발생하였습니다.");
            return "redirect:/";
        }

        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDto);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 조회 중 에러가 발생하였습니다.");
            return "redirect:/";
        }

        return "item/itemDtl";
    }

    @GetMapping("/item/list")
    public String itemList(String code, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        Page<MainItemDto> items = itemService.getItemList(code, pageable);

        System.out.println(items.isFirst());
        model.addAttribute("categoryCode", code);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);

        return "item/itemList";
    }

    //카테고리 조회
    @GetMapping("/item/categories")
    public List<Category.CategoryResponse> categoryList(){
        Category[] values = Category.values();

        return Arrays.stream(values).map((value) ->
                        new Category.CategoryResponse(
                                value.toString(), Category.valueOf(value.toString()))
                )
                .collect(Collectors.toList());
    }

}
