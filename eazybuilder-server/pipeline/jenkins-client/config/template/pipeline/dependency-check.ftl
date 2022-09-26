<#if !project.profile?? || !project.profile.skipDependencyCheck>
            stage('dependency check'){
               steps {
                  echo '========dependency check start========'
                  <#if project.projectType??&&project.projectType=='npm'>
                    sh '''mkdir -p target'''
                    sh '''sh /usr/share/maven-repo/dependency-check/bin/dependency-check.sh --retireJsUrl=http://0.0.0.0:8080/mirror/retire.js/raw/master/repository/jsrepository.json --project 依赖扫描 --out target --format ALL --scan src/**'''
                  <#elseif project.legacyProject>
                    sh '''mkdir -p target'''
                    sh '''sh /usr/share/maven-repo/dependency-check/bin/dependency-check.sh --retireJsUrl=http://0.0.0.0:8080/mirror/retire.js/raw/master/repository/jsrepository.json --project 依赖扫描 --out target --format ALL --scan src/**/*.jar'''
                  <#else>
                    sh '''mvn <#if !project.legacyProject && project.pomPath?? && project.pomPath !="">-f ${project.pomPath} </#if>org.owasp:dependency-check-maven:6.0.2:aggregate -DretireJsUrl=http://10.129.130.251:8080/mirror/retire.js/raw/master/repository/jsrepository.json -Dmaven.repo.local=/usr/share/maven-repo/teams/${project.team.id} -Dformat=ALL -DautoUpdate=false -DskipProvidedScope=true -DskipSystemScope=true '''
                  </#if>
                  echo '========dependency check end========'
               }
            }
</#if>