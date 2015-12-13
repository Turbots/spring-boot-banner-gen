package be.ordina.cloudfoundry.controller;

import be.ordina.cloudfoundry.banner.Banner;
import be.ordina.cloudfoundry.banner.BannerGenerator;
import be.ordina.cloudfoundry.banner.BannerOptions;
import be.ordina.cloudfoundry.validation.UploadValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerGenerator bannerGenerator;

    @InitBinder("bannerOptions")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UploadValidator());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(@SuppressWarnings("UnusedParameters") BannerOptions bannerOptions) {
        return "banner";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String generateBanner(@Valid @ModelAttribute("bannerOptions") BannerOptions bannerOptions, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "banner";
        }
        log.info("Received request for image [" + bannerOptions.getImage().getOriginalFilename() + "]");
        Banner banner = bannerGenerator.generateBanner(bannerOptions.getImage(), bannerOptions.isDark());
        model.addAttribute("banner", banner);
        return "banner";
    }
}
