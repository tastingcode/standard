package com.shop.service.item_img;

import com.shop.entity.item_img.ItemImg;
import com.shop.entity.item_img.ItemImgRepository;
import com.shop.service.etc.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgPath}")
    private String itemImgPath;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgPath, itemImgFile);
            imgUrl = "/images/itemimgs/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws IOException {

        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(savedItemImg.getImgName()))
                fileService.deleteFile(itemImgPath + savedItemImg.getImgName());

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgPath, itemImgFile);
            String imgUrl = "/images/itemimgs/" + imgName;

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }

    }

}
