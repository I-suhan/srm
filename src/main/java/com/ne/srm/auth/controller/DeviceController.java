package com.ne.srm.auth.controller;

import com.ne.srm.auth.dto.DeviceResponse;
import com.ne.srm.auth.service.UserSessionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final UserSessionsService userSessionsService;

    //获取设备列表
    @GetMapping("me")
    public List<DeviceResponse> devices(){
        return userSessionsService.devices();

    }


}
