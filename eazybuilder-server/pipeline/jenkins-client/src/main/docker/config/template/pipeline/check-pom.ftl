<#if project.profile?? && project.profile.checkPom>
    stage('snapshot检查'){
        steps{
 echo '========snapshot检查 start========'
            mavenSnapshotCheck check: 'true'
 echo '========snapshot检查 image end========'
        }
    }
</#if>