<#if project.profile?? && project.profile.sqlRemind?string == "true">
    stage('sql脚本敏感提醒'){
        steps {
            echo '========sql脚本敏感提醒 start========'
            script {
                def toJson = {
                    input ->
                    groovy.json.JsonOutput.toJson(input)
                }
                def remind_url= "http://${gitlab_api_domain}/gitlab-api/hook/parseSqlPipeline"
                files=sh(returnStdout: true, script: 'find -name "*.sql" -o -name "*.SQL"').split();
                for(file in files){
                    String[] arr = file.split('/');
                    if(arr.length>1){
                        def filename = arr[arr.length-1];
                        println filename;
                        def content = sh(returnStdout: true, script: 'cat '+file+' | base64')
                        def fullPath = sh(returnStdout: true, script: 'echo '+file+' | base64')
                        println content
                        println fullPath
                        def body = [
                            baseSqlFile: content,
                            date: '${project.releaseDate?string('yyyy-MM-dd HH:mm:ss')}',
                            fileName: filename,
                            groupName: '${project.team.code}',
                            fullPath: fullPath
                        ]
                        response = httpRequest consoleLogResponseBody: true, contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: toJson(body), url: remind_url, validResponseCodes: '200'
                        println response
                    }
                }
            }
            echo '========sql脚本敏感提醒 end========'
        }
        input {
            message 'sql校验完成是否继续执行?'
            ok '确认'
            parameters {
                choice choices: ['继续执行'],description: 'sql校验完成是否继续执行?',name: 'sqlConfirm'
            }
        }
    }
</#if>