package org.infoshare.rekinyfinansjeryweb.restController;

import org.infoshare.rekinyfinansjeryweb.dto.FiltrationSettingsDTO;
import org.infoshare.rekinyfinansjeryweb.entity.user.MyUserPrincipal;
import org.infoshare.rekinyfinansjeryweb.entity.user.User;
import org.infoshare.rekinyfinansjeryweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FiltrationPreferencesController {
    @Autowired
    UserService userService;
    @GetMapping("/filtration_preferences/{name}")
    public ResponseEntity<FiltrationSettingsDTO> receiveSavedFiltrationPreferences(@PathVariable("name") String name,
                                                                                   @AuthenticationPrincipal MyUserPrincipal principal){

        Map<String, FiltrationSettingsDTO> map = userService.getSavedFiltrationSettings();
        if(map.containsKey(name)){
            return new ResponseEntity<>(map.get(name), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
