def projectHome='<#if !project.legacyProject && project.pomPath?? && project.pomPath !="">/${project.pomPath}<#else>./</#if>'
                      echo '========Declarative: Post Actions start========'
                      if(projectHome.lastIndexOf('/')!=-1){
                          projectHome=projectHome.substring(0,projectHome.lastIndexOf('/'))
                          projectHome="./"+projectHome
                      }
                      //collect and report to ci
                      def out = sh script: 'java -Dsonar.user=${sonarUser} -Dsonar.password=${sonarPassword} -Dsonar.url=${sonarUrl}  -Dci.address=${baseUrl} -Dproject.tagName=${tagName!'master'} -Dproject.url=${project.scm.url} -jar ci-tool/lib/report-collector-jar-with-dependencies.jar '+projectHome+' ${baseUrl}', returnStdout: true
                      println(out)
                      echo '========Declarative: Post Actions end========'
                      sh script: 'rm -rf ci-tool'
