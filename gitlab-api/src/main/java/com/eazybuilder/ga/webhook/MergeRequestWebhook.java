package com.eazybuilder.ga.webhook;

import com.eazybuilder.ga.fegin.DemandFeginService;
import com.eazybuilder.ga.pojo.CIPackagePojo;
import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.pojo.merge.GLWHRootInfo;
import com.eazybuilder.ga.service.AddNoteService;
import com.eazybuilder.ga.service.CIPackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mergeRequest")
@CrossOrigin
public class MergeRequestWebhook {

    private static Logger logger = LoggerFactory.getLogger(MergeRequestWebhook.class);

    /** merge请求状态 */
    private final String MERGE_STATUS = "can_be_merged";
    /** merge操作状态 */
    private final String STATUS = "merged";
    /** 目标分支，即要合并到的分支 */
    private final String TARGET_BRANCH = "master";

    @Autowired
    private DemandFeginService demandFeginService;

    @Autowired
    private CIPackageService ciPackageService;

    @Autowired
    private AddNoteService addNoteService;


    @PostMapping("/createMergeHook")
    public void createMergeHook(@RequestBody GLWHRootInfo glwhRootInfo){
        // 获取项目名称
        String projectName = glwhRootInfo.getProject().getName();
        // 获取gitlab触发此次请求的操作类型，比如提交、同意、撤销合并分支请求
        String merge_status = glwhRootInfo.getObject_attributes().getMerge_status();
        String state = glwhRootInfo.getObject_attributes().getState();
        // 获取source分支和获取target分支
        String target_branch = glwhRootInfo.getObject_attributes().getTarget_branch();
        String source_branch = glwhRootInfo.getObject_attributes().getSource_branch();
        // 获取操作用户邮箱
        String user_email =
                glwhRootInfo.getObject_attributes().getLast_commit().getAuthor().getEmail();
        String user_name = glwhRootInfo.getUser().getUsername();
        String http_url = glwhRootInfo.getProject().getHttp_url();

        String arr[];
        String code;
        if(source_branch.contains("-")){
            arr = source_branch.split("-");
        }else if(source_branch.contains("#")){
            arr = source_branch.split("#");
        }else{
            return;
        }
        if(arr.length>0){
            code = arr[arr.length-1];
        }else{
            return;
        }

        String topCode = String.valueOf(demandFeginService.getTopIssueId(Integer.valueOf(code)));

        CIPackagePojo pojo = new CIPackagePojo();
        pojo.setCode(code);
        pojo.setImageTag(source_branch);
        pojo.setProjectName(projectName);
        pojo.setUserName(user_name);
        pojo.setTopCode(topCode);
        pojo.setTagName(source_branch);
        pojo.setGitPath(http_url);

        /*
         * profileCode 以逗号隔开的字符串。例如：1，2，3，4
         *1：单元测试及覆盖率检查
         *2：工程依赖包安全检查
         *3：源码质量及漏洞扫描
         *4：扫描JavaScript
         *--5：扫描SQL(支持ibatis/mybatis) 暂时不用
         *6：docker镜像构建
         *7: 自动发布/部署
         *8: 推送war包
         * */
        pojo.setProfileCode("1,2,3,4");
        ciPackageService.sendCIPackage(pojo);
    }


}
