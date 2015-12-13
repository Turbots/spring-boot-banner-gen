package be.ordina.cloudfoundry.banner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BannerOptions {

    private MultipartFile image;
    private boolean dark;
}
