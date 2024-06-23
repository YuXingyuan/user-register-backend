package com.interview.userregister.controller;

import com.interview.userregister.model.request.UserRequest;
import com.interview.userregister.model.response.BaseResponse;
import com.interview.userregister.service.UserRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.interview.userregister.constant.UserRegisterConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = API_BASE_PATH, produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Register APIs", description = "For adding, editing, reading and deleting")
public class UserRegisterController {

    private UserRegisterService userRegisterService;

    @Autowired
    public UserRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @Operation(summary = "Add User")
    @PostMapping(path = API_ADD_PATH, consumes = APPLICATION_JSON_VALUE)
    public BaseResponse addUser(@Valid @RequestBody UserRequest user) {
        return userRegisterService.addUser(user);
    }

    @Operation(summary = "Edit User by Id")
    @PostMapping(path = API_EDIT_PATH, consumes = APPLICATION_JSON_VALUE)
    public BaseResponse editUser(@Valid @RequestBody UserRequest user, @PathVariable("id") String id) {
        return userRegisterService.editUser(user, id);
    }

    @Operation(summary = "Delete User by Id")
    @DeleteMapping(path = API_DELETE_PATH)
    public BaseResponse deleteUser(@PathVariable("id") String id) {
        return userRegisterService.deleteUser(id);
    }

    @Operation(summary = "Delete Selected Users by Ids")
    @DeleteMapping(path = API_DELETE_SELECTED_PATH)
    public BaseResponse deleteSelectedUser(@RequestParam("ids") List<String> idList) {
        return userRegisterService.deleteSelectedUser(idList);
    }

    @Operation(summary = "Get User by Id")
    @GetMapping(path = API_GET_PATH)
    public BaseResponse getUser(@PathVariable("id") String id) {
        return userRegisterService.getUser(id);
    }

    @Operation(summary = "Get All Users")
    @GetMapping(path = API_GET_ALL_PATH)
    public BaseResponse getAllUser() {
        return userRegisterService.getAllUser();
    }

}
