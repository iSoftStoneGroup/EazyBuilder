 <#if project.profile?? && project.profile.createMR &&  buildVo?? && (buildVo.pipelineType == '代码提交' || buildVo.pipelineType == '手动触发') >
    //deploy
    stage('Github Pull Request Merger') {
        steps{
            echo '========Github Pull Request Merger start========'
            sh 'curl GET  "${GITLAB_API_ADDR}/gitlab-api/gitlab/checkAndCreateMR?projectUrl=${buildVo.encodedUrl}&source_branch=${buildVo.targetBranch}&target_branch=master&mergeDesignee=${project.profile.mergeDesignee}&groupCode=${project.team.code}" '
            echo '========Github Pull Request Merger end========'
        }
    }
 </#if>