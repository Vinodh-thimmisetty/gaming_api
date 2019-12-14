package com.sutherland.web;

import com.sutherland.domain.GamingInfo;
import com.sutherland.entity.AuthKeys;
import com.sutherland.repository.AuthRepository;
import com.sutherland.service.GamingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author vinodh kumar thimmisetty
 */
@RestController
@RequestMapping("/v1/gaming")
@Api(tags = "API(s) to manage the GAMING information.")
@Slf4j
public class GamingController {

    private GamingService gamingService;
    private AuthRepository authRepository;

    public GamingController(GamingService gamingService, AuthRepository authRepository) {
        this.gamingService = gamingService;
        this.authRepository = authRepository;
    }

    @GetMapping("/keys")
    @ApiOperation(value = "To get validation keys.")
    public ResponseEntity<?> getAuthKeys() {
        return ResponseEntity.ok(authRepository.findAll().stream().map(AuthKeys::getKey).collect(Collectors.toList()));
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "Find All available game details. By default, TOP 15 gaming details based on score are listed.", tags = {})
    @ApiImplicitParam(name = "Authorization", value = "Enter authorization Key", required = true, paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<?> findAll(@RequestHeader("Authorization") String key,
                                     @ApiParam(example = "score") @RequestParam(required = false, defaultValue = "score") String sortBy,
                                     @ApiParam(example = "DESC") @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
                                     @ApiParam(example = "0") @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                     @ApiParam(example = "15") @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        if (!validateKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key.");
        }
        final Pageable pageable = PageRequest.of(pageNumber,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection),
                        sortBy));
        return ResponseEntity.ok(this.gamingService.findAllGames(pageable));
    }

    @GetMapping("/filterGames")
    @ApiOperation(value = "Filter available game details in any combination of [title/platform/genre/score/editors_choice]. By default, TOP 15 gaming details based on score are listed.", tags = {})
    @ApiImplicitParam(name = "Authorization", value = "Enter authorization Key", required = true, paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<?> filterGames(@RequestHeader("Authorization") String key,
                                         @ApiParam(example = "Fable: The Journey") @RequestParam(required = false) String title,
                                         @ApiParam(example = "Xbox 360") @RequestParam(required = false) String platform,
                                         @ApiParam(example = "RPG") @RequestParam(required = false) String genre,
                                         @ApiParam(example = "7.2") @RequestParam(required = false) Double score,
                                         @ApiParam(example = "N") @RequestParam(required = false) String editors_choice,
                                         @ApiParam(example = "score") @RequestParam(required = false, defaultValue = "score") String sortBy,
                                         @ApiParam(example = "DESC") @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
                                         @ApiParam(example = "0") @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                         @ApiParam(example = "15") @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        if (!validateKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key.");
        }
        return ResponseEntity.ok(this.gamingService.filterGames(title, platform, genre, score, editors_choice, sortBy, sortDirection, pageNumber, pageSize));
    }

    @PostMapping("/addGame")
    @ApiOperation(value = "Add new game details. Gaming id is auto-generated, so avoid giving your own numbers.", tags = {})
    @ApiImplicitParam(name = "Authorization", value = "Enter authorization Key", required = true, paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<?> create(@RequestHeader("Authorization") String key,
                                    @RequestBody GamingInfo info) {
        if (!validateKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key.");
        }
        return new ResponseEntity<>(this.gamingService.createGame(info), HttpStatus.CREATED);
    }

    @PutMapping("/updateGame")
    @ApiOperation(value = "Update any existing game based on their Gaming ID", tags = {})
    @ApiImplicitParam(name = "Authorization", value = "Enter authorization Key", required = true, paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<?> update(@RequestHeader("Authorization") String key, @ApiParam(example = "15") @RequestParam Long gameId,
                                    @RequestBody GamingInfo info) {
        final GamingInfo gamingInfo = this.gamingService.updateGame(gameId, info);
        if (!validateKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key.");
        }
        return (gamingInfo != null) ? ResponseEntity.ok(gamingInfo) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteGame")
    @ApiOperation(value = "DELETE any existing game based on their Gaming ID", tags = {})
    @ApiImplicitParam(name = "Authorization", value = "Enter authorization Key", required = true, paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<?> deleteGame(@RequestHeader("Authorization") String key, @ApiParam(example = "15") @RequestParam Long gameId) {
        final GamingInfo gamingInfo = this.gamingService.deleteGame(gameId);
        if (!validateKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Key.");
        }
        return (gamingInfo != null) ? ResponseEntity.ok(gamingInfo) : ResponseEntity.notFound().build();
    }

    private boolean validateKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            return authRepository.findAll().stream().map(AuthKeys::getKey).anyMatch(x -> x.equalsIgnoreCase(key));
        }
        return false;
    }

}
