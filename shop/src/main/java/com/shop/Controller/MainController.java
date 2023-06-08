package com.shop.Controller;

import com.shop.constant.ViewModel;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {


    @GetMapping(value = "/")
    public String main(Model model) {
        model.addAttribute(ViewModel.USER_ID, ViewModel.getUserName());
        return "main";
    }

    @GetMapping(value = "/information")
    public String mains(Model model){
        model.addAttribute(ViewModel.USER_ID, ViewModel.getUserName());
        return "information";
    }


}
