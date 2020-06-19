package project.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.ListResponseDto;
import project.models.Language;
import project.services.PlatformService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(value = "/api/v1/platform/")
@Validated
public class ApiPlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping("languages")
    ResponseEntity<?> languages(@RequestParam(required = false) String language,
                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
                                @RequestParam(required = false, defaultValue = "20") @Positive @Max(20) Integer itemPerPage) {
        Page<Language> page;
        if (language == null || language.isEmpty()) {
            page = platformService.getLanguages(offset, itemPerPage);
        } else {
            page = platformService.getLanguages(language, offset, itemPerPage);
        }

        return ResponseEntity.ok(new ListResponseDto<>(page.getTotalElements(), offset, itemPerPage, page.getContent()));
    }
}