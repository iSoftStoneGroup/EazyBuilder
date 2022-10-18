<#if project.profile?? && project.profile.updateConfig?string == "true">
    stage('update config file in nacos'){
        steps {
            echo '========update config file in nacos start========'
            echo '${project.profile.nameSpace}'
            script {
                def toJson = {
                    input ->
                    groovy.json.JsonOutput.toJson(input)
                }
                def nacos_url= "http://${gitlab_api_domain}/gitlab-api/hook/releaseConfigPipeline"
                out =sh(script:"ls ${project.profile.nameSpace}",returnStatus:true)
                println out
                if(out==0){
                  echo 'nocos文件存在，继续后续流程'
                  files=sh(returnStdout: true, script: 'find ${project.profile.nameSpace} -name "*"').split();
                  for(file in files){
                  String[] arr = file.split('/');
                  if(arr.length>1){
                  def filename = arr[arr.length-1];
                  println filename;
                  def content = sh(returnStdout: true, script: 'cat '+file+' | base64')
                  println content
                  def body = [
                  namespace: '${project.profile.nameSpace}',
                  filename: filename,
                  fileContentBase: content
                  ]
                  response = httpRequest consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: toJson(body), url: nacos_url, validResponseCodes: '200'
                  println response
                  }
                  }
                }else if(out==2){
                  echo 'nocos文件不存在，跳过后续流程'
                }else{
                  error("command is error")
                }

            }
           echo '========update config file in nacos end========'
        }
    }
 
          
                    
</#if>