package org.alwyn.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.common.result.Results;
import org.alwyn.shortlink.admin.dto.req.GroupCreationReqDTO;
import org.alwyn.shortlink.admin.dto.req.GroupUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.GroupListQueryRespDTO;
import org.alwyn.shortlink.admin.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/short-link/admin/group/creation")
    public Result<Void> createGroupByGroupName(@RequestBody GroupCreationReqDTO requestParam) {
        groupService.createGroupByGroupName(requestParam.getGroupname(), requestParam.getUsername());
        return Results.success();
    }

    @GetMapping("/api/short-link/admin/group/query")
    public Result<List<GroupListQueryRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

    @PutMapping("/api/short-link/admin/group/update")
    public Result<Integer> updateGroupByGid(@RequestBody GroupUpdateReqDTO requestParam) {
        return Results.success(groupService.updateGroupByGroupName(requestParam));
    }

    @DeleteMapping("/api/short-link/admin/group/update")
    public Result<Integer> deleteGroupByGid(@RequestParam String gid) {
        return Results.success(groupService.deleteGroupByGid(gid));
    }


}

